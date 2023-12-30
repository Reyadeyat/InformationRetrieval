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
public class Token {

    final static public Integer CLAUSE = 0;
    final static public Integer KEYWORD = 1;
    final static public Integer FUNCTION = 2;
    final static public Integer MOPERATOR = 3;
    final static public Integer COPERATOR = 4;
    final static public Integer SYMBOL = 5;
    final static public Integer OSYMBOL = 6;
    final static public Integer CSYMBOL = 7;
    final static public Integer USYMBOL = 8;//Unary
    final static public Integer BSYMBOL = 9;//Binary
    final static public Integer TSYMBOL = 10;//Ternary
    final static public Integer SSYMBOL = 11;
    final static public Integer STRING = 12;
    final static public Integer LITERAL = 13;
    final static public Integer NUMBER = 14;
    
    //Symbols
    final static public Token SPACE = new Token(" ", USYMBOL, "SPACE");
    final static public Token NOT_EQUAL = new Token("<>", BSYMBOL, "NOT_EQUAL");
    final static public Token GREATE = new Token(">", BSYMBOL, "GREATE");
    final static public Token LESS = new Token("<", BSYMBOL, "LESS");
    final static public Token BETWEEN = new Token("BETWEEN", TSYMBOL, "BETWEEN");
    final static public Token LIKE = new Token("LIKE", BSYMBOL, "LIKE");
    final static public Token IN = new Token("IN", BSYMBOL, "IN");
    final static public Token AND = new Token("AND", BSYMBOL, "AND");
    final static public Token OR = new Token("OR", BSYMBOL, "OR");
    final static public Token EQUAL = new Token("=", BSYMBOL, "EQUAL");
    final static public Token PLUS = new Token("+", BSYMBOL, "PLUS");
    final static public Token MINUS = new Token("-", BSYMBOL, "MINUS");
    final static public Token ASTRISK = new Token("*", BSYMBOL, "ASTRISK");
    final static public Token DIVISION = new Token("/", BSYMBOL, "DIVISION");
    final static public Token OPEN_PARENTHESES = new Token("(", OSYMBOL, "OPEN_PARENTHESES");
    final static public Token CLOSE_PARENTHESES = new Token(")", CSYMBOL, "CLOSE_PARENTHESES");
    final static public Token OPEN_BRACE = new Token("{", OSYMBOL, "OPEN_BRACE");
    final static public Token CLOSE_BRACE = new Token("}", CSYMBOL, "CLOSE_BRACE");
    final static public Token OPEN_BRACKETS = new Token("[", OSYMBOL, "OPEN_BRACKETS");
    final static public Token CLOSE_BRACKETS = new Token("]", CSYMBOL, "CLOSE_BRACKETS");
    final static public Token COLON = new Token(":", BSYMBOL, "COLON");
    final static public Token COMMA = new Token(",", USYMBOL, "COMMA");
    final static public Token SEMICOLON = new Token(";", USYMBOL, "SEMICOLON");
    final static public Token SINGLE_QUOTE = new Token("'", SSYMBOL, "SINGLE_QUOTE");
    final static public Token DOUBLE_QUOTES = new Token("\"", SSYMBOL, "DOUBLE_QUOTES");
    final static public Token OPEN_SINGLE_QUOTE = new Token("'", SSYMBOL, "OPEN_SINGLE_QUOTE");
    final static public Token OPEN_DOUBLE_QUOTES = new Token("\"", SSYMBOL, "OPEN_DOUBLE_QUOTES");
    final static public Token CLOSE_SINGLE_QUOTE = new Token("'", SSYMBOL, "CLOSE_SINGLE_QUOTE");
    final static public Token CLOSE_DOUBLE_QUOTES = new Token("\"", SSYMBOL, "CLOSE_DOUBLE_QUOTES");
    final static public Token ACCENT = new Token("`", SSYMBOL, "ACCENT");
    final static public Token DOT = new Token(".", BSYMBOL, "DOT");

    //final static public Token BOF = new Token("BOF", SYMBOL, "BOF");
    //final static public Token EOF = new Token("EOF", SYMBOL, "EOF");

    final static public HashMap<Integer, String> typeMap = new HashMap<>();
    final static public ArrayList<String> clauses = new ArrayList<>();
    final static public ArrayList<String> keywords = new ArrayList<>();
    final static public ArrayList<String> functions = new ArrayList<>();
    final static public ArrayList<String> moperators = new ArrayList<>();
    final static public ArrayList<String> coperators = new ArrayList<>();

    private String literal;
    private Integer type;
    private String name;

    private Token(String literal, Integer type, String name) {
        this.literal = literal;
        this.type = type;
        this.name = name;
    }

    static public void init() {
        if (typeMap != null) {
            typeMap.put(CLAUSE, "CLAUSE");
            typeMap.put(KEYWORD, "KEYWORD");
            typeMap.put(FUNCTION, "FUNCTION");
            typeMap.put(MOPERATOR, "MOPERATOR");
            typeMap.put(COPERATOR, "COPERATOR");
            typeMap.put(SYMBOL, "SYMBOL");
            typeMap.put(OSYMBOL, "OSYMBOL");
            typeMap.put(CSYMBOL, "CSYMBOL");
            typeMap.put(USYMBOL, "USYMBOL");
            typeMap.put(BSYMBOL, "BSYMBOL");
            typeMap.put(TSYMBOL, "TSYMBOL");
            typeMap.put(SSYMBOL, "SSYMBOL");
            typeMap.put(STRING, "STRING");
            typeMap.put(LITERAL, "LITERAL");
            typeMap.put(NUMBER, "NUMBER");
        }
    }
    
