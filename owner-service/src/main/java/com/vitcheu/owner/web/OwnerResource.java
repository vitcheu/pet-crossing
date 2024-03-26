package com.vitcheu.owner.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.request.AddUserRequest;
import com.vitcheu.owner.context.UserContext;
import com.vitcheu.owner.model.dto.OwnerDetails;
import com.vitcheu.owner.service.MessageSender;
import com.vitcheu.owner.service.OwnerService;

import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Slf4j
public class OwnerResource {

  @Resource
  private OwnerService ownerService;

  @Resource
  MessageSender messageSender;

  @GetMapping("profile/all")
  public List<OwnerDetails> getAllOwner() {
    var list = ownerService.findAll();
    log.info("list: " + list);
    return list;
  }

  /**
   *  get profile of current logined user
   */
  @GetMapping("profile/current")
  public OwnerDetails currentOwner() {
    var userId = UserContext.getUserId();
    OwnerDetails result = ownerService.findOne(userId);
    return result;
  }

  @GetMapping("profile/pets")
  public List<PetDetails> getAllPets() {
    var ownerId = UserContext.getUserId();
    var allPets = ownerService.getAllPets(ownerId);
    return allPets;
  }

  @GetMapping("profile/message")
  public String testMessage(@PathParam("msg") String msg) {
    log.info("msg: " + msg);
    messageSender.sendMessage(msg);
    return msg+" was sent.";
  }

  @PostMapping(PublicUrl.ADD_USER)
  public boolean addUser(@RequestBody AddUserRequest request) {
     return ownerService.addUser(request); 
  }
  
}

