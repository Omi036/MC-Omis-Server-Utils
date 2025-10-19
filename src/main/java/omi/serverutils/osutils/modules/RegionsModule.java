package omi.serverutils.osutils.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import omi.serverutils.osutils.OSUtils;

import javax.json.Json;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegionsModule {
    public static HashMap<String, JsonArray> dimensionRegions = new HashMap<>();
    public static final File REGIONS_FILE = new File(OSUtils.dataDirectory, "regions.json");

    // Loads all the regions inside the data file to the static hashmap
    public static void loadRegions(){
        dimensionRegions.clear();
        dimensionRegions = Store.getRegions();
    }


    public static Region getRegionByName(String name, String dimension) {
        JsonArray regionsInDimension = getRegionsInDimension(dimension);
        Region returnableRegion = null;

        for(JsonElement regionAsJson: regionsInDimension.getAsJsonArray()){
            Region parsedRegion = Region.fromJson((JsonObject) regionAsJson);

            if(parsedRegion.name.equals(name)){
                returnableRegion = parsedRegion;
                break;
            }
        }

        return returnableRegion;
    }

    public static boolean dimensionIsRegistered(String dimension) {
        return dimensionRegions.containsKey(dimension);
    }

    public static ArrayList<Region> getAllRegions(){
        ArrayList<Region> returnable = new ArrayList<>();

        for(String dimensionKey : dimensionRegions.keySet()){
            for(JsonElement region : dimensionRegions.get(dimensionKey)){
                returnable.add(Region.fromJson((JsonObject) region));
            }
        }

        return returnable;
    }

    public static ArrayList<Region> getRegionsInDimensionAsArray(String dimension){
        ArrayList<Region> returnable = new ArrayList<>();

            for(JsonElement region : dimensionRegions.get(dimension)){
                returnable.add(Region.fromJson((JsonObject) region));
            }

        return returnable;
    }


    public static JsonArray getRegionsInDimension(String dimension){
        JsonArray regionsInDimension = dimensionRegions.get(dimension);
        return regionsInDimension == null ? new JsonArray() : regionsInDimension;
    }

    public static ArrayList<String> getRegionsNamesInDimension(String dimension){
        JsonArray regionsInDimension = getRegionsInDimension(dimension);
        ArrayList<String> returnable = new ArrayList<>();

        if(regionsInDimension.isEmpty()) return returnable;

        for(JsonElement regionAsJson: regionsInDimension.getAsJsonArray()){
            Region parsedRegion = Region.fromJson((JsonObject) regionAsJson);
            returnable.add(parsedRegion.name);
        }

        return returnable;
    }


    public static class Store {
        // Gets every region in every dimension as a Map<Dimension, RegionList>
        public static HashMap<String, JsonArray> getRegions(){
            HashMap<String, JsonArray> returnable = new HashMap<>();

            // Make sure the file exists and its initialized
            ServerDataModule.ensureData(REGIONS_FILE);
            JsonObject regionsAsJson = ServerDataModule.getJson(REGIONS_FILE);

            // Add every regionList to the map
            regionsAsJson.keySet().forEach(dimensionKey -> {
                returnable.put(dimensionKey, regionsAsJson.getAsJsonArray(dimensionKey));
            });

            return returnable;
        }

        // Saves the regionMap to the file
        public static void saveRegions(HashMap<String, JsonArray> regions){
            JsonObject savable = new JsonObject();
            // Adds every JSONArray to the JsonObject to save
            regions.forEach(savable::add);

            // Saves it and reloads the regions
            ServerDataModule.saveJson(REGIONS_FILE, savable);
            loadRegions();
        }


        // Replaces a region given its dimension and name
        public static void replaceRegionAndSave(String regionName, String dimensionName, Region newRegion){
            HashMap<String, JsonArray> regions = getRegions();
            boolean regionFound = false;

            // Copy regions to new hashmap if they arent the one to replace
            for(String dimensionKey : regions.keySet()){
                // If region is already found, exit loop
                if(regionFound) break;

                OSUtils.LOGGER.debug(String.valueOf("Comprueba la dimension"));

                // If region isnt in this dimension return
                if(!dimensionKey.equals(dimensionName)) continue;

                OSUtils.LOGGER.debug(String.valueOf("Buscando en la dimension"));

                // If region is in dimension, save it to lookup later
                JsonArray dimensionRegions = regions.get(dimensionKey);

                // Iterate through every region in dimension to check which one to delete
                for(JsonElement regionAsJson : dimensionRegions){
                    JsonObject currentRegion = (JsonObject) regionAsJson;

                    OSUtils.LOGGER.debug(String.valueOf("Buscando region"));

                    // If it isnt the one return
                    OSUtils.LOGGER.debug(String.valueOf("Comprueba el nombre"));
                    OSUtils.LOGGER.debug(String.valueOf(currentRegion.get("name").getAsString()));
                    OSUtils.LOGGER.debug(String.valueOf(regionName));
                    if(!currentRegion.get("name").getAsString().equals(regionName)) continue;

                    // Remove current dimension and replace it with the new one
                    dimensionRegions.remove(regionAsJson);
                    dimensionRegions.add(newRegion.toJson());
                    regions.replace(dimensionKey, dimensionRegions);
                    regionFound = true;

                    OSUtils.LOGGER.debug(String.valueOf("Reemplazada region"));
                    break;
                }
            }

            OSUtils.LOGGER.debug(String.valueOf(regionFound));
            OSUtils.LOGGER.debug(String.valueOf("Llego"));

            // Save the new modified regions and reload
            saveRegions(regions);
            loadRegions();
        }

        // Add a new region to the list
        public static void uploadRegion(Region newRegion) {
            HashMap<String, JsonArray> regions = getRegions();
            String dimension = newRegion.dimension;

            // Get JsonArray of dimension
            JsonObject newRegionJson = newRegion.toJson();
            JsonArray regionsInDimension = regions.get(dimension) != null ? regions.get(dimension) : new JsonArray();
            regionsInDimension.add(newRegionJson);

            // Replace the JsonArray with the modified one
            if(regions.get(dimension) == null){
                regions.put(dimension, regionsInDimension);
            } else {
                regions.replace(dimension, regionsInDimension);
            }

            saveRegions(regions);
            loadRegions();
        }

        public static void removeRegion(String regionName, String dimensionName){
            HashMap<String, JsonArray> regions = getRegions();

            // Get JsonArray of dimension
            JsonArray regionsInDimension = regions.get(dimensionName);

            // Iterate through every region in dimension to check which one to delete
            for(JsonElement region : regionsInDimension){
                JsonObject currentRegion = (JsonObject) region;

                // If it isnt the one return
                if(!currentRegion.get("name").getAsString().equals(regionName)) return;

                regionsInDimension.remove(region);
                break;
            }

            // Replace the JsonArray with the modified one
            regions.replace(dimensionName, regionsInDimension);

            saveRegions(regions);
            loadRegions();
        }
    }


    public static class Region {
        public boolean enabled;
        public String name;
        public String dimension;
        public Vec3 startPos;
        public Vec3 endPos;
        public Map<String, Boolean> properties;
        public Boolean empty = false;
        public ArrayList<String> owners;

        public Region(String name, String dimension, Vec3 startPos, Vec3 endPos, Map<String, Boolean> properties, ArrayList<String> owners, boolean enabled){
            this.name = name;
            this.dimension = dimension;
            this.startPos = startPos;
            this.endPos = endPos;
            this.properties = properties;
            this.owners = owners;
            this.enabled = enabled;
        }

        public Region(){
            this.empty = true;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean userIsOwner(String username){
            return this.owners.contains(username);
        }

        public boolean isPositionInside(Vec3 position){
            // Get the minimum and maximum values of the region
            int minX = Math.min(  (int) startPos.x, (int) endPos.x  );
            int minY = Math.min(  (int) startPos.y, (int) endPos.y  );
            int minZ = Math.min(  (int) startPos.z, (int) endPos.z  );
            int maxX = Math.max(  (int) startPos.x, (int) endPos.x  );
            int maxY = Math.max(  (int) startPos.y, (int) endPos.y  );
            int maxZ = Math.max(  (int) startPos.z, (int) endPos.z  );

            return position.x >= minX && position.x <= maxX &&
                    position.y >= minY && position.y <= maxY &&
                    position.z >= minZ && position.z <= maxZ;
        }

        public JsonObject toJson(){
            JsonArray startPosArray = new JsonArray();
            startPosArray.add(startPos.x);
            startPosArray.add(startPos.y);
            startPosArray.add(startPos.z);

            JsonArray endPosArray = new JsonArray();
            endPosArray.add(endPos.x);
            endPosArray.add(endPos.y);
            endPosArray.add(endPos.z);

            JsonObject propertiesObject = new JsonObject();
            properties.keySet().forEach(key -> propertiesObject.addProperty(key, properties.get(key)));

            JsonArray ownersArray = ServerDataModule.GSON.toJsonTree(this.owners).getAsJsonArray();

            JsonObject object = new JsonObject();
            object.addProperty("enabled", this.enabled);
            object.addProperty("name", this.name);
            object.addProperty("dimension", this.dimension);
            object.add("startPos", startPosArray);
            object.add("endPos", endPosArray);
            object.add("properties", propertiesObject);
            object.add("owners", ownersArray);

            return object;
        }

        public JsonArray toJsonArray() {
            JsonArray array = new JsonArray();
            array.add(this.toJson());
            return array;
        }


        public static Region fromJson(JsonObject jsonRegion){
            if( !(jsonRegion.has("name")
                    && jsonRegion.has("enabled")
                    && jsonRegion.has("dimension")
                    && jsonRegion.has("startPos")
                    && jsonRegion.has("endPos")
                    && jsonRegion.has("properties")
                    && jsonRegion.has("owners"))) {

                throw new RuntimeException("Object is not a child of a Region or its missing properties");
            }


            // Gets the raw values from the json
            String name = jsonRegion.get("name").getAsString();
            boolean enabled = jsonRegion.get("enabled").getAsBoolean();
            String dimension = jsonRegion.get("dimension").getAsString();
            JsonArray startPos = jsonRegion.getAsJsonArray("startPos");
            JsonArray endPos = jsonRegion.getAsJsonArray("endPos");
            JsonObject properties = jsonRegion.getAsJsonObject("properties");
            JsonArray owners = jsonRegion.getAsJsonArray("owners");

            // Parses the raw values into the correct types
            Vec3 startPosVec = new Vec3(startPos.get(0).getAsInt(), startPos.get(1).getAsInt(), startPos.get(2).getAsInt());
            Vec3 endPosVec = new Vec3(endPos.get(0).getAsInt(), endPos.get(1).getAsInt(), endPos.get(2).getAsInt());
            Map<String, Boolean> propertiesObject = new HashMap<>();
            ArrayList<String> ownersArray = new ArrayList<>();

            owners.forEach(owner -> ownersArray.add(owner.getAsString()));
            properties.keySet().forEach(key -> propertiesObject.put(key, properties.get(key).getAsBoolean()));

            return new Region(name, dimension, startPosVec, endPosVec, propertiesObject, ownersArray, enabled);
        }
    }
}