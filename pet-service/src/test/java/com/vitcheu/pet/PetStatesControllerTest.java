package com.vitcheu.pet;

import static com.vitcheu.common.model.PetPropertiesName.HP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.model.Propertychange;
import com.vitcheu.pet.model.Pet;
import com.vitcheu.pet.repository.PetRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test","mysql"})
@AutoConfigureMockMvc
public class PetStatesControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  PetRepository petRepository;

  @Test
  void HandlehpchangeRequest() throws Exception {
    int petId = 1;
    int value = 10;
    Propertychange req = new Propertychange(petId,HP, value);

    Optional<Pet> byId = petRepository.findById(petId);
    assertThat(byId).isPresent();
    Pet pet = byId.get();
    int oriHp = pet.getProperties().getHp();

    mockMvc
      .perform(
        post("/"+PublicUrl.PET_PROPERTIES)
          .contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(req))
      )
      .andExpect(status().isOk())
      .andDo(System.out::println)
      .andReturn();

    Optional<Pet> byId2 = petRepository.findById(petId);
    assertThat(byId2).isPresent();
    Pet pet2 = byId2.get();
    int curHp = pet2.getProperties().getHp();

    assertEquals(oriHp + value, curHp);
  }
}
