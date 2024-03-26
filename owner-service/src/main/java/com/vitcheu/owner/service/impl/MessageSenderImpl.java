
package com.vitcheu.owner.service.impl;

import org.springframework.stereotype.Service;

import com.vitcheu.owner.service.MessageSender;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageSenderImpl implements MessageSender{
    // @Resource
    // private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public <D> void sendMessage(D data) {
        log.info("sending message: "+data);
        // kafkaTemplate.sendDefault(data);
    }
}
