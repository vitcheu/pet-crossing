package com.vitcheu.common.model.combat;

public enum MessageType {
  /* combat connecting request sent from  client to server*/
  CONNECT,
  /* combat action sent from client to server*/
  COMBAT,
  /* combat reuslt sent from server to client*/
  RESULT,
  /* server's request sent from server to client*/
  SERVER_REQUEST,
  /* client's response sent from client to server */
  CLIENT_RESPONSE,
  /* server's message sent from server to client */
  SERVER_MESSAGE,
}
