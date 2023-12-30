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

import net.reyadeyat.flp.compiler.Token;
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
public class Parser /*Syntactic analysis, Syntax Checking, Symbol Table*/ {
    
    static final private HashMap<String, String> mysql_informix_translator = new HashMap<>();
    static final private HashMap<String, String> informix_mysql_translator = new HashMap<>();
    
    private Scanner lexer;
    
    public Parser(Scanner lexer) {
        this.lexer = lexer;
        
        if (mysql_informix_translator != null) {
            mysql_informix_translator.put("DISTINCT", "UNIQUE");
            mysql_informix_translator.put("LIMIT", "SKIP");
            mysql_informix_translator.put("OFFSET", "FIRST");
            mysql_informix_translator.put("DATE_FORMAT", "TO_CHAR");
            mysql_informix_translator.put("TRUNCATE", "TRUNC");
            mysql_informix_translator.put("SIGNED", "INTEGER");
            mysql_informix_translator.put("ISNULL", "NVL");
        }

        if (informix_mysql_translator != null) {
            informix_mysql_translator.put("UNIQUE", "DISTINCT");
            informix_mysql_translator.put("SKIP", "LIMIT");
            informix_mysql_translator.put("FIRST", "OFFSET");
            informix_mysql_translator.put("TO_CHAR", "DATE_FORMAT");
            informix_mysql_translator.put("TO_DATE", "DATE_FORMAT");
            informix_mysql_translator.put("TRUNC", "TRUNCATE");
            informix_mysql_translator.put("INTEGER", "SIGNED");
            informix_mysql_translator.put("ISNULL", "NVL");
        }
        
        parse();
    }
    
    private void parse() {
        //Iterate Tokens
        Integer expression_id = 0;
        Expression expression = null;
        lexer.first();
        Token token = lexer.next();
        //Find Expressions Start Keywords
        if (token.isKeyword() && token.getToken().equalsIgnoreCase("SELECT") == true) {
            expression = new Expression(null, Expression.SELECT, 0, lexer, token);
        } else if (token.isKeyword() && token.getToken().equalsIgnoreCase("INSERT") == true) {
            expression = new Expression(null, Expression.INSERT, 0, lexer, token);
        } else if (token.isKeyword() && token.getToken().equalsIgnoreCase("UPDATE") == true) {
            expression = new Expression(null, Expression.UPDATE, 0, lexer, token);
        } else if (token.isKeyword() && token.getToken().equalsIgnoreCase("DELETE") == true) {
            expression = new Expression(null, Expression.DELETE, 0, lexer, token);
        }
            
            //Create Expressions
            //Translate Epressions
            //Arrange Expressions
        
        
        /*if (lexer.getType() == Scanner.INFORMIX_MYSQL) {
                ttoken = informix_mysql_translator.get(ttoken);
            } else if (lexer.getType() == Scanner.MYSQL_INFORMIX) {
                ttoken = mysql_informix_translator.get(ttoken);
            }
            if (ttoken != null) {
                return new Token(ttoken, KEYWORD, "KEYWORD");
            }
            
            if (lexer.getType() == Scanner.INFORMIX_MYSQL) {
                ttoken = informix_mysql_translator.get(ttoken);
            } else if (lexer.getType() == Scanner.MYSQL_INFORMIX) {
                ttoken = mysql_informix_translator.get(ttoken);
            }
            if (ttoken != null) {
                return new Token(ttoken, FUNCTION, "FUNCTION");
            }*/

    }
    
    private void translateKeyword() {
        
    }
    
    private void translateFunction() {
        
    }
    
    public String print() {
        /*System.out.print(token.getToken());
            if (token.isKeyword() || token.isnSymbol() || token.iscSymbol()) {
                System.out.print(" ");
            }*/
        return "";
    }
    
