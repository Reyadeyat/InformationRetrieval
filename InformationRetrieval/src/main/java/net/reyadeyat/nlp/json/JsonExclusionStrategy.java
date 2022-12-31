
package net.reyadeyat.nlp.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class JsonExclusionStrategy implements ExclusionStrategy {
    private transient ArrayList<String> expose_field_list;
    
    public JsonExclusionStrategy (ArrayList<String> expose_field_list) {
        this.expose_field_list = expose_field_list;
    }
    
    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        //if (fa.getDeclaringClass().equals(Citation.class) && expose_field_list.contains(fa.getName())) {
        if (expose_field_list.contains(fa.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
}
