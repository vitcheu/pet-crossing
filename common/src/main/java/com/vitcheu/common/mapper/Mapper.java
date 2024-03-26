package com.vitcheu.common.mapper;

public interface Mapper<R, E> {
   E map(E response, R request);
}
