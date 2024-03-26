package com.vitcheu.owner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CombatServiceTest {

  private static final String TMX_USER_ID = "tmx-user-id";

  private static final String PET_ID = "petId";

  private static final String OPONENT = "oponent";

  private static final String COMBAT_URL = "/gameplay/battle";

  @Autowired
  MockMvc mockMvc;

  @Test
  void testConnection() throws Exception {
    connect("5", "1", 3);
    connect("3", "3", 5);

  }

  private void connect(String oponent, String pet, int userId) throws Exception {
    mockMvc
      .perform(
        post(COMBAT_URL)
          .param(OPONENT, oponent)
          .param(PET_ID, pet)
          .header(TMX_USER_ID, userId)
      )
      .andExpect(status().isOk())
      .andDo(System.out::println)
      .andReturn();
  }
}
