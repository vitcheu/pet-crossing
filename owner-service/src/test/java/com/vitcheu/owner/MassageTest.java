package com.vitcheu.owner;

import com.vitcheu.common.constants.message.MessageConstants;
import jakarta.annotation.Resource;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MassageTest {

  @Resource
  private RabbitTemplate rabbitTemplate;

  @Test
  void testSendingMessage() {
    String queueName = MessageConstants.QUEUE_NAME1;
    String msg = "Hello From Java2";
    log.info(
      String.format(
        "[Thread:%s] sending message: %s",
        Thread.currentThread(),
        msg
      )
    );
    rabbitTemplate.convertAndSend(queueName, msg);
  }

  @Test
  void testMultiMessages() throws InterruptedException {
    String queueName = MessageConstants.QUEUE_NAME2;
    int num = 50;
    String bseMsg = "Multi message";
    for (int i = 0; i < num; i++) {
      var msg = bseMsg + String.valueOf((i + 1));
      rabbitTemplate.convertAndSend(queueName, msg);
      Thread.sleep(1000 / num);
    }
  }

  @Test
  void sendToFanout() {
    String msg = "Send to Fanout";
    log.info(
      String.format(
        "[Thread:%s] sending message: %s",
        Thread.currentThread(),
        msg
      )
    );
    for (int i = 0; i < 3; i++) {
      rabbitTemplate.convertAndSend(MessageConstants.FAN_EXCHANG, null, msg);
    }
  }

  @Test
  void sendToDirect() throws InterruptedException {
    String redMsg = "redMsg";
    String yellowMsg = "yellowMsg";
    log.info(
      String.format("[Thread:%s] sending messages", Thread.currentThread())
    );

    log.info("Sending redMsg\n");
    String exchange = MessageConstants.DIRECT_EXCHANG2;
    rabbitTemplate.convertAndSend(exchange, "red", redMsg);
    Thread.sleep(1000);

    log.info("Sending yellowMsg\n");
    rabbitTemplate.convertAndSend(exchange, "yellow", yellowMsg);

    log.info("Sending blueMsg\n");
    String blueMsg = "blueMsg";
    rabbitTemplate.convertAndSend(exchange, "blue", blueMsg);
  }

  @Test
  void sendObjectMsg() {
    Map<String, Object> map = Map.of("name", "Jack", "age", 16);

    String queueName = MessageConstants.QUEUE_NAME2;
    rabbitTemplate.convertAndSend(
        queueName,
        map
    );
    log.info("Send succeeded.");
  }
}
