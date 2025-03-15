package it.polimi.ingsw.cg04.model.utils;

import com.google.gson.*;
import java.lang.reflect.Type;

public class EnumDeserializer<T extends Enum<T>> implements JsonDeserializer<T> {

    private final Class<T> enumClass;

    public EnumDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return Enum.valueOf(enumClass, json.getAsString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Valore non valido per " + enumClass.getSimpleName() + ": " + json.getAsString());
        }
    }
}

