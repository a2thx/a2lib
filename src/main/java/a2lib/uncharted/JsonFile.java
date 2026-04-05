package a2lib.uncharted;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;

public class JsonFile<T> {
    private final File file;
    private final Gson gson;
    private final Type type;
    private T data;

    public JsonFile(File folder, String fileName, Type type, T defaultData) {
        this.file = new File(folder, fileName.endsWith(".json") ? fileName : fileName + ".json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.type = type;
        this.data = defaultData;

        load();
    }

    public void load() {
        if (!file.exists()) {
            save();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            T loadedData = gson.fromJson(reader, type);
            if (loadedData != null) {
                this.data = loadedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
