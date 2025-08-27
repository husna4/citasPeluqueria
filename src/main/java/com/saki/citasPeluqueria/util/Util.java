package com.saki.citasPeluqueria.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author husnain
 */
public class Util {

    public static <E,D> List<D> convertListEntityToDto(List<E> entities, Class<D> dtoClass, ModelMapper modelMapper){
        return entities.stream()
                .map(e -> modelMapper.map(e, dtoClass))
                .collect(Collectors.toList());
    }
}
