package logic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.lang.reflect.Type;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSource, JsonSerializationContext context){
        return new JsonPrimitive(date.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString());
    }
}
