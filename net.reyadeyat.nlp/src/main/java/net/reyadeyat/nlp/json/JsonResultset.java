/*
 * Copyright (C) 2023 Reyadeyat
 *
 * Reyadeyat/NLP is licensed under the
 * BSD 3-Clause "New" or "Revised" License
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://reyadeyat.net/NLP.LICENSE  
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.reyadeyat.nlp.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * Description
 * 
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2023.01.01
 */
public class JsonResultset {
 
    static public JsonObject resultsetJson(String[] field_list, ResultSet resultset, JsonObject json) throws Exception {
        JsonArray json_resultset = resultset(resultset);
        if (json == null) {
            json = new JsonObject();
        }
        JsonElement[] fields_group = new JsonElement[field_list.length];
        JsonElement[] fields_group_parent = new JsonElement[field_list.length];
        for (int c = 0; c < fields_group_parent.length; c++) {
            fields_group[c] = new JsonObject();
        }
        JsonArray leaf = null;
        for (int c = 0; c < json_resultset.size(); c++) {
            JsonObject record = json_resultset.get(c).getAsJsonObject();
            for (int f = 0; f < field_list.length; f++) {
                if (record.get(field_list[f]).equals(fields_group[f]) == false) {
                    fields_group[f] = record.get(field_list[f]);
                    if (f < field_list.length-1) {
                        fields_group_parent[f] = new JsonObject();
                    } else {
                        leaf = new JsonArray();
                        fields_group_parent[f] = leaf;
                    }
                    if (f > 0) {
                        ((JsonObject)fields_group_parent[f-1]).add(record.get(field_list[f]).getAsString(), fields_group_parent[f]);
                    } else if (f == 0) {
                        json.add(record.get(field_list[f]).getAsString(), fields_group_parent[f]);
                    }
                }
            }
            leaf.add(record);
        }
        
        return json;
    }
    
    static public <T> void resultset(ResultSet resultset, Class<T> java_class, ArrayList<T> t_list) throws Exception {
        JsonArray json_resultset = resultset(resultset);
        Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        for (int c = 0; c < json_resultset.size(); c++) {
            T record = gson.fromJson(json_resultset.get(c), java_class);
            t_list.add(record);
        }
    }
    
    static public <T> ArrayList<T> resultset(ResultSet resultset, Class<T> java_class) throws Exception {
        JsonArray json_resultset = resultset(resultset);
        ArrayList<T> t = new ArrayList<T>(json_resultset.size());
        Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
        for (int c = 0; c < json_resultset.size(); c++) {
            T record = gson.fromJson(json_resultset.get(c), java_class);
            t.add(record);
        }
        return t;
    }
    
    static public <K, V> Map<K, V> resultsetMap(Map<K, V> map, String key, ResultSet resultset, Class<V> java_class) throws Exception {
        Gson gson = JsonUtil.gson();
        try {
            ResultSetMetaData resultset_metadata = resultset.getMetaData();
            while (resultset.next()) {
                JsonObject record = new JsonObject();
                @SuppressWarnings("unchecked")
                K key_value = (K) resultset.getObject(key);
                for (int c = 1; c <= resultset_metadata.getColumnCount(); c++) {
                    int field_type = resultset_metadata.getColumnType(c);
                    String field_label= resultset_metadata.getColumnLabel(c);
                    Object field_value = resultset.getObject(c);
                    addRecordField(field_type, field_label, field_value, record);
                    
                }
                V java_object = JsonUtil.jsonElementToObject(record, java_class);
                map.put(key_value, java_object);
            }
            JsonUtil.reclaimGson(gson);
        } catch (Exception ex) {
            JsonUtil.reclaimGson(gson);
            throw ex;
        }
        return map;
    }
    
    static public JsonArray resultset(ResultSet resultset) throws Exception {
        JsonArray json_resultset = new JsonArray();
        ResultSetMetaData resultset_metadata = resultset.getMetaData();
        int count = resultset_metadata.getColumnCount();
        while (resultset.next()) {
            JsonObject record = new JsonObject();
            json_resultset.add(record);
            for (int c = 1; c <= count; c++) {
                int field_type = resultset_metadata.getColumnType(c);
                String field_label= resultset_metadata.getColumnLabel(c);
                Object field_value = resultset.getObject(c);
                addRecordField(field_type, field_label, field_value, record);
            }
        }
        return json_resultset;
    }
    
    static public void addRecordField(int field_type, String field_label, Object field_value, JsonObject record) throws Exception {
        if (field_value == null) {
            record.add(field_label, JsonNull.INSTANCE);
            return;
        }
        switch (field_type) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
                record.addProperty(field_label, (String) field_value);
                break;
            case Types.TINYINT:
            case Types.BOOLEAN:
            case Types.BIT:
                record.addProperty(field_label, BooleanParser.parse(field_value.toString()));
                break;
            case Types.INTEGER:
            case Types.DECIMAL:
            case Types.SMALLINT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.FLOAT:
                record.addProperty(field_label, (Number) field_value);
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
            case Types.TIME_WITH_TIMEZONE:
                record.addProperty(field_label, /*(String)*/ field_value.toString());
                break;
            default:
                throw new Exception("Ubndefined SQL field field_label '"+field_label+"' java.sql.Types '"+field_type+"'");
        }
    }
}
