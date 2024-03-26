package com.vitcheu.owner.service.impl;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.request.AddUserRequest;
import com.vitcheu.owner.config.CacheConfig;
import com.vitcheu.owner.model.dto.*;
import com.vitcheu.owner.model.mapper.OwnerMapper;
import com.vitcheu.owner.model.mapper.PropMapper;
import com.vitcheu.owner.model.po.*;
import com.vitcheu.owner.repository.OwnerRepository;
import com.vitcheu.owner.service.OwnerService;
import com.vitcheu.owner.web.client.*;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

  private static final int INIT_MONEY = 1000;

  @Resource
  private OwnerRepository ownerRepository;

  @Resource
  PetsClient petsClient;

  @Resource
  PropsClient propsClient;

  @Resource
  private ProfileClient profileClient;

  @Resource
  private ModelMapper modelMapper;

  @Override
  public List<OwnerDetails> findAll() {
    final var owners = ownerRepository.findAll();
    List<OwnerDetails> ownerDetailsList = owners
      .stream()
      .map(this::getOwnerDetails)
      .toList();
    return ownerDetailsList;
  }

  @Override
  public OwnerDetails findOne(long id) {
    OwnerServiceImpl proxy = proxy();
    Owner owner = proxy.getOwnerById(id);
    OwnerDetails ownerDetails = getOwnerDetails(owner);

    return ownerDetails;
  }

  @Override
  public List<PetDetails> getAllPets(long ownerId) {
    Owner owner = proxy().getOwnerById(ownerId);
    List<PetPo> petPos = owner.getPets();

    List<Integer> ids = petPos.stream().map(p -> p.getPetId()).toList();
    List<PetDetails> pets = petsClient.getPetsFromRemote(ids);

    log.info("Returning pets: " + pets);
    return pets;
  }

  private OwnerServiceImpl proxy() {
    return (OwnerServiceImpl) AopContext.currentProxy();
  }

  @Override
  @Cacheable(cacheNames = CacheConfig.CACHE_OWNER_REPO)
  public Owner getOwnerById(long ownerId) {
    Optional<Owner> byId = ownerRepository.findById(ownerId);
    if (byId.isEmpty()) {
      throw new ResourceNotFoundException(
        "Could not find Owner with Id:" + ownerId
      );
    }

    Owner owner = byId.get();
    return owner;
  }

  private OwnerDetails getOwnerDetails(Owner owner) {
    List<OwnedProp> ownedProps = owner.getOwnedProps();

    var propDetails = ownedProps.stream().map(this::getMappedDetails).toList();
    propDetails.forEach(p -> log.info("PropDetail:" + p));

    OwnerDetails ownerDetails = OwnerMapper.mapOwnerDetails(owner, propDetails);

    /* Gets priflie from remote */
    try {
      var profile = profileClient.getProfileFromRemote(owner.getUserId());
      ownerDetails.setEmail(profile.email());
      ownerDetails.setUsername(profile.userName());
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("getting profile from auth-server failed.");
    }
    return ownerDetails;
  }

  /*
   * Get prop details from remote
   */
  private PropDetails getMappedDetails(OwnedProp op) {
    Prop prop = propsClient.getPropFromRemote(op.getPropId());
    if (prop == null) {
      return null;
    }

    PropDetails mappeDetails = PropMapper.mapToDetails(prop, op);
    return mappeDetails;
  }

  @Override
  public boolean addUser(AddUserRequest request) {
    Owner owner = new Owner();
    assert request.userId()>0;
    owner.setUserId(request.userId());
    owner.setMoney(INIT_MONEY);
    try {
      ownerRepository.save(owner);
      return true;
    } catch (Exception e) {
      log.error("Error saving owner: " + e);
      return false;
    }
  }
}