    public static Token tokenize(char symbol) {
        return tokenizeSymbol(symbol);
    }
    
    static public void addClause(String keyword) {
        keywords.add(keyword);
    }

    static public void addKeyword(String keyword) {
        keywords.add(keyword);
    }
    
    static public void addFunction(String function) {
        functions.add(function);
    }
    
    static public void addCOperator(String coperator) {
        coperators.add(coperator);
    }
    
    static public void addMOperator(String moperator) {
        moperators.add(moperator);
    }
    
    static public void addClauses(String[] clauses) {
        for (String clause : clauses) {
            addClause(clause.toUpperCase());
        }
    }

    static public void addKeywords(String[] keywords) {
        for (String keyword : keywords) {
            addKeyword(keyword.toUpperCase());
        }
    }
    
    static public void addFunctions(String[] functions) {
        for (String function : functions) {
            addFunction(function.toUpperCase());
        }
    }
    
    static public void addCOperators(String[] coperators) {
        for (String coperator : coperators) {
            addCOperator(coperator.toUpperCase());
        }
    }
    
    static public void addMOperators(String[] moperators) {
        for (String moperator : moperators) {
            addMOperator(moperator.toUpperCase());
        }
    }

    public static Token tokenizeSymbol(char symbol) {
        switch (symbol) {
            //case ' ' : return SPACE;
            case '>':
                return GREATE;
            case '<':
                return LESS;
            case '=':
                return EQUAL;
            case '+':
                return PLUS;
            case '-':
                return MINUS;
            case '*':
                return ASTRISK;
            case '/':
                return DIVISION;
            case ':':
                return COLON;
            case ',':
                return COMMA;
            case ';':
                return SEMICOLON;
            case '\'':
                return SINGLE_QUOTE;
            case '\"':
                return DOUBLE_QUOTES;
            case '`':
                return ACCENT;
            case '.':
                return DOT;
            case '(':
                return OPEN_PARENTHESES;
            case ')':
                return CLOSE_PARENTHESES;
            case '{':
                return OPEN_BRACE;
            case '}':
                return CLOSE_BRACE;
        }

        return null;
    }
    
    static public Token getsSymbol(char s, boolean open) throws Exception {
        if (s == '\'') {
            return (open == true ? OPEN_SINGLE_QUOTE : CLOSE_SINGLE_QUOTE);
        } else if (s == '\"') {
            return (open == true ? OPEN_DOUBLE_QUOTES : CLOSE_DOUBLE_QUOTES);
        }
        throw new Exception("either \' or \" allowed");
    }

    public static Token tokenizeLexeme(String token) {

        String ttoken = token.toUpperCase();
        if (keywords.contains(ttoken)) {
            return new Token(token, KEYWORD, "KEYWORD");
        } else if (functions.contains(ttoken)) {
            return new Token(token, FUNCTION, "FUNCTION");
        } else if (coperators.contains(ttoken)) {
            return new Token(ttoken, COPERATOR, "COPERATOR");
        } else if (moperators.contains(ttoken)) {
            return new Token(ttoken, MOPERATOR, "MOPERATOR");
        }

        boolean isNumber = false;
        char[] token_buffer = token.toCharArray();
        for (int i = token_buffer.length - 1; i >= 0; i--) {
            if (token_buffer[i] < '0' || token_buffer[i] > '9') {
                isNumber = false;
                break;
            }
        }

        if (isNumber == true) {
            return new Token(token, NUMBER, "NUMBER");
        }

        return new Token(token, LITERAL, "LITERAL");
    }

    public static Token tokenizeString(String token) {
        return new Token(token, STRING, "STRING");
    }

    void changeType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return this.literal;
    }
    
    public Boolean isClause() {
        return type == CLAUSE;
    }

    public Boolean isKeyword() {
        return type == KEYWORD;
    }
    
    public Boolean isFunction() {
        return type == FUNCTION;
    }
    
    public Boolean isMOperator() {
        return type == MOPERATOR;
    }
    
    public Boolean isCOperator() {
        return type == COPERATOR;
    }

    public Boolean isSymbol() {
        return (type == SYMBOL
                || type == BSYMBOL || type == TSYMBOL
                || type == OSYMBOL || type == CSYMBOL
                || type == USYMBOL || type == BSYMBOL
                || type == TSYMBOL || type == SSYMBOL);
    }
    
    public Boolean isnSymbol() {
        return (type == USYMBOL
                || type == BSYMBOL || type == TSYMBOL);
    }
    
    public Boolean isuSymbol() {
        return (type == USYMBOL);
    }
    
    public Boolean isbSymbol() {
        return (type == BSYMBOL);
    }
    
    public Boolean istSymbol() {
        return (type == TSYMBOL);
    }
    
    public Boolean isoSymbol() {
        return (type == OSYMBOL);
    }
    
    public Boolean iscSymbol() {
        return (type == CSYMBOL);
    }
    
    public Boolean issSymbol() {
        return (type == SSYMBOL);
    }

    @Override
    public String toString() {
        return getType(this.type) + " => " + this.literal + " => " + this.name;
    }

    static public String getType(Integer type) {
        String string = typeMap.get(type);
        
        return (string == null ? "UNDEFINED" : string);
    }

    static public boolean isSpace(char chr) {
        if (chr == ' ' || chr == '\t' || chr == '\r' || chr == '\n') {
            return true;
        }
        return false;
    }
}
