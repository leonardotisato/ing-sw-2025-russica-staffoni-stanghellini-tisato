package it.polimi.ingsw.cg04.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.cg04.model.tiles.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TileLoader {

    public static Map<Integer, Tile> loadTilesFromJson(String jsonFilePath, List<Integer> faceDownTiles) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        try (
                InputStream is = TileLoader.class.getClassLoader().getResourceAsStream(jsonFilePath);
                Reader reader = is != null ? new InputStreamReader(is) : null
        ) {
            if (reader == null) {
                throw new RuntimeException("Resource not found: " + jsonFilePath);
            }

            Type mapType = new TypeToken<Map<String, JsonObject>>() {}.getType();
            Map<String, JsonObject> tempMap = gson.fromJson(reader, mapType);

            Map<Integer, Tile> tileMap = new HashMap<>();
            for (Map.Entry<String, JsonObject> entry : tempMap.entrySet()) {
                int id = Integer.parseInt(entry.getKey());

                JsonObject tileJson = entry.getValue();
                tileJson.addProperty("id", id);

                Tile card = createTileFromJson(tileJson);
                tileMap.put(id, card);
                faceDownTiles.add(id);
            }
            Collections.shuffle(faceDownTiles, new Random());
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
            case "StructuralTile" -> new Gson().fromJson(jsonObject, StructuralTile.class);
            default -> throw new JsonParseException("Tipo sconosciuto: " + type);
        };
    }
}
