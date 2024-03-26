package com.vitcheu.owner.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.vitcheu.common.constants.message.MessageConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("rabbitMq")
@Slf4j
public class MessageListener {

  // @RabbitListener(queues = MessageConstants.QUEUE_NAME1)
  // void handle(String msg) throws InterruptedException {
  //   log.info(
  //     String.format(
  //       "[Thread:%s]#1 Received message: %s",
  //       Thread.currentThread(),
  //       msg
  //     )
  //   );
  // }

  // @RabbitListener(queues = MessageConstants.QUEUE_NAME1)
  // void handle2(String msg) throws InterruptedException {
  //   log.info(
  //     String.format(
  //       "[Thread:%s]#12 Received message: %s",
  //       Thread.currentThread(),
  //       msg
  //     )
  //   );
  // }

  // @RabbitListener(queues = MessageConstants.QUEUE_NAME2)
  // void listener2(Object msg) throws InterruptedException {
  //   log.info(
  //     String.format(
  //       "[Thread:%s]#2 Received message: %s",
  //       Thread.currentThread(),
  //       msg
  //     )
  //   );
  // }

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
  // @RabbitListener(queues = MessageConstants.QUEUE_TEST_SIMPLE)
  // void objectHandler(Map<String, Object> map) {
  //   log.info("Received :" + map);
  //   map.forEach((k, v) -> System.out.println(String.format("(%s,%s)", k, v)));
  // }
}