    /*Scanner bounds that encapsulates the statement
            define statements*/
    public Boolean compile(String statement) throws Exception {
        /*
        add statement root operator to statement to compose complete set of language statements
                SQL statement = <select statment> + <from statement> + <where statementt> + <group by statement> + <having> + <order by statement>
                <select statment>= Table.FIELD AS Alias or Expression AS Alias
                <Expression> = Mathematical functions/operators applied to N terms of {Table.FIELD or Expression}
                <from statement> = Table<with fields metadata>  , expression
                <where statementt> =
                <group by statement>=
                <having> =
                <order by statement> <FIELD , expression>
        */
/*        Operator staging = null;
        Operator operator = new Operator(OPERATOR.STATEMENT);
        Operand operand_register = null;
        Boolean string = false;
        Integer parantheses = 0;
        int ml = mm.size();
        
        while (next()) {
            if (sm.equalsIgnoreCase("'")) {
                if (sm0.equalsIgnoreCase("'")) {
                    operand_register = new Operand(OPERAND.STRING, "");
                    operator.push(operand_register);
                }
                string = !string;
            } else if (string == true) {
                operand_register = new Operand(OPERAND.STRING, sm);
                operator.push(operand_register);
            } else if (sm.equalsIgnoreCase("page")) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, "page");
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT).push(new Operand(OPERAND.VARIABLE, "$V{PAGE_NUMBER}", "java.lang.Integer"));
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("pageCount")) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, "pageCount");
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT).push(new Operand(OPERAND.VARIABLE, "$V{PAGE_COUNT}", "java.lang.Integer"));
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("date")) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, "date");
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT);
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("today")) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, "today");
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT).push(new Operand(OPERAND.UDATE, "java.util.Date.from(java.time.Instant.now())"));
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("month")) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, "month");
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT);
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("getrow")) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, "getrow");
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT).push(new Operand(OPERAND.VARIABLE, "$V{REPORT_COUNT}", "java.lang.Integer"));
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("sum")
                    || sm.equalsIgnoreCase("count")
                    || sm.equalsIgnoreCase("average")
                    || sm.equalsIgnoreCase("max")
                    || sm.equalsIgnoreCase("min")) {
                String functionName = sm.toUpperCase();
                //String newTypeClass = (functionName.equalsIgnoreCase("SUM") || functionName.equalsIgnoreCase("COUNT") || functionName.equalsIgnoreCase("AVERAGE") ? Double.class.getCanonicalName() : typeClass);
                calculation = functionName.equalsIgnoreCase("SUM") ? Calculation.SUM : functionName.equalsIgnoreCase("COUNT") ? Calculation.COUNT : functionName.equalsIgnoreCase("AVERAGE") ? Calculation.AVERAGE : functionName.equalsIgnoreCase("MAX") ? Calculation.MAX : functionName.equalsIgnoreCase("MIN") ? Calculation.MIN : Calculation.FORMULA;
                incrementType = Expression.IncrementType.NORMAL;
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, functionName);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT);
                operator.push(staging);
                operator = staging;
            } else if (dw.table.fields.containsKey(sm.toUpperCase())) {
                field = dw.table.fields.get(sm.toUpperCase());
                if (field.typeClass == null) {
                    throw new Exception("Unexpected typeClass 'null' for field '" + field.name + "' in variable '" + name + "' with expression '" + expression + "' ");
                }
                //if (operator.operator == OPERATOR.ARGUMENT) {
                    operator.push(new Operand(OPERAND.FIELD, "$F{" + field.name + "}", field.typeClass));
                /*} else {
                    operator.push(new Operand(Operand.operand(field.typeClass), "$F{" + field.name + "}"));
                }*/
                //type(field.typeClass);
/*            } else if (dw.table.variables.containsKey(sm.toUpperCase())) {
                variable = dw.table.variables.get(sm.toUpperCase());
                if (variable.computed == false) {
                    rturn.Return("type", "variable");
                    rturn.Return("variable", name);
                    return false;
                }
                if (variable.typeClass == null) {
                    throw new Exception("Unexpected typeClass 'null' for variable '" + variable.name + "' with expression '" + variable.expression + "' in variable '" + name + "' with expression '" + expression + "'");
                }
                if (operator.operator == OPERATOR.ARGUMENT) {
                    operator.push(new Operand(OPERAND.VARIABLE, "$V{" + variable.name + "}", variable.typeClass));
                } else {
                    operator.push(new Operand(Operand.operand(variable.typeClass), "$V{" + variable.name + "}"));
                }
                //type(variable.typeClass);
            } else if (sm.equalsIgnoreCase("if")) {
                staging = new Operator(OPERATOR.IF);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.IF_CONDITION);
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase(",")) {
                if (operator.operator == OPERATOR.IF_CONDITION) {
                    operator = operator.parent;//IF
                    staging = new Operator(OPERATOR.IF_THEN);
                    operator.push(staging);
                    operator = staging;
                } else if (operator.operator == OPERATOR.IF_THEN) {
                    operator = operator.parent;//IF
                    staging = new Operator(OPERATOR.IF_ELSE);
                    operator.push(staging);
                    operator = staging;
                } else {
                    throw new Exception("Sysntax Error: undefined '" + sm + "' in " + operator.operator);
                }
            } else if (sm.equalsIgnoreCase("case")) {
                staging = new Operator(OPERATOR.CASE);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.CASE_CONDITION);
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("string") == true
                    || sm.equalsIgnoreCase("integer") == true
                    || sm.equalsIgnoreCase("int") == true
                    || sm.equalsIgnoreCase("long") == true
                    || sm.equalsIgnoreCase("float") == true
                    || sm.equalsIgnoreCase("double") == true
                    || sm.equalsIgnoreCase("date") == true
                    || sm.equalsIgnoreCase("time") == true
                    || sm.equalsIgnoreCase("timestamp") == true) {
                staging = new Operator(OPERATOR.RETURN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.METHOD, sm.toUpperCase());
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.ARGUMENT);//.push(new Operand(Operand.operand(Operator.getMethodTypeClass(sm)), ""));
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("when")) {
                if (operator.operator == OPERATOR.CASE_THEN) {
                    operator = operator.parent.parent;
                } else if (operator.operator == OPERATOR.CASE_CONDITION) {
                    operator = operator.parent;
                } else {
                    throw new Exception("Sysntax Error: undefined '" + sm + "' in " + operator.operator);
                }
                staging = new Operator(OPERATOR.CASE_WHEN);
                operator.push(staging);
                operator = staging;
                staging = new Operator(OPERATOR.CASE_WHEN_CONDITION).push(new Operator(OPERATOR.CONDITION_EQUAL));
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("then")) {
                if (operator.operator == OPERATOR.CASE_WHEN_CONDITION) {
                    operator = operator.parent;
                } else if (operator.operator == OPERATOR.CASE_THEN) {
                } else {
                    throw new Exception("Sysntax Error: undefined '" + sm + "' in " + operator.operator);
                }
                staging = new Operator(OPERATOR.CASE_THEN);
                operator.push(staging);
                operator = staging;
            } else if (sm.equalsIgnoreCase("else") == true) {
                if (operator.operator == OPERATOR.CASE_THEN) {
                    operator = operator.parent.parent;
                } else {
                    throw new Exception("Sysntax Error: CASE_ELSE stated with no CASE_THEN '" + sm + "' in " + operator.operator);
                }
                staging = new Operator(OPERATOR.CASE_ELSE);
                operator.push(staging);
                operator = staging;
            } else if (sm.length() == 1 && "+*-/=<>".indexOf(sm) > -1) {
                if (operator.operator == OPERATOR.IF_CONDITION) {
                    switch (sm) {
                        case "=": operator.push(new Operator(OPERATOR.CONDITION_EQUAL));break;
                        case "<": operator.push(new Operator(OPERATOR.CONDITION_EQUAL));break;
                        case ">": operator.push(new Operator(OPERATOR.CONDITION_EQUAL));break;
                        default: throw new Exception("Sysntax Error: undefined '" + sm + "' in " + operator.operator);
                    }
                } else {
                    switch (sm) {
                        case "+": operator.push(new Operator(OPERATOR.ADD));break;
                        case "-": operator.push(new Operator(OPERATOR.SUBTRACT));break;
                        case "*": operator.push(new Operator(OPERATOR.MULTIPLY));break;
                        case "/": operator.push(new Operator(OPERATOR.DEVIDE));break;
                        case "=": operator.push(new Operator(OPERATOR.ASSIGN));break;
                        default: throw new Exception("Sysntax Error: undefined '" + sm + "' in " + operator.operator);
                    }
                }
            } else if (sm.equalsIgnoreCase(")")) {
                parantheses--;
                if (operator.operator == OPERATOR.CASE_THEN) {
                    operator = operator.parent.parent;
                    staging = new Operator(OPERATOR.CASE_END);
                    operator.push(staging);
                } else if (operator.operator == OPERATOR.CASE_ELSE) {
                    operator = operator.parent;
                    staging = new Operator(OPERATOR.CASE_END);
                    operator.push(staging);
                } else if (operator.operator == OPERATOR.IF_THEN
                        || operator.operator == OPERATOR.IF_ELSE) {
                    operator = operator.parent;
                    staging = new Operator(OPERATOR.IF_END);
                    operator.push(staging);
                } else if (operator.operator == OPERATOR.ARGUMENT) {
                    operator = operator.parent.parent;
                }/* else {//PARENTHESIS_CLOSE
                    operator = operator.parent;
                }*/
/*                if (operator == null) {
                    throw new Exception("Operator is null");
                } else if (operator.parent == null) {
                    throw new Exception("Operator '" + operator + "' parent is null");
                }
                operator = operator.parent;
            } else if (sm.equalsIgnoreCase("(")) {
                parantheses++;
                if (operator.operator != OPERATOR.IF_CONDITION
                        && operator.operator != OPERATOR.CASE_CONDITION
                        && operator.operator != OPERATOR.ARGUMENT) {
                    staging = new Operator(OPERATOR.PARENTHESIS_OPEN);
                    operator.push(staging);
                    operator = staging;
                }
                //statement.push(operator);
            } else if (sm.equalsIgnoreCase("for") == true) {
                next();
                validateSM("group");
                next();
                Group group = dw.groups.get(sm.toUpperCase());
                if(group != null) {
                    resetGroupName = group.name;
                } else {
                    throw new Exception("unexpected '" + sm + "'");
                }
            } else if (sm.equalsIgnoreCase("AND") == true || sm.equalsIgnoreCase("OR") == true) {
                if (operator.operator == OPERATOR.IF_CONDITION) {
                    if (sm.equalsIgnoreCase("AND") == true) {
                        operator.push(new Operator(OPERATOR.CONDITION_AND));
                    } else if (sm.equalsIgnoreCase("OR") == true) {
                        operator.push(new Operator(OPERATOR.CONDITION_OR));
                    }
                } else {
                    throw new Exception("Sysntax Error: undefined '" + sm + "' in " + operator.operator);
                }
            } else if (sm.matches("[-+]?\\d*") == true) {
                operand_register = new Operand(OPERAND.INTEGER, sm);
                operator.push(operand_register);
            } else if (sm.matches("[-+]?\\d*\\.\\d+") == true) {
                operand_register = new Operand(OPERAND.DOUBLE, sm);
                operator.push(operand_register);
            } else {
                throw new Exception("unexpected '" + sm + "'");
            }
        }
        
        if (operator.operator != OPERATOR.STATEMENT) {
            throw new Exception("Syntax Error in '" + name + "' for '" + ii + "'");
        }
        if (parantheses != 0) {
            throw new Exception("Syntax Error unbalanced parantheses in '" + name + "' for '" + ii + "'");
        }
        //debug += "\n" + ii + operator.toString(0,4);
        run = operator.evaluate();
        debug += "\n" + ii + "\n" + run + operator.toString(0,4);
        typeClass = operator.runtimeTypeClass;
        return computed = true;
    }
*/
        return null;
    }
}
