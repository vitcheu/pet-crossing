package com.vitcheu.combat.listener;

import com.vitcheu.common.constants.message.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

// @Component
@Slf4j
public class CombatMessagesListener {

  @RabbitListener(
    bindings = @QueueBinding(
      exchange = @Exchange(
        name = MessageConstants.DIRECT_EXCHANG2,
        type = ExchangeTypes.DIRECT
      ),
      value = @Queue(name = MessageConstants.QUEUE_NAME2, durable = "true"),
      key = { "red", "blue" }
    )
  )
  void directExchangeListener(Object msg) {
    log.info(
      String.format(
        "[Thread:%s]#22 Received message: %s",
        Thread.currentThread(),
        msg
      )
    );
  }
}
