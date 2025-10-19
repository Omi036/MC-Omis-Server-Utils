package omi.serverutils.osutils.modules;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerDataModule {
    public static final Gson GSON = new Gson();

    public static File saveJson(String pathname, JsonObject object) {
        File file = new File(pathname);

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error saving to json: " + file.getAbsolutePath(), e);
        }

        return file;
    }

    public static File saveJson(File file, JsonObject object){
        return saveJson(file.getAbsolutePath(), object);
    }


    public static JsonObject getJson(String pathname) {
        File file = ensureData(pathname);

        try (FileReader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();

        } catch (IOException e) {
            throw new RuntimeException("Error reading json file: " + file.getAbsolutePath(), e);
        }
    }

    public static JsonObject getJson(File file){
        return getJson(file.getAbsolutePath());
    }


    // Returns a file that is assured that exists
    public static File ensureData(String pathname){
        File datafile = new File(pathname);

        if(!datafile.exists()) saveJson(pathname, new JsonObject());

        return datafile;
    }

    public static File ensureData(File file){
        if(!file.exists()) saveJson(file.getAbsolutePath(), new JsonObject());
        return file;
    }
}
