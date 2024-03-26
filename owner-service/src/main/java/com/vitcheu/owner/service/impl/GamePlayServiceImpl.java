package com.vitcheu.owner.service.impl;

import static com.vitcheu.owner.constants.GameplayValue.FavoribylityPlusValueOnPlaying;

import com.vitcheu.common.model.*;
import com.vitcheu.common.model.combat.PropertyChangeDto;
import com.vitcheu.common.model.combat.messages.*;
import com.vitcheu.common.model.request.BuyPropRequest;
import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.common.model.response.PropUsedResult;
import com.vitcheu.owner.context.UserContext;
import com.vitcheu.owner.model.combat.CombatInfo;
import com.vitcheu.owner.model.dto.Prop;
import com.vitcheu.owner.model.event.*;
import com.vitcheu.owner.model.po.Owner;
import com.vitcheu.owner.model.po.PetPo;
import com.vitcheu.owner.repository.*;
import com.vitcheu.owner.service.*;
import com.vitcheu.owner.utils.AppEventPublisher;
import com.vitcheu.owner.web.client.*;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
class GamePlayServiceImpl implements GamePlayService, GameEventListener {

  private static final String COMBAT_SERVER = "combat-server";

  private static final Object COMBAT_SERVICE_URL = "combat";

  @Value("${combatapp.combat-server-url}")
  private String COMBAT_SERVER_URL;

  private static PropertyChangeDto convertToDto(Propertychange c) {
    return new PropertyChangeDto(c);
  }

  @Resource
  private DiscoveryClient discoveryClient;

  @Resource
  PetRepository petRepository;

  @Resource
  PropRepository propRepository;

  @Resource
  OwnerService ownerService;

  @Resource
  PropsClient propsClient;

  @Resource
  PetsClient petsClient;

  @Resource
  OwnerRepository ownerRepository;

  @Resource
  EffecExcutor effecExcutor;

  @Resource
  EffectResultCollector effectResultCollector;

  private ConcurrentHashMap<Long, CombatClient> clientMap = new ConcurrentHashMap<>();

  private ConcurrentHashMap<Long, CombatInfo> combatInfos = new ConcurrentHashMap();

  @Override
  @Transactional
  public PropUsedResult useProp(
    long userId,
    int petId,
    int propId,
    @Valid @Min(1) int count
  ) {
    var owner = ownerService.getOwnerById(userId);
    owner.updatePropsAmount(propId, -count);
    if (!owner.hasProp(propId)) {
      propRepository.deleteByOwnerIdAndPropId(userId, propId);
    } else {
      propRepository.updateCount(propId, userId, -count);
    }

    //the prop takes effect
    Prop prop = propsClient.getPropFromRemote(propId);
    var pet = petsClient.getPetFromRemote(petId);

    effecExcutor.excute(prop, pet, count);

    //collect the effect that applied to the pet
    var changes = effectResultCollector.getRecentChanges(userId);
    var reuslt = buildResult(changes);
    log.info("Prop-Used result:" + reuslt);
    return reuslt;
  }

  @Override
  @Transactional
  public void buyProp(BuyPropRequest req) {
    propsClient.buyPropRequest(req);

    /* update number and money */
    Integer propId = req.propId();
    Prop prop = propsClient.getPropFromRemote(propId);

    int count = req.quantity();
    int price = prop.getPrice();
    long userId = UserContext.getUserId();
    log.info(
      String.format("User@%s buys Prop@%s with $:%s", userId, propId, price)
    );

    updateMoneyAndPropCount(propId, count, price, userId);
  }

  @Override
  @Transactional
  public void addPet(PetRequest petRequest) {
    PetDetails petDetails = petsClient.postAddingPetRequest(petRequest);

    Integer petId = petDetails.getId();
    log.info("new PetId:" + petId);
    long userId = UserContext.getUserId();
    Owner owner = ownerService.getOwnerById(userId);

    var petPo = new PetPo(petId, userId);
    owner.addPet(petPo);

    ownerRepository.save(owner);
  }

  @Override
  @Transactional
  public void removePet(Integer petId) {
    long userId = UserContext.getUserId();
    try {
      petsClient.removePet(petId);

      Owner owner = ownerService.getOwnerById(userId);
      owner.removePet(petId);
      petRepository.deleteByPetIdAndOwnerId(petId, userId);
    } catch (Exception e) {
      log.warn("deleted Pet@" + petId + " failed.");
    }
  }

  @Override
  @Transactional
  public void playWith(Integer petId) {
    var pet = petsClient.getPetFromRemote(petId);
    var properties = pet.getProperties();
    properties.changPropertyByValue(
      PetPropertiesName.FAVORABILITY,
      FavoribylityPlusValueOnPlaying
    );

    AppEventPublisher.publishEvent(
      new PetPropertiesChangedEvent(
        UserContext.getUserId(),
        false,
        new Propertychange(
          petId,
          PetPropertiesName.FAVORABILITY,
          FavoribylityPlusValueOnPlaying
        )
      )
    );
  }

