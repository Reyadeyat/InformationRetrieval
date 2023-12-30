/*
 * Copyright (C) 2023 Reyadeyat
 *
 * Reyadeyat/FLP is licensed under the
 * BSD 3-Clause "New" or "Revised" License
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://reyadeyat.net/FLP.LICENSE 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.reyadeyat.flp.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
public class TextParser {

    final static public ArrayList<String> kw = new ArrayList<>(Arrays.asList(new String[]{
        "datawindow", "units", "timer_interval", "processing", 
        "print", "orientation", "margin", "left", "right", "top", "bottom", 
        "paper", "size", "paper", "source", "selected", "mouse", "no", "yes",
        "header", "summary", "footer", "detail", 
        "table", "column", "type", "char", "updatewhereclause", "dbname",
        "release", "group", "groupbox", "text", "name", "tag", "visible", "moveable",
        "resizeable", "band", "compute", "strikethrough", "format",
        "expression", "date", "today", "alignment", "border",
        "html", "valueishtml", "crosstab", "repeat",
        "font", "charset", "face", "family",
        "height", "width", "italic", "pitch", "strikethrough", "weight", "underline",
        "background", "mode", "color", "alignment", "border", "x", "y"
    }));
    final static public String symbol = ".=()/*-+[]|<>,;:\\'\"";
    final static public HashMap<String, String> imtf = new HashMap() {
        {
            put("TO_CHAR", "DATE_FORMAT");
            put("TO_DATE", "DATE_FORMAT");
            put("NVL", "IFNULL");
        }
    };
    
    final static public HashMap<String, String> imtkw = new HashMap() {
        {
            put("UNIQUE", "DISTINCT");
            put("TRUNC", "TRUNCATE");
            put("INTEGER", "SIGNED");
            put("NUMERIC", "SIGNED INTEGER");
            put("TEMP", "TEMPORARY");
        }
    };

    final static public StringBuffer informixt_to_mysql_parser(String ii) throws Exception {
        StringBuffer fisk, ms = new StringBuffer(), b = new StringBuffer();
        ArrayList<String> mm = new ArrayList<>();
        boolean s = false, p = false;
        char c, pc, sc;
        c = pc = sc = '\0';
        int l = ii.length();
        for (int i = 0; i < l; i++) {
            
            if (b.toString().equalsIgnoreCase("charset")) {
                i = i;
            }
            
            c = ii.charAt(i);
            if (c == '\'') {
                c = c;
            }
            if (s == false && (symbol.indexOf(c) > -1 || c == ' ')) {
                if (b.length() > 0) {
                    mm.add(b.toString());
                    b.delete(0, b.length());
                }
                if (symbol.indexOf(c) > -1 && (c == '\"' || c == '\'')) {
                    if (b.length() > 0) {
                        mm.add(b.toString());
                        b.delete(0, b.length());
                    }
                    s = true;
                    sc = c;
                    mm.add(String.valueOf(c));
                } else if (symbol.indexOf(c) > -1) {
                    mm.add(String.valueOf(c));
                }
            } else if (s == true
                    && c == sc && pc != '\\' /*&& pc != sc*/
                    && (i == l - 1 ? '\0' : ii.charAt(i + 1)) != sc) {
                if (b.length() > 0) {
                    mm.add(b.toString());
                    b.delete(0, b.length());
                }
                s = false;
                sc = '\0';
                mm.add(String.valueOf(c));
            } else {
                b.append(c);
            }
            pc = c;
        }
        if (b.length() > 0) {
            mm.add(b.toString());
            b.delete(0, b.length());
        }
        
        int ml = mm.size();
        String sm0 = "", sm = "", sm1 = mm.get(0);
        fisk = b.append(" ");
        for (int i = 0; i < ml; i++) {
            System.out.println(mm.get(i));
        }

        /*int ml = mm.size();
        String sm0 = "", sm = "", sm1 = mm.get(0);
        fisk = b.append(" ");
        for (int i = 0; i < ml; i++) {
            sm = sm1;
            sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
            if (sm.equalsIgnoreCase("extend") == true) {
                ++i;
                ms.append(mm.get(++i));
                while (mm.get(++i).equalsIgnoreCase(")") == false);
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
            } else if (imtkw.containsKey(sm.toUpperCase()) == true) {
                ms.append(imtkw.get(sm.toUpperCase())).append("(,".indexOf(sm) > -1 ? "" : " ");
            } else if (imtf.containsKey(sm.toUpperCase()) == true && sm1.equalsIgnoreCase("(")) {
                ms.append(imtf.get(sm.toUpperCase()));
            } else if (sm.equalsIgnoreCase("cast") == true) {
                if (p = sm1.equalsIgnoreCase("(")) ++i;
                    ms.append(sm).append(p ? "(" : " ").append(mm.get(++i))
                        .append(" ").append(mm.get(++i)).append(" ");
                sm = mm.get(++i).toUpperCase();
                if (imtkw.containsKey(sm) == true) {
                    ms.append(imtkw.get(sm));
                } else if (sm.equalsIgnoreCase("DATETIME") == true) {
                    ms.append("DATETIME) ");
                    while (p && mm.get(++i).equalsIgnoreCase(")") == false);
                } else {
                    throw new Exception("CAST AS '" + sm + "' is not defined");
                }
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
            } else if (sm1.equalsIgnoreCase("[") == true) {
                ++i;
                int f = Integer.parseInt(mm.get(++i).toString());
                ++i;
                int t = Integer.parseInt(mm.get(++i).toString()) - f;
                ms.append("SUBSTRING(").append(sm).append(", ").append(f).append(", ").append(t).append(")");
                ++i;
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
            } else if (sm.equalsIgnoreCase(":") == true && sm1.equalsIgnoreCase(":") == true) {
                ms.delete(ms.length() - sm0.length() - 1, ms.length());
                ++i;
                sm = mm.get(++i);
                ms.append("CAST(").append(sm0).append(" AS ")
                        .append(imtkw.get(sm.toUpperCase())).append(")");
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
            } else if (sm.equalsIgnoreCase("skip") == true) {
                fisk.append("LIMIT ").append(Integer.parseInt(mm.get(i+3).toString())).append(" ");
                fisk.append("OFFSET ").append(Integer.parseInt(mm.get(i+1).toString()));
                i+=3;
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
                sm="";
            } else if (sm.equalsIgnoreCase("first") == true) {
                fisk.append("LIMIT ").append(Integer.parseInt(mm.get(++i).toString())).append(" ");
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
                sm="";
            } else if (sm.equalsIgnoreCase("|") == true && sm1.equalsIgnoreCase("|")) {
                ms.insert(ms.length() - sm0.length() - 1, "CONCAT(");
                ms.append(",");
                i++;
                while ((sm = mm.get(++i)).equalsIgnoreCase(",") == false) {
                    if (sm.equalsIgnoreCase("|") == true) {
                        i++;
                        ms.append(",");
                    } else {
                        ms.append(sm);
                    }
                }
                ms.append("),");
                sm1 = (i + 1 == ml ? "" : mm.get(i + 1));
            } else {
                ms.append(sm).append("'(.".indexOf(sm) > -1 || "'.,()".indexOf(sm1) > -1 ? "" : "<>".indexOf(sm) > -1 && ">=".indexOf(sm1) > -1 ? "" : " ");
            }
            sm0 = sm;
        }*/

        return ms.append(fisk);
    }

    public static void main(String[] args) throws Exception {

        String fileName;
        File file;
        FileInputStream fis;
        byte[] data;

        fileName = "/linux/sql_statment.sql";
        file = new File(fileName);
        fis = new FileInputStream(file);
        data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String informix_sql = "SQL STATEMENT";

        System.out.println("Informix SQL Statement");
        System.out.println("------------------------------------------------------------------------------");
        //System.out.println(informix_sql);
        System.out.println("------------------------------------------------------------------------------");
        long t1, t2;
        System.out.println("Parsing");
        System.out.println("------------------------------------------------------------------------------");
        t1 = System.nanoTime();
        System.out.println(TextParser.informixt_to_mysql_parser(informix_sql.replace("\n", " ")).toString());
        t2 = System.nanoTime();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Execution Time = " + ((t2 - t1)/1000000d) + " milliseconds");
    }

}