package com.vitcheu.combat.web.client;

import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.PetProperties;
import java.time.Instant;
import java.util.Date;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class PetClientDev implements IPetClient {

  @Override
  public PetDetails getPetFromRemote(Integer id) {
    PetDetails petDetails = new PetDetails(
      id,
      "Alice",
      1L,
      Date.from(Instant.now()),
      "dog",
      new PetProperties(25, 25, 25, 25, 10, 10, 10)
    );

    return petDetails;
  }
}
