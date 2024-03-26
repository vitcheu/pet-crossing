package com.vitcheu.combat.service.impl;

import static com.vitcheu.common.model.PetPropertiesName.DEF;
import static com.vitcheu.common.model.PetPropertiesName.HP;

import com.vitcheu.combat.context.PlayerContextHoler;
import com.vitcheu.combat.event.MessageReceivedEvent;
import com.vitcheu.combat.event.SendSocketMessageRequestEvent;
import com.vitcheu.combat.model.CombatInfo;
import com.vitcheu.combat.model.builder.PropertychangDtoBuilder;
import com.vitcheu.combat.service.TurnHandler;
import com.vitcheu.combat.utils.AppEventPublisher;
import com.vitcheu.combat.web.client.IPetClient;
import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.PetProperties;
import com.vitcheu.common.model.combat.*;
import com.vitcheu.common.model.combat.messages.*;
import com.vitcheu.common.model.request.UsePropsRequest;
import com.vitcheu.common.model.response.PropUsedResult;
import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TurnHandlerImpl implements TurnHandler {

  private static enum PET_PRIORITY {
    FIRST_PET,
    SECOND_PET,
  }

  private static final int LUCK_FLOW_RANGE = 1;

  private static final int DEF_INC_TIME = 2;

  private final ConcurrentHashMap<Integer, PropertychangDtoBuilder> builderMap = new ConcurrentHashMap<>();

  private final ConcurrentHashMap<Long, BlockingQueue<PropUsedResult>> resultQueues = new ConcurrentHashMap<>();

  @Resource
  private IPetClient petClient;

  @Override
  @Async("taskExecutor")
  public void handle(
    CombatInfo combatInfo,
    long userId,
    Consumer<TurnResult> resultHandler
  ) {
    //store userid
    PlayerContextHoler.setLocalContext(userId);

    var p1 = combatInfo.getFirstPlayer();
    var p2 = combatInfo.getSecondPlayer();
    var petId1 = p1.getPetId();
    var petId2 = p2.getPetId();
    var action1 = p1.getAction();
    var action2 = p2.getAction();
    log.info("Handling turn actions...");
    log.debug("p1: " + p1);
    log.debug("p2: " + p2);

    preHandle(petId1, petId2);

    /* retriving infos of pets */
    var pet1 = petClient.getPetFromRemote(p1.getPetId());
    var pet2 = petClient.getPetFromRemote(p2.getPetId());

    var property1 = pet1.getProperties();
    var property2 = pet2.getProperties();

    var actionInfo1 = new ActionInfo(pet1, pet2, action1, action2);
    var actionInfo2 = new ActionInfo(pet2, pet1, action2, action1);

    List<ActionInfo> handlingActionInfos = new ArrayList<>();
    handlingActionInfos.add(actionInfo1);
    handlingActionInfos.add(actionInfo2);

    if (
      getPrority(action1, action2, property1, property2)
        .equals(PET_PRIORITY.SECOND_PET)
    ) {
      /* chang the order of the actions beeing handled*/
      Collections.reverse(handlingActionInfos);
    }

    for (var info : handlingActionInfos) {
      handleAction(info);
    }

    Map<Integer, List<PropertyChangeDto>> result = new HashMap<>();
    for (int id : List.of(petId1, petId2)) {
      final var buidler = getBuilder(id);
      final var dto = buidler.build();
      result.put(id, dto);
    }

    postHandle(petId1, petId2);

    log.info("Returning result: " + result);
    var turnResult = new TurnResult(result);
    resultHandler.accept(turnResult);
  }

  private void preHandle(int petId1, int petId2) {
    var builder1 = new PropertychangDtoBuilder();
    var builder2 = new PropertychangDtoBuilder();
    builderMap.put(petId1, builder1);
    builderMap.put(petId2, builder2);
  }

  private PropertychangDtoBuilder getBuilder(int petId) {
    var builder = builderMap.get(petId);
    Objects.requireNonNull(builder);
    return builder;
  }

  private void postHandle(int petId1, int petId2) {
    builderMap.remove(petId1);
    builderMap.remove(petId2);
  }

  /**
   * decide which pet takes action first
   * depends on the action type:
   * DEFENCE > ATTAK > PROPS
   */
  private PET_PRIORITY getPrority(
    TurnAction action1,
    TurnAction action2,
    PetProperties property1,
    PetProperties property2
  ) {
    var comparator = Comparator.comparing(TurnActionType::getPriority);
    int result = comparator.compare(
      action1.getActionType(),
      action2.getActionType()
    );
    log.info("compare result: " + result);

    return switch (result) {
      case 0 -> getProrityInternal(property1, property2);
      case 1 -> PET_PRIORITY.FIRST_PET;
      default -> PET_PRIORITY.SECOND_PET;
    };
  }

  /**
   * decide which pet attacks first,
   * bacically depends on speed and luck
   * @return @Code{FIRST_PET} indicates the first pet wins,
   * therefore @Code{SECOND_PET} indicates the other wins.
   */
  private PET_PRIORITY getProrityInternal(
    PetProperties property1,
    PetProperties property2
  ) {
    var speed1 = property1.getSpeed();
    var speed2 = property2.getSpeed();
    if (speed1 > speed2) {
      return PET_PRIORITY.FIRST_PET;
    } else if (speed2 > speed1) {
      return PET_PRIORITY.SECOND_PET;
    } else {
      var luck1 = property1.getLuck();
      var luck2 = property2.getLuck();

      if (luck1 == luck2) {
        Random random = new Random();
        var additon = random.nextInt(-LUCK_FLOW_RANGE, LUCK_FLOW_RANGE);
        luck1 += additon;
      }

      if (luck1 > luck2) {
        return PET_PRIORITY.FIRST_PET;
      } else {
        return PET_PRIORITY.SECOND_PET;
      }
    }
  }

  public void handleAction(ActionInfo actionInfo) {
    var action = actionInfo.action1();
    log.info("handling: " + actionInfo);

    action.getActionType().accpet(this, actionInfo);
  }

  @Override
  public void handleDefence(ActionInfo info) {
    var pet1 = info.pet1();
    pet1.getProperties().changPropertyByTime(DEF, DEF_INC_TIME);
  }

  /**
   * pet1 attacks pet2
   */
  @Override
  public void handleAttack(ActionInfo info) {
    var pet1 = info.pet1();
    var pet2 = info.pet2();

    var property1 = pet1.getProperties();
    var property2 = pet2.getProperties();

    var atk1 = property1.getAtk();
    var def2 = property2.getDef();

    int diff = atk1 - def2;
    diff = (diff > 0) ? diff : 0;

    var change = new PropertyChangeDto(HP, -diff);
    log.info("change of the attack: " + change);
    buildChange(pet2.getId(), change);
  }

  private PropertychangDtoBuilder buildChange(
    int petId,
    PropertyChangeDto change
  ) {
    return getBuilder(petId).chaneBy(change);
  }

  @Override
  public void handleProps(ActionInfo info) {
    var action = (UsePropsAction) info.action1();
    var pet1 = info.pet1();
    var ownerId = pet1.getOwnerId();
    var req = UsePropsRequest
      .builder()
      .petId(pet1.getId())
      .propId(action.getPropId())
      .count(action.getCount())
      .build();

    var serverRequest = new ServerRequest<UsePropsRequest>(req);
    var msg = CombatMessage.serverRequest(serverRequest);

    AppEventPublisher.publishEvent(
      new SendSocketMessageRequestEvent(msg, ownerId)
    );

    takeResult(pet1, ownerId);
  }

  public void takeResult(PetDetails pet1, Long ownerId) {
    log.info("taking result from queue provided by the other thread.");
    /* waiting for the result of prop-used */
    var resultQueue = getResultQueue(ownerId);
    try {
      PropUsedResult result = resultQueue.take();
      for (var change : result.changes()) {
        buildChange(pet1.getId(), change);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private synchronized BlockingQueue<PropUsedResult> getResultQueue(
    long ownerId
  ) {
    var queue = resultQueues.get(ownerId);
    if (queue == null) {
      queue = new LinkedBlockingQueue<PropUsedResult>(1);
      resultQueues.put(ownerId, queue);
    }
    return queue;
  }

  @Override
  @EventListener
  public void onMessageReceived(MessageReceivedEvent event) {
    log.info(
      String.format(
        "[%s] Received message: %s",
        event.getOwnerId(),
        event.getMessage()
      )
    );
    var message = event.getMessage();
    CombatData payload = message.getPayload();
    if (payload instanceof ClientResponse) {
      var result = ((ClientResponse) payload).getPropUsedResult();

      var resultQueue = getResultQueue(event.getOwnerId());
      try {
        resultQueue.put(result);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
