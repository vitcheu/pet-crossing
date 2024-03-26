
package com.vitcheu.owner.service;

public interface MessageSender {
    <D> void sendMessage(D data) ;
}
