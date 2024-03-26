package com.vitcheu.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcheu.common.model.request.UsePropsRequest;
import com.vitcheu.owner.model.po.OwnedProp;
import com.vitcheu.owner.repository.PetRepository;
import com.vitcheu.owner.repository.PropRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class GameplayTest {

  @Autowired
  MockMvc mockMvc;


  @Autowired
  PetRepository petRepository;

  @Autowired
  PropRepository propRepository;

  @Test
  void testPropRepoUpdate() throws Exception {
    int petId = 1;
    int propId = 1;
    long userId = 1L;
    int count = 0;
    UsePropsRequest req = new UsePropsRequest(petId, propId, count);

    OwnedProp prop = propRepository.findByPropId(propId).get();
    int originalAmount = prop.getAmount();

    mockMvc
      .perform(
        post("/gameplay/props/use")
          .contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(req))
      )
      .andExpect(status().isOk())
      .andDo(System.out::println)
      .andReturn();

    OwnedProp prop2 = propRepository.findByPropId(propId).get();
    int curAmount = prop2.getAmount();

    assertEquals(originalAmount +count, curAmount);
  }
}