  @Override
  public void sendMessage(String msg) {
    long userId = UserContext.getUserId();

    // CombatMessage combatMessage = new CombatMessage(
    //   msg,
    //   String.valueOf(userId),
    //   String.valueOf(5L)
    // );
    // sendMessage(combatMessage);
    log.info("sending failed!!!");
  }

  @Override
  public void startBattle(int petId, long oponent) {
    long userId = UserContext.getUserId();
    log.info(
      String.format(
        "Player@%s fighting with Player@%s using Pet@%s",
        userId,
        oponent,
        petId
      )
    );

    /* send connect request and wait the other side to connect */
    var msg = CombatMessage.conncetRequestMessage(oponent, petId);
    sendMessageInternal(msg);

    var info = new CombatInfo(oponent, petId);
    combatInfos.put(userId, info);
  }

  @Override
  public boolean getConnectionState() {
    var info = combatInfos.get(UserContext.getUserId());
    return info != null && info.isConnecting();
  }

  @Override
  @EventListener
  public void onCombatReadyEvent(@NonNull CombatReadyEvent event) {
    long userId = event.getUserId();
    log.info(
      "The combat between User@" + userId + " and its oponent  is ready."
    );

    var info = combatInfos.get(userId);
    info.startCombat();
  }

  @Override
  @EventListener
  public void onTurnResult(@NonNull TurnResultEvent e) {
    log.info(
      String.format("Turn reuslt of User%s:%s", e.getUserId(), e.getResult())
    );

    var result = e.getResult();
    var userId = e.getUserId();
    var info = combatInfos.get(userId);
    info.setLastTurnResult(result);
  }

  @Override
  public void disconnect() {
    long userId = UserContext.getUserId();
    var client = clientMap.get(userId);
    if (client != null) {
      try {
        client.closeBlocking();
      } catch (InterruptedException e) {
        e.printStackTrace();
        while (!client.isClosed()) {
          client.close();
        }
      } finally {
        clientMap.remove(userId);
        combatInfos.remove(userId);
      }
    }
  }

  @Override
  public void postTurnAction(TurnAction action) {
    var msg = CombatMessage.actionMessage(action);
    sendMessageInternal(msg);
  }

  private PropUsedResult buildResult(final List<Propertychange> changes) {
    var content = changes
      .stream()
      .map(GamePlayServiceImpl::convertToDto)
      .toList();
    PropUsedResult result = new PropUsedResult(content);
    return result;
  }

  private CombatClient getClient() {
    long userId = UserContext.getUserId();
    String url = getUrl(userId);

    var client = clientMap.get(userId);

    if (client == null || client.isClosed()) {
      client = startClient(userId, url);
      clientMap.put(userId, client);
    } else {
      log.info("Already connected.");
    }

    return client;
  }

  private String getUrl(long userId) {
    var instances = discoveryClient.getInstances(COMBAT_SERVER);
    if (instances.isEmpty()) {
      throw new RuntimeException("Unable to find combat server.");
    } else {
      var instance = instances.get(0);
      var instanceUri = String.format(
        "%s:%s",
        instance.getHost(),
        instance.getPort()
      );
      var url = String.format(
        "ws://%s/%s/%s",
        instanceUri,
        COMBAT_SERVICE_URL,
        userId
      );
      return url;
    }
  }

  private CombatClient startClient(long userId, String url) {
    log.info("start connecting to " + url + "...");
    try {
      CombatClient client = CombatClientFactory.createClient(url, userId);
      client.connectBlocking();
      log.info("connected!");
      return client;
    } catch (Exception e) {
      clientMap.remove(userId);
      throw new RuntimeException(e);
    }
  }

  private void sendMessageInternal(CombatMessage message) {
    CombatClient client = getClient();
    if (!client.isOpen()) {
      throw new RuntimeException("Connection is closed.");
    }
    client.sendMessage(message);
  }

  private void updateMoneyAndPropCount(
    Integer propId,
    int count,
    int price,
    long userId
  ) {
    var owner = ownerService.getOwnerById(userId);
    var money = owner.getMoney();
    if (money < count * price) {
      throw new com.vitcheu.common.exception.UnsupportedOperationException(
        "Not enough money"
      );
    }

    var newProp = owner.addProp(propId, count);
    if (newProp != null) {
      propRepository.save(newProp);
    }

    owner.setMoney(money - count * price);
    ownerRepository.updateMoney(userId, -count * price);
  }

  @Override
  public Optional<TurnResult> getTurnResult() {
    var info = combatInfos.get(UserContext.getUserId());

    if (info == null) {
      throw new UnsupportedOperationException("must start a battle first");
    }
    return Optional.ofNullable(info.getLastTurnResult());
  }
}
