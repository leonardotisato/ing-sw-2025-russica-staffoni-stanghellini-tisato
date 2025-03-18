package it.polimi.ingsw.cg04.model.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;
import it.polimi.ingsw.cg04.model.enumerations.Shot;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardLoader {
    public static Map<Integer, AdventureCard> loadCardsFromJson(String jsonFilePath, List<Integer> level1Cards, List<Integer> level2Cards) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

//                .registerTypeAdapter(BoxType.class, new EnumDeserializer<>(BoxType.class))
//                .registerTypeAdapter(Direction.class, new EnumDeserializer<>(Direction.class))
//                .registerTypeAdapter(Meteor.class, new EnumDeserializer<>(Meteor.class))
//                .registerTypeAdapter(new TypeToken<Map<BoxType, Integer>>() {}.getType(), new BoxTypeMapDeserializer())
//                .registerTypeAdapter(new TypeToken<List<Direction>>() {}.getType(), new EnumListDeserializer<>(Direction.class))
//                .registerTypeAdapter(new TypeToken<List<Meteor>>() {}.getType(), new EnumListDeserializer<>(Meteor.class))
//                .registerTypeAdapter(new TypeToken<List<Shot>>() {}.getType(), new EnumListDeserializer<>(Shot.class))
//                .registerTypeAdapter(new TypeToken<List<Map<BoxType, Integer>>>() {}.getType(), new BoxTypeListMapDeserializer())
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type mapType = new TypeToken<Map<String, JsonObject>>() {}.getType();
            Map<String, JsonObject> tempMap = gson.fromJson(reader, mapType);

            Map<Integer, AdventureCard> cardMap = new HashMap<>();
            for (Map.Entry<String, JsonObject> entry : tempMap.entrySet()) {
                int id = Integer.parseInt(entry.getKey());
                AdventureCard card = createCardFromJson(entry.getValue());
                cardMap.put(id, card);
                if (card.getCardLevel() == 1) level1Cards.add(id);
                else if (card.getCardLevel() == 2) level2Cards.add(id);
            }
            return cardMap;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static AdventureCard createCardFromJson(JsonObject jsonObject) {
        String type = jsonObject.get("type").getAsString();
        return switch (type) {
            case "AbandonedShip" -> new Gson().fromJson(jsonObject, AbandonedShip.class);
            case "AbandonedStation" -> new Gson().fromJson(jsonObject, AbandonedStation.class);
            case "Epidemic" -> new Gson().fromJson(jsonObject, Epidemic.class);
            case "MeteorsRain" -> new Gson().fromJson(jsonObject, MeteorsRain.class);
            case "OpenSpace" -> new Gson().fromJson(jsonObject, OpenSpace.class);
            case "Pirates" -> new Gson().fromJson(jsonObject, Pirates.class);
            case "Planets" -> new Gson().fromJson(jsonObject, Planets.class);
            case "Slavers" -> new Gson().fromJson(jsonObject, Slavers.class);
            case "Smugglers" -> new Gson().fromJson(jsonObject, Smugglers.class);
            case "Stardust" -> new Gson().fromJson(jsonObject, Stardust.class);
            case "WarZone" -> new Gson().fromJson(jsonObject, WarZone.class);
            default -> throw new JsonParseException("Tipo sconosciuto: " + type);
        };
    }
}
