package net.reyadeyat.nlp.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class JsonObjectAdapter extends TypeAdapter<JsonObject> {

    @Override
    public JsonObject read(JsonReader json_reader) throws IOException {
        Gson gson = JsonUtil.gson();
        try {
            JsonObject json_object = gson.fromJson(json_reader.nextString(), JsonObject.class);
            JsonUtil.reclaimGson(gson);
            return json_object;
        } catch (Exception ex) {
            JsonUtil.reclaimGson(gson);
            throw ex;
        }
    }

    @Override
    public void write(JsonWriter json_writer, JsonObject json_object) throws IOException {
        Gson gson = JsonUtil.gson();
        try {
            gson.toJson(json_object, json_writer);
            JsonUtil.reclaimGson(gson);
        } catch (Exception ex) {
            JsonUtil.reclaimGson(gson);
            throw ex;
        }
    }
}
