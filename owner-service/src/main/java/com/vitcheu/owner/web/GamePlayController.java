package com.vitcheu.owner.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vitcheu.common.model.combat.messages.TurnAction;
import com.vitcheu.common.model.request.*;
import com.vitcheu.common.model.response.ApiResponse;
import com.vitcheu.common.model.response.PropUsedResult;
import com.vitcheu.owner.context.UserContext;
import com.vitcheu.owner.service.GamePlayService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * API for gameplay
 */
@RestController
@RequestMapping("/gameplay")
public class GamePlayController {

  @Resource
  private GamePlayService gameplayService;

  @PostMapping("props/use")
  public ApiResponse<Object> userProp(
    @Valid @RequestBody UsePropsRequest request
  ) {
    PropUsedResult result = gameplayService.useProp(
      UserContext.getUserId(),
      request.petId(),
      request.propId(), request.count()
    );

    return ApiResponse.builder().message("Prop used.").body(result).build();
  }

  @PostMapping("/action/play-with/{petId}")
  public ApiResponse<Object> playWith(@PathVariable("petId") Integer petId) {
    gameplayService.playWith(petId);

    return ApiResponse.builder().message("Ok").build();
  }

  @PostMapping("props/buy")
  public ApiResponse<Object> buyProp(
    @Valid @RequestBody BuyPropRequest request
  ) {
    gameplayService.buyProp(request);

    return ApiResponse.builder().message("Prop bought.").build();
  }

  @PostMapping("pets/new")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ApiResponse<Object> addNewPets(@RequestBody PetRequest petRequest) {
    gameplayService.addPet(petRequest);

    return ApiResponse.builder().message("Pet added.").build();
  }

  @PostMapping("/pets/remove/{petId}")
  @ResponseStatus(code = HttpStatus.OK)
  public ApiResponse<Object> removePet(@PathVariable("petId") Integer petId) {
    gameplayService.removePet(petId);

    return ApiResponse.builder().message("Pet removed.").build();
  }

  @GetMapping("/send")
  public ApiResponse<Object> sendMessage(@RequestParam("msg") String msg) {
    gameplayService.sendMessage(msg);

    return ApiResponse.builder().body(msg).message("ok").build();
  }

  @PostMapping("/battle")
  public ApiResponse<Object> battleWith(
    @RequestParam("oponent") long oponent,
    @RequestParam("petId") int petId
  ) {
    gameplayService.startBattle(petId, oponent);

    return ApiResponse.builder().message("Ok,started.").build();
  }

  @PostMapping("/battle/action")
  public ApiResponse<Object> turnAction(@RequestBody TurnAction action) {
    gameplayService.postTurnAction(action);

    return ApiResponse
      .builder()
      .message("Ok, action has been Sent and Accept.")
      .build();
  }

  @PostMapping("/battle/disconnect")
  public void disConnect() {
    gameplayService.disconnect();
  }

  /*
   * endpoint to be polled for conection state.
   */
  @GetMapping("/battle/connection-state")
  public ApiResponse<Object> connectState() {
    boolean state = gameplayService.getConnectionState();
    String msg = state ? "Connected" : "Unconnected";

    return ApiResponse.builder().message(msg).build();
  }

  /*
   * endpoint to be polled for turn result.
   */
  @GetMapping("/battle/turn-result")
  public ApiResponse<Object> turnResult() {
    var turnResult = gameplayService.getTurnResult();
    var msg = turnResult.isPresent()
      ? "Turn result available"
      : "No turn result available";
    return ApiResponse
      .builder()
      .message(msg)
      .body(turnResult.map(i -> i).orElse(null))
      .build();
  }
}
