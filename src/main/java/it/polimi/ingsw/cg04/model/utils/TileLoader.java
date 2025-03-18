package it.polimi.ingsw.cg04.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;
import it.polimi.ingsw.cg04.model.enumerations.Shot;
import it.polimi.ingsw.cg04.model.tiles.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileLoader {

    public static Map<Integer, Tile> loadTilesFromJson(String jsonFilePath, List<Integer> faceDownTiles) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type mapType = new TypeToken<Map<String, JsonObject>>() {}.getType();
            Map<String, JsonObject> tempMap = gson.fromJson(reader, mapType);

            Map<Integer, Tile> tileMap = new HashMap<>();
            for (Map.Entry<String, JsonObject> entry : tempMap.entrySet()) {
                int id = Integer.parseInt(entry.getKey());
                Tile card = createTileFromJson(entry.getValue());
                tileMap.put(id, card);
                faceDownTiles.add(id);
            }
            return tileMap;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static Tile createTileFromJson(JsonObject jsonObject) {
        String type = jsonObject.get("type").getAsString();
        return switch (type) {
            case "AlienSupportTile" -> new Gson().fromJson(jsonObject, AlienSupportTile.class);
            case "BatteryTile" -> new Gson().fromJson(jsonObject, BatteryTile.class);
            case "HousingTile" -> new Gson().fromJson(jsonObject, HousingTile.class);
            case "LaserTile" -> new Gson().fromJson(jsonObject, LaserTile.class);
            case "PropulsorTile" -> new Gson().fromJson(jsonObject, PropulsorTile.class);
            case "ShieldTile" -> new Gson().fromJson(jsonObject, ShieldTile.class);
            case "StorageTile" -> new Gson().fromJson(jsonObject, StorageTile.class);
            default -> throw new JsonParseException("Tipo sconosciuto: " + type);
        };
    }
}
