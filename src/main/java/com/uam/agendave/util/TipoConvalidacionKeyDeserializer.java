package com.uam.agendave.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.uam.agendave.model.TipoConvalidacion;

public class TipoConvalidacionKeyDeserializer extends KeyDeserializer {

    @Override
    public TipoConvalidacion deserializeKey(String key, DeserializationContext ctxt) {
        try {
            return TipoConvalidacion.valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Tipo de convalidación inválido: " + key, e);
        }
    }
}

