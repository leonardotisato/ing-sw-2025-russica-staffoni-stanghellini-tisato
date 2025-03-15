package it.polimi.ingsw.cg04.model.utils;

import com.google.gson.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BoxTypeMapDeserializer implements JsonDeserializer<Map<BoxType, Integer>> {
    @Override
    public Map<BoxType, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Map<BoxType, Integer> boxes = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            try {
                BoxType key = BoxType.valueOf(entry.getKey()); // Converte la chiave String in Enum
                int value = entry.getValue().getAsInt();
                boxes.put(key, value);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Tipo di effetto sconosciuto: " + entry.getKey());
            }
        }
        return boxes;
    }
}

