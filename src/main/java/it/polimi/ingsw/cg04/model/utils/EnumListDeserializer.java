package it.polimi.ingsw.cg04.model.utils;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EnumListDeserializer<T extends Enum<T>> implements JsonDeserializer<List<T>> {

    private final Class<T> enumClass;

    public EnumListDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        List<T> enumList = new ArrayList<>();
        if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                try {
                    enumList.add(Enum.valueOf(enumClass, element.getAsString().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException("Valore non valido per " + enumClass.getSimpleName() + ": " + element.getAsString());
                }
            }
        }
        return enumList;
    }
}

