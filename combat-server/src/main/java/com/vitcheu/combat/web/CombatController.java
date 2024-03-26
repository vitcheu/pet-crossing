package com.vitcheu.combat.web;

import com.vitcheu.combat.service.MessageSender;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
class CombatController {

  @Resource
  MessageSender sender;

  @GetMapping("test")
  public String showResourcesVetList() {
    return "context loaded";
  }

  @GetMapping("/send")
  public String sendMsgToClient(
    @RequestParam("userId") long userId,
    @RequestParam("msg") String msg
  ) {
    log.info("userId=" + userId + ", msg=" + msg);
    sender.sendText(userId, msg);
    return msg;
  }
}
