package net.reyadeyat.nlp.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
public class JsonArrayAdapter extends TypeAdapter<JsonArray> {

    @Override
    public JsonArray read(JsonReader json_reader) throws IOException {
        Gson gson = JsonUtil.gson();
        try {
            JsonArray json_array = gson.fromJson(json_reader.nextString(), JsonArray.class);
            JsonUtil.reclaimGson(gson);
            return json_array;
        } catch (Exception ex) {
            JsonUtil.reclaimGson(gson);
            throw ex;
        }
    }

    @Override
    public void write(JsonWriter json_writer, JsonArray json_array) throws IOException {
        Gson gson = JsonUtil.gson();
        try {
            gson.toJson(json_array, json_writer);
            JsonUtil.reclaimGson(gson);
        } catch (Exception ex) {
            JsonUtil.reclaimGson(gson);
            throw ex;
        }
    }
}
