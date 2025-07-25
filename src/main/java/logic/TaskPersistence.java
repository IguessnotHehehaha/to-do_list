package logic;

import java.util.List;
import java.time.LocalDate;
import java.lang.reflect.Type;
import java.io.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Task;


public class TaskPersistence {
    private static final String FILE_PATH = "tasks.json";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();

    public static void save(List<Task> tasks) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(tasks, writer);
        }
    }
    public static  List<Task> load() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists()) {
            return null;
        }
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Task>>(){}.getType();
            return gson.fromJson(reader, listType);
        }
    }

}
