package com.vitcheu.combat.web.client;

import com.vitcheu.common.model.PetDetails;

public interface IPetClient {

    PetDetails getPetFromRemote(Integer id);

}