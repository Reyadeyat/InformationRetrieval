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

import java.util.ArrayList;

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
public class Scanner /*Lexical analysis*/ {
    ArrayList<Token> tokens;
    Integer cursor;
    
    private Integer lexerType;
    
    static final public Integer INFORMIX_MYSQL = 1;
    static final public Integer MYSQL_INFORMIX = 2;
    
    //Natural Languages Scanner
    private String symbol = "+-*<>/,.='()\r\n\t ";
    private String ignore = "\r\n\t ";
    private Character sc = '\'';// '\'' SQL '\"' programming
    private Character esc = '\'';// '\'\'' SQL '\"' programing '\"\"' JSON
    private Character escr = null;
    private Integer z;
    private String sm0, sm, sm1;
    private String formula;
    
    
    /*Make Semantics of Syntax like
            Key_word Class Keyword Parent_Class
            class Apple extends Fruit*/
    
    public Scanner(String expression, Integer lexerType) throws Exception {
        tokens = new ArrayList<>();
        
        this.lexerType = lexerType;
        
        cursor = 0;
        //tokens.add(Token.BOF);
        
        StringBuilder b = new StringBuilder();
        z = 0;
        formula = "";
        sm0 = sm = sm1 = "";
        boolean s = false, p = false;
        char c, pc, nc;
        c = pc = nc = '\0';
        int l = expression.length();
        for (int x = 0; x < l; x++) {
            c = expression.charAt(x);
            nc = x == l - 1 ? '\0' : expression.charAt(x + 1);
            if (s == false && (symbol.indexOf(c) > -1)) {
                if (b.length() > 0) {
                    tokens.add(Token.tokenizeLexeme(b.toString()));
                    b.delete(0, b.length());
                }
                if (ignore.indexOf(c) > -1) {
                    b.setLength(0);
                    continue;
                } else if (s == false && c == sc) {
                    /*ignore add open strnig flag*/
                    /*tokens.add(Token.tokenize(c));*/
                    s = true;
                } else {
                    tokens.add(Token.tokenizeSymbol(c));
                }
            } else if (s == true && c == esc && nc == sc) {
                if (escr == null) {
                    b.append(esc).append(sc);
                } else {
                    b.append(escr);
                }
                x++;
            } else if (s == true
                    && c == sc) {
                if (b.length() > 0) {
                    tokens.add(Token.tokenizeString(b.toString()));
                    b.setLength(0);
                }
                /*ignore add close strnig flag*/
                /*tokens.add(Token.tokenize(c));*/
                s = false;
            } else {
                b.append(c);
            }
            pc = c;
        }
        if (b.length() > 0) {
            //mm.add(b.toString());
            tokens.add(Token.tokenizeLexeme(b.toString()));
            b.delete(0, b.length());
        }
        b.setLength(0);
    }
    
    public void first() {
        cursor = -1;
    }
    
    public Token next() {
        /*if (sm1 == null) {
            sm0 = sm = sm1;
            return false;
        }

        sm0 = sm;
        sm = mm.get(z);
        sm1 = z + 1 >= mm.size() ? null : mm.get(++z);
        return true;*/
        
        if (tokens.size() <= cursor) {
            cursor = tokens.size();
            return null;
        }
        return tokens.get(++cursor);
    }
    
    public Token previous() {
        cursor--;
        if (cursor < 0) {
            cursor = -1;
            return null;
        }
        return tokens.get(--cursor);
    }
    
    public boolean last() {
        cursor = tokens.size();
        return true;
    }
    
    public void printTokens() {
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
    }
    
    public Integer getType() {
        return lexerType;
    }
    
    void validateSM(String ss) throws Exception {
        if (sm.equalsIgnoreCase(ss) == false) {
            throw new Exception("error found '" + sm + "' expecting '" + ss + "' in formula '" + formula + "'");
        }
    }

    void startFormula() {
        formula = formula + "." + sm;
        //System.out.println("Start Formula '" + formula + "'");
    }

    void endFormula() {
        //System.out.println("End Formula '" + formula + "'");
        formula = formula.substring(0, formula.lastIndexOf("."));
    }

    void unknown() throws Exception {
        throw new Exception("unknown '" + sm + "' in '" + formula + "'");
    }
}
