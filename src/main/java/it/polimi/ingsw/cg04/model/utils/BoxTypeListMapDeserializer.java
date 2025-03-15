package it.polimi.ingsw.cg04.model.utils;
import com.google.gson.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.lang.reflect.Type;
import java.util.*;

public class BoxTypeListMapDeserializer implements JsonDeserializer<List<Map<BoxType, Integer>>> {
    @Override
    public List<Map<BoxType, Integer>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        List<Map<BoxType, Integer>> listOfMaps = new ArrayList<>();

        // Controlliamo che sia un array JSON
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();

            for (JsonElement element : jsonArray) {
                Map<BoxType, Integer> map = new HashMap<>();
                JsonObject jsonObject = element.getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    try {
                        BoxType key = BoxType.valueOf(entry.getKey()); // Converte String → Enum
                        int value = entry.getValue().getAsInt();
                        map.put(key, value);
                    } catch (IllegalArgumentException e) {
                        throw new JsonParseException("Tipo di effetto sconosciuto: " + entry.getKey());
                    }
                }
                listOfMaps.add(map); // Aggiungiamo la mappa alla lista
            }
        }

        return listOfMaps;
    }
}

