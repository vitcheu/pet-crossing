package com.vitcheu.owner.service.impl;

import static com.vitcheu.common.model.PetPropertiesName.*;
import static com.vitcheu.owner.model.effect.PetPropertiesChangeEffect.hpchangEffect;

import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.stereotype.Service;

import com.vitcheu.owner.model.dto.Prop;
import com.vitcheu.owner.model.effect.Effect;
import com.vitcheu.owner.model.effect.PetPropertiesChangeEffect;
import com.vitcheu.owner.service.EffecExcutor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EffectExcutorImpl implements EffecExcutor {

  private static Map<Integer, Effect<?>> propToEffectMapper = new HashMap<>();

  static {
    /* 好伤药 */
    propToEffectMapper.put(1, hpchangEffect(10));

    /* 饭团 */
    propToEffectMapper.put(2, new PetPropertiesChangeEffect(FAVORABILITY, 5));

    /* 缎带 */
    propToEffectMapper.put(3, new PetPropertiesChangeEffect(ATK, 5));

    /* 神奇糖果 */
    propToEffectMapper.put(4, new PetPropertiesChangeEffect(MP, 10));
  }

  @Override
  public <T> void excute(Prop prop, T targetEntity, int count) {
    log.info("excuting:" + prop + ", entity:" + targetEntity);

    var effect = (Effect<T>) getEffect(prop);
    effect.takesEffect(targetEntity, count);
  }

  public Effect<?> getEffect(Prop prop) {
    Effect<?> effect = propToEffectMapper.get(prop.getId());
    if (effect == null) {
      throw new RuntimeCryptoException("Unsupported!");
    }
    return effect;
  }
}
