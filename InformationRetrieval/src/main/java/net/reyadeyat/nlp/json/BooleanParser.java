package net.reyadeyat.nlp.json;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class BooleanParser {
    static public Boolean parse(String booleanString) throws Exception {
        if ("on,1,true,yes".indexOf(booleanString.toLowerCase()) > -1) {
            return true;
        } else if ("off,0,false,no".indexOf(booleanString.toLowerCase()) > -1) {
            return false;
        }
        throw new Exception("Boolean string '" + booleanString + "' is invalid use {0,1,true,false,yes,no,on,off}");
    }
}
