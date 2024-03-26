package com.vitcheu.pet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.pet.repository.PetRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PetResourceTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  PetRepository petRepository;

  @Test
  void createPet() throws Exception {
    long ownerId = 1L;
    PetRequest petRequest = new PetRequest(
      0,
      Date.from(Instant.now()),
      "Slime",
      1
    );

    MvcResult result = mockMvc
      .perform(
        post("/owners/" + ownerId + "/pets")
          .contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(petRequest))
      )
    //   .andExpect(status().isOk())
      .andDo(System.out::println)
      .andReturn();
  }
}
