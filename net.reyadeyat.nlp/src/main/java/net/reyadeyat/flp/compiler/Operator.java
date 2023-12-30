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
import java.util.Stack;
import net.reyadeyat.flp.compiler.Operand.OPERAND;

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
public class Operator implements Operandum {

    public enum OPERATOR {
        STATEMENT, METHOD, ARGUMENT, CODE, RETURN, CASE, CASE_CONDITION, CASE_WHEN, CASE_WHEN_CONDITION, CASE_THEN, CASE_ELSE, CASE_END, IF, IF_CONDITION, IF_THEN, IF_ELSE, IF_ELSEIF, IF_END, BREAK, WISE, ASSIGN, ADD, SUBTRACT, MULTIPLY, DEVIDE, CONDITION_NOT, CONDITION_AND, CONDITION_OR, CONDITION_EQUAL, CONDITION_GREAT, CONDITION_LESS, SHORT_IF, SHORT_IF_CONDITION, SHORT_IF_TRUE, SHORT_IF_FALSE, PARENTHESIS_OPEN, PARENTHESIS_CLOSE,
ALL, ALTER, AND, ANY, AS, ASC, BACKUP, BETWEEN, BY, CHECK, COLUMN, CONSTRAINT, CREATE, DATABASE, DEFAULT, DELETE, DESC, DISTINCT, DROP, EXEC, EXISTS, FOREIGN, FROM, FULL, GROUP, HAVING, IN, INDEX, INNER, INSERT, INTO, IS, JOIN, KEY, LEFT, LIKE, LIMIT, NOT, NULL, OR, ORDER, OUTER, PRIMARY, PROCEDURE, REPLACE, RIGHT, ROWNUM, SELECT, SET, TABLE, TOP, TRUNCATE, UNION, UNIQUE, UPDATE, VALUES, VIEW, WHERE
/*"ALTER",
"ALTER COLUMN",
"ALTER TABLE",
"ALL",
"AND",
"ANY",
"AS",
"ASC",
"BACKUP DATABASE",
"BETWEEN",
"CASE",
"CHECK",
"COLUMN",
"CONSTRAINT",
"CREATE",
"CREATE DATABASE",
"CREATE INDEX",
"CREATE OR REPLACE VIEW",
"CREATE TABLE",
"CREATE PROCEDURE",
"CREATE UNIQUE INDEX",
"CREATE VIEW",
"DATABASE",
"DEFAULT",
"DELETE",
"DESC",
"DISTINCT",
"DROP",
"DROP COLUMN",
"DROP CONSTRAINT",
"DROP DATABASE",
"DROP DEFAULT",
"DROP INDEX",
"DROP TABLE",
"DROP VIEW",
"EXEC",
"EXISTS",
"FOREIGN KEY",
"FROM",
"FULL OUTER JOIN",
"GROUP BY",
"HAVING",
"IN",
"INDEX",
"INNER JOIN",
"INSERT INTO",
"INSERT INTO SELECT",
"IS NULL",
"IS NOT NULL",
"JOIN",
"LEFT JOIN",
"LIKE",
"LIMIT",
"NOT",
"NOT NULL",
"OR",
"ORDER BY",
"OUTER JOIN",
"PRIMARY KEY",
"PROCEDURE",
"RIGHT JOIN",
"ROWNUM",
"SELECT",
"SELECT DISTINCT",
"SELECT INTO",
"SELECT TOP",
"SET",
"TABLE",
"TOP",
"TRUNCATE TABLE",
"UNION",
"UNION ALL",
"UNIQUE",
"UPDATE",
"VALUES",
"VIEW",
"WHERE",*/
    };

    public enum OPERATION {
        STRUCTURE, DATA, CONTROL, CONTROL_END, CONDITION, ARITHMETIC
    }
    public OPERATION operation;
    public OPERATOR operator;
    public ArrayList<Operandum> operanda;
    public ArrayList<Operand> operands;
    public ArrayList<Operator> operators;
    public Boolean interpret;
    public String runtimeTypeClass;
    public String source_code;
    public String runtime_code;
    public Operator parent;
    static public HashMap<String, String> method_typeClass = new HashMap<String, String>() {
        {
            put("string", "java.lang.String");
            put("integer", "java.lang.Integer");
            put("long", "java.lang.Long");
            put("float", "java.lang.float");
            put("double", "java.lang.double");
            put("date", "java.sql.Date");
            put("time", "java.sql.Time");
            put("timestamp", "java.sql.Timestamp");
        }
    };

    public Operator(OPERATOR operator) throws Exception {
        this(operator, null);
    }

    public Operator(OPERATOR operator, String source_code) throws Exception {
        interpret = false;
        operanda = new ArrayList<Operandum>();
        operands = new ArrayList<Operand>();
        operators = new ArrayList<Operator>();
        this.operator = operator;
        this.source_code = source_code;

        switch (operator) {
            case STATEMENT:
                operation = OPERATION.STRUCTURE;
                break;
            case CASE:
            case CASE_CONDITION:
            case CASE_WHEN:
            case CASE_WHEN_CONDITION:
            case CASE_THEN:
            case CASE_ELSE:
            case IF:
            case IF_CONDITION:
            case IF_THEN:
            case IF_ELSE:
            case IF_ELSEIF:
            case SHORT_IF:
            case SHORT_IF_CONDITION:
            case SHORT_IF_TRUE:
            case SHORT_IF_FALSE:
            case PARENTHESIS_OPEN:
                operation = OPERATION.CONTROL;
                break;
            case CASE_END:
            case IF_END:
            case PARENTHESIS_CLOSE:
            case BREAK:
                operation = OPERATION.CONTROL_END;
                break;
            case METHOD:
            case ARGUMENT:
            case CODE:
            case RETURN:
                operation = OPERATION.DATA;
                break;
            case WISE:
            case ASSIGN:
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DEVIDE:
                operation = OPERATION.ARITHMETIC;
                break;
            case CONDITION_NOT:
            case CONDITION_AND:
            case CONDITION_OR:
            case CONDITION_EQUAL:
            case CONDITION_GREAT:
            case CONDITION_LESS:
                operation = OPERATION.CONDITION;
                break;
            default:
                throw new Exception("OPERATION not defined for Operator '" + operator + "'");
        }
    }

    public Operator push(Operand operand) {
        operands.add(operand);
        operanda.add(operand);
        return this;
    }

    public Operator push(Operator operator) {
        operator.parent = this;
        operators.add(operator);
        operanda.add(operator);
        return this;
    }

    public Operator parent(Operator operator) {
        return this.parent;
    }

    public String evaluate() throws Exception {
        if (interpret == false && runtime_code != null) {
            return runtime_code;
        }
        Stack<Operandum> stack = new Stack<Operandum>();
        //Stack<Operator> device = new Stack<Operator>();
        //Stack<Operand> register = new Stack<Operand>();
        //stack.push(this);
        runtime_code = evaluate(stack, null, null);
        /*if (register != null && register.size() > 0) {
            throw new Exception("Register of Operator '" + operator + "' has not been totaly consumed");
        }
        if (device != null && device.size() > 0) {
            throw new Exception("Device of Operator '" + operator + "' has not been totaly consumed");
        }*/
        if (stack.size() > 0) {
            throw new Exception("Stack of Operator '" + operator + "' has not been totaly consumed");
        }
        return runtime_code;
    }

    @Override
    public String evaluate(Stack<Operandum> stack, Stack<Operator> device, Stack<Operand> register) throws Exception {
        if (interpret == false && runtime_code != null) {
            return runtime_code;
        }
        StringBuilder b = new StringBuilder();
        stack.push(this);
        if (operator == OPERATOR.STATEMENT) {
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
        } else if (operator == OPERATOR.METHOD) {
            if (source_code.equalsIgnoreCase("string") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'string' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operand operand = argument.getOperand(0);
                if (operand.operand == OPERAND.FIELD
                        || operand.operand == OPERAND.VARIABLE) {
                    b.append("String.valueOf(").append(argument.runtime_code).append(")");
                } else {
                    b.append("String.valueOf(\"").append(argument.runtime_code).append("\")");
                }
                type("java.lang.String");
            } else if (source_code.equalsIgnoreCase("integer") == true
                    || source_code.equalsIgnoreCase("int") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'integer' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operand operand = argument.getOperand(0);
                if (operand.operand == OPERAND.FIELD
                        || operand.operand == OPERAND.VARIABLE) {
                    b.append("Integer.valueOf(").append(argument.runtime_code).append(")");
                } else {
                    b.append("Integer.valueOf(\"").append(argument.runtime_code).append("\")");
                }
                type("java.lang.Integer");
            } else if (source_code.equalsIgnoreCase("long") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'long' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operand operand = argument.getOperand(0);
                if (operand.operand == OPERAND.FIELD
                        || operand.operand == OPERAND.VARIABLE) {
                    b.append("Long.valueOf(").append(argument.runtime_code).append(")");
                } else {
                    b.append("Long.valueOf(\"").append(argument.runtime_code).append("\")");
                }
                type("java.lang.Long");
            } else if (source_code.equalsIgnoreCase("float") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'float' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operand operand = argument.getOperand(0);
                if (operand.operand == OPERAND.FIELD
                        || operand.operand == OPERAND.VARIABLE) {
                    b.append("Float.valueOf(").append(argument.runtime_code).append(")");
                } else {
                    b.append("Float.valueOf(\"").append(argument.runtime_code).append("\")");
                }
                type("java.lang.Float");
            } else if (source_code.equalsIgnoreCase("double") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'double' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operand operand = argument.getOperand(0);
                if (operand.operand == OPERAND.FIELD
                        || operand.operand == OPERAND.VARIABLE) {
                    b.append("Double.valueOf(").append(argument.runtime_code).append(")");
                } else {
                    b.append("Double.valueOf(\"").append(argument.runtime_code).append("\")");
                }
                type("java.lang.Double");
            } else if (source_code.equalsIgnoreCase("date") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'date' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operator rturn = argument.getOperator(OPERATOR.RETURN);
                Operand oprnd = argument.getOperand(0);
                if (rturn != null) {
                    b.append(rturn.runtime_code);
                    type(argument);
                } else if (oprnd != null 
                        && (oprnd.operand == OPERAND.FIELD || oprnd.operand == OPERAND.VARIABLE)) {
                    argument.evaluate(stack, device, register);
                    b.append("new SimpleDateFormat(\"dd-MMM-yyyy\").parse(").append(oprnd.runtime_code).append(")");
                    //type(oprnd);
                    //type("java.util.Date");
                    type("java.lang.Integer");
                }
            } else if (source_code.equalsIgnoreCase("month") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'date' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                Operator rturn = argument.getOperator(OPERATOR.RETURN);
                Operand oprnd = argument.getOperand(0);
                if (rturn != null) {
                    b.append(rturn.runtime_code);
                    type(argument);
                } else if (oprnd != null 
                        && (oprnd.operand == OPERAND.FIELD || oprnd.operand == OPERAND.VARIABLE)) {
                    argument.evaluate(stack, device, register);
                    b.append("new SimpleDateFormat(\"MM\").parse(").append(oprnd.runtime_code).append(")");
                    //type(oprnd);
                    type("java.util.Date");
                }
            } else if (source_code.equalsIgnoreCase("time") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'time' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append("java.sql.Time.from(").append(argument.runtime_code).append(")");
                type(argument);
            } else if (source_code.equalsIgnoreCase("timestamp") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'timestamp' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append("java.sql.Timestamp.from(").append(argument.runtime_code).append(")");
                type(argument);
            } else if (source_code.equalsIgnoreCase("today") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() != 1) {
                    throw new Exception("METHOD 'today' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append(argument.runtime_code);
                type(argument);
            } else if (source_code.equalsIgnoreCase("page") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'page' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append(argument.runtime_code);
                type(argument);
            } else if (source_code.equalsIgnoreCase("pageCount") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'pageCount' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append(argument.runtime_code);
                type(argument);
            } else if (source_code.equalsIgnoreCase("getrow") == true) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() > 1) {
                    throw new Exception("METHOD 'getrow' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append(argument.runtime_code);
                type(argument);
            } else if (source_code.equalsIgnoreCase("sum")
                    || source_code.equalsIgnoreCase("count")
                    || source_code.equalsIgnoreCase("average")
                    || source_code.equalsIgnoreCase("max")
                    || source_code.equalsIgnoreCase("min")) {
                Operator argument = getOperator(OPERATOR.ARGUMENT);
                if (argument.operanda.size() == 0) {
                    throw new Exception("METHOD '" + source_code + "' wrong number of arguments '" + argument.operanda.size() + "'");
                }
                argument.evaluate(stack, device, register);
                b.append(argument.runtime_code);
                //b.append(source_code.toUpperCase()).append("(").append(argument.runtime_code).append(")");
                type(argument);
            } else {
                throw new Exception("METHOD '" + source_code + "' is undefined");
            }

        } else if (operator == OPERATOR.ARGUMENT) {
            //Validation
            /*for (int i = 0; i < operators.size(); i++) {
                Operator operat = operators.get(i);
                if (operat.operator != OPERATOR.RETURN
                        && operat.operator != OPERATOR.ADD
                        && operat.operator != OPERATOR.SUBTRACT
                        && operat.operator != OPERATOR.MULTIPLY
                        && operat.operator != OPERATOR.DEVIDE) {
                    b.append(operat.toString()).append(",");
                }
            }
            if (b.length() > 0) {
                b.deleteCharAt(b.length()-1);
                throw new Exception("ARGUMENT Operator can't have operators other than {RETURN, ADD, SUBTRACT, MULTIPLY, DEVIDE} {" + b.toString() + "}");
            }*/
            //Code
            for (int i = 0; i < operanda.size(); i++) {
                Operandum oprndm = operanda.get(i);
                String e = oprndm.evaluate(stack, device, register);
                if (oprndm.isOperator(OPERATION.ARITHMETIC)) {
                    b.append(e);
                } else if (oprndm.isOperand() && oprndm.getOperand().typeClass != null) {
                    b.append(e);
                    type(oprndm);
                } else if (oprndm.isOperand()) {
                    throw new Exception("Operand '" + oprndm + "' typeClass is null");
                } else if (oprndm.isOperator() && oprndm.getOperator().runtimeTypeClass != null) {
                    b.append(e);
                    type(oprndm);
                } else if (oprndm.isOperator()) {
                    throw new Exception("Operator '" + oprndm + "' runtimeTypeClass is null");
                } else {
                    throw new Exception("Operandum neither Operator nor Operand!");
                }
            }
        } else if (operator == OPERATOR.RETURN) {
            for (int i = 0; i < operanda.size(); i++) {
                Operandum oprndm = operanda.get(i);
                if (oprndm.isOperator()) {
                    Operator oprtr = oprndm.getOperator();
                    b.append(oprtr.evaluate(stack, device, register));
                    type(oprtr);
                } else if (oprndm.isOperand()) {
                    Operand oprnd = oprndm.getOperand();
                    b.append(oprnd.evaluate(stack, device, register));
                    type(oprnd);
                }
            }
        } else if (operator == OPERATOR.ADD) {
            b.append("+");
        } else if (operator == OPERATOR.SUBTRACT) {
            b.append("-");
        } else if (operator == OPERATOR.MULTIPLY) {
            b.append("*");
        } else if (operator == OPERATOR.DEVIDE) {
            b.append("/");
        } else if (operator == OPERATOR.ASSIGN) {
            b.append("=");
        } else if (operator == OPERATOR.CASE) {
            //Validation
            for (int i = 0; i < operators.size(); i++) {
                Operator operat = operators.get(i);
                if (operat.operator != OPERATOR.CASE_CONDITION
                        && operat.operator != OPERATOR.CASE_WHEN
                        && operat.operator != OPERATOR.CASE_ELSE
                        && operat.operator != OPERATOR.CASE_END) {
                    b.append(operat.toString()).append(",");
                }
            }
            if (b.length() > 0) {
                b.deleteCharAt(b.length() - 1);
                throw new Exception("CASE Operator can't have operators other than {CASE_CONDITION, CASE_WHEN, CASE_ELSE, CASE_END} {" + b.toString() + "}");
            }
            //Code
            Stack<Operator> dev = new Stack<Operator>();
            Stack<Operand> reg = new Stack<Operand>();
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                String e = operandum.evaluate(stack, dev, reg);
                if (operandum.isOperator(OPERATOR.CASE_CONDITION) == false) {
                    b.append(e);
                }
                type(operandum);
            }
        } else if (operator == OPERATOR.CASE_CONDITION) {
            //Validation
            //Code
            device.push(this);
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
                if (operandum.isOperator() == true && operandum.isOperator(OPERATOR.RETURN) == false) {
                    //dev.push(operandum.getOperand());
                    throw new Exception("Compilation error; CASE_CONDITION unexpected Operandum found '" + operandum + "'");
                } else if (operandum.isOperator(OPERATOR.RETURN) == true) {
                    register.push(Operand.toOperand(operandum.getOperator()));
                } else if (operandum.isOperand() == true) {
                    register.push(operandum.getOperand());
                } else {
                    throw new Exception("Compilation error; CASE_CONDITION unexpected Operandum found '" + operandum + "'");
                }
            }
        } else if (operator == OPERATOR.CASE_WHEN) {
            //Validation
            for (int i = 0; i < operators.size(); i++) {
                Operator operat = operators.get(i);
                if (operat.operator != OPERATOR.CASE_WHEN_CONDITION
                        && operat.operator != OPERATOR.CASE_THEN) {
                    b.append(operat.toString()).append(",");
                }
            }
            if (b.length() > 0) {
                b.deleteCharAt(b.length() - 1);
                throw new Exception("CASE_WHEN Operator can't have operators other than {CASE_WHEN_CONDITION, CASE_THEN} {" + b.toString() + "}");
            }
            //Code
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
        } else if (operator == OPERATOR.CASE_WHEN_CONDITION) {
            //Validation
            //Code
            //Inject CASE_CONDITION from CASE device into CASE_WHEN_CONDITION device
            if (device.size() != 1) {
                throw new Exception("Compilation error; CASE_WHEN_CONDITION has " + device.size() + " operators; required 1!");
            }
            Operator coprtr = device.pop();
            if (coprtr.operator == OPERATOR.CASE_CONDITION) {
                operanda.add(0, Operand.toOperand(coprtr));
            } else {
                throw new Exception("Compilation error; CASE_WHEN_CONDITION requires CASE_CONDITION operator but found '" + coprtr + "' operator!");
            }
            device.push(coprtr);
            if (operanda.size() % 3 != 0) {
                throw new Exception("Compilation error; CASE_WHEN_CONDITION conditional operandums are not ternary!");
            }
            Stack<Operator> dev = new Stack<Operator>();
            Stack<Operand> reg = new Stack<Operand>();
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                if (i % 3 == 0) {
                    if (operandum.isOperator()) {
                        if (operandum.getOperator().operation == OPERATION.DATA) {
                            operandum.evaluate(stack, device, register);
                            Operand oprdn = Operand.toOperand(operandum.getOperator());
                            reg.push(oprdn);
                        } else {
                            throw new Exception("Compilation error; CASE_WHEN_CONDITION operator 1 is not OPERATION.DATA operator!");
                        }
                    } else {
                        Operand oprdn = operandum.getOperand();
                        reg.push(oprdn);
                        oprdn.evaluate(stack, device, register);
                        type(oprdn);
                    }
                } else if (i % 3 == 1) {
                    if (operandum.isOperator()
                            && operandum.getOperator().operation == OPERATION.CONDITION) {
                        dev.push(operandum.getOperator());//Condition; don't evaluate
                    } else {
                        throw new Exception("Compilation error; CASE_WHEN_CONDITION operator 2 is '" + operandum + "' not OPERATION.CONDITION operator!");
                    }
                } else if (i % 3 == 2) {
                    if (operandum.isOperator()) {
                        if (operandum.getOperator().operation == OPERATION.DATA) {
                            operandum.evaluate(stack, device, register);
                            Operand oprdn = Operand.toOperand(operandum.getOperator());
                            reg.push(oprdn);
                        } else {
                            throw new Exception("Compilation error; CASE_WHEN_CONDITION operator 3 is not OPERATION.DATA operator!");
                        }
                    } else {
                        Operand oprdn = operandum.getOperand();
                        reg.push(oprdn);
                        oprdn.evaluate(stack, device, register);
                        type(oprdn);
                    }
                }
            }
            while (dev.size() > 0) {//Condition; evaluate
                Operator oprtr = dev.pop();
                b.append(oprtr.evaluate(stack, null, reg));
                type(oprtr);
            }
        } else if (operator == OPERATOR.CASE_THEN) {
            //Validation
            //Code
            b.append(" ? ");
            Operator opr = device.pop();
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
            device.push(opr);
            b.append(" : ");
        } else if (operator == OPERATOR.CASE_ELSE) {
            //Validation
            //Code
            Operator opr = device.pop();
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
                device.push(opr);
            }
            device.push(this);
            device.push(opr);
        } else if (operator == OPERATOR.CASE_END) {
            //Validation
            //Code
            Boolean elsed = false;
            Operandum case_condition = null;
            while (device.size() > 0) {
                Operandum oprndm = device.pop();
                if (oprndm.isOperator(OPERATOR.CASE_CONDITION)) {
                    case_condition = oprndm;
                } else if (oprndm.isOperator(OPERATOR.CASE_ELSE)) {
                    elsed = true;
                }
            }
            if (elsed == false) {
                b.append(Operand.defaultValue(Operand.operand(case_condition.getType())));
            }
            register.clear();
            device.clear();
        } else if (operator == OPERATOR.IF) {
            //Validation
            for (int i = 0; i < operators.size(); i++) {
                Operator oprtr = operators.get(i);
                if (oprtr.operator != OPERATOR.IF_CONDITION
                        && oprtr.operator != OPERATOR.IF_THEN
                        && oprtr.operator != OPERATOR.IF_ELSEIF
                        && oprtr.operator != OPERATOR.IF_ELSE
                        && oprtr.operator != OPERATOR.IF_END) {
                    b.append(oprtr.toString()).append(",");
                }
            }
            if (b.length() > 0) {
                b.deleteCharAt(b.length() - 1);
                throw new Exception("IF Operator can't have operators other than {IF_CONDITION, IF_THEN, IF_ELSEIF, IF_ELSE, IF_END} {" + b.toString() + "}");
            }
            //Code
            Stack<Operator> dev = new Stack<Operator>();
            Stack<Operand> reg = new Stack<Operand>();
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, dev, reg));
                type(operandum);
            }
        } else if (operator == OPERATOR.IF_CONDITION) {
            //Validation
            //Code
            if (operanda.size() % 3 != 0) {
                throw new Exception("Compilation error; IF_CONDITION conditional operandums are not ternary terms!");
            }
            
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                if (i % 3 == 0) {
                    if (operandum.isOperator()) {
                        if (operandum.getOperator().operation == OPERATION.DATA) {
                            operandum.evaluate(stack, device, register);
                            Operand oprdn = Operand.toOperand(operandum.getOperator());
                            register.push(oprdn);
                        } else {
                            throw new Exception("Compilation error; IF_CONDITION operator 1 is not OPERATION.DATA operator!");
                        }
                    } else {
                        Operand oprdn = operandum.getOperand();
                        register.push(oprdn);
                        oprdn.evaluate(stack, device, register);
                        type(oprdn);
                    }
                } else if (i % 3 == 1) {
                    if (operandum.isOperator()
                            && operandum.getOperator().operation == OPERATION.CONDITION) {
                        device.push(operandum.getOperator());//Condition; don't evaluate
                    } else {
                        throw new Exception("Compilation error; IF_CONDITION operator 2 is '" + operandum + "' not OPERATION.CONDITION operator!");
                    }
                } else if (i % 3 == 2) {
                    if (operandum.isOperator()) {
                        if (operandum.getOperator().operation == OPERATION.DATA) {
                            operandum.evaluate(stack, device, register);
                            Operand oprdn = Operand.toOperand(operandum.getOperator());
                            register.push(oprdn);
                        } else {
                            throw new Exception("Compilation error; IF_CONDITION operator 3 is not OPERATION.DATA operator!");
                        }
                    } else {
                        Operand oprdn = operandum.getOperand();
                        register.push(oprdn);
                        oprdn.evaluate(stack, device, register);
                        type(oprdn);
                    }
                }
            }
            while (device.size() > 0) {//Condition; evaluate
                Operator oprtr = device.pop();
                b.append(oprtr.evaluate(stack, device, register));
                type(oprtr);
            }
        } else if (operator == OPERATOR.CONDITION_NOT) {
            //Validation
            //Code
            b.append(" ! ");
        } else if (operator == OPERATOR.CONDITION_AND) {
            //Validation
            //Code
            b.append(" AND ");
        } else if (operator == OPERATOR.CONDITION_OR) {
            //Validation
            //Code
            b.append(" OR ");
        } else if (operator == OPERATOR.CONDITION_EQUAL) {
            //Validation
            //Code
            Operand oprnd2 = register.pop();
            Operand oprnd1 = register.pop();
            String code2 = oprnd2.runtime_code;
            String code1 = oprnd1.runtime_code;
            int comp = oprnd1.compareTo(oprnd2);
            if (comp > 0) {
                type(oprnd1);
                code2 = oprnd2.cast(oprnd1.getType());
                //Operand operandx = oprnd1.clone();
                //operandx.typeClass(oprnd2.getType());
                //oprnd2 = operandx;
            } else if (comp < 0) {
                type(oprnd2);
                code1 = oprnd1.cast(oprnd2.getType());
                //Operand operandx = oprnd2.clone();
                //operandx.typeClass(oprnd1.getType());
                //oprnd2 = operandx;
            }
            /*if (oprnd1.compareTo(oprnd2) != 0) {
                throw new Exception("Compilation error; CONDITIONA_EQUALS operands are not of same type '" + oprnd1.typeClass + "' == '" + oprnd2.typeClass + "'");
            }*/
            if (runtimeTypeClass.equalsIgnoreCase("java.lang.String")) {
                b.append(code1).append(".equalsIgnoreCase(").append(code2).append(")");
            } else if (runtimeTypeClass.equalsIgnoreCase("java.sql.Date") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.sql.Time") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.sql.Timestamp") == true) {
                b.append(code1).append(".equals(").append(code2).append(")");
            } else if (runtimeTypeClass.equalsIgnoreCase("java.lang.Byte") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Short") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Integer") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Long") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Float") == true
                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Double") == true) {
                b.append(code1).append(" == ").append(code2);
            }
        } else if (operator == OPERATOR.CONDITION_GREAT) {
            //Validation
            //Code
            b.append(" > ");
        } else if (operator == OPERATOR.CONDITION_LESS) {
            //Validation
            //Code
            b.append(" < ");
        } else if (operator == OPERATOR.IF_ELSEIF) {
            //Validation
            for (int i = 0; i < operators.size(); i++) {
                Operator operat = operators.get(i);
                if (operat.operator != OPERATOR.IF_CONDITION
                        && operat.operator != OPERATOR.IF_ELSEIF
                        && operat.operator != OPERATOR.IF_END) {
                    b.append(operat.toString()).append(",");
                }
            }
            if (b.length() > 0) {
                b.deleteCharAt(b.length() - 1);
                throw new Exception("IF_ELSEIF Operator can't have operators other than {IF_CONDITION, IF_ELSEIF, IF_END} {" + b.toString() + "}");
            }
            //Code
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
            }
        } else if (operator == OPERATOR.IF_THEN) {
            //Validation
            //Code
            b.append(" ? ");
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
        } else if (operator == OPERATOR.IF_ELSE) {
            //Validation
            //Code
            b.append(" : ");
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
        } else if (operator == OPERATOR.IF_END) {
            //Validation
            //Code
            register.clear();
            device.clear();
        } else if (operator == OPERATOR.PARENTHESIS_OPEN) {
            //Validation
            //Code
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
        } else if (operator == OPERATOR.PARENTHESIS_CLOSE) {
            //Validation
            //Code
            for (int i = 0; i < operanda.size(); i++) {
                Operandum operandum = operanda.get(i);
                b.append(operandum.evaluate(stack, device, register));
                type(operandum);
            }
        } else {
            throw new Exception("Unhandled Evaluation for Operator '" + operator + "'");
        }
        Operandum poped = stack.pop();

        runtime_code = b.toString();
        return runtime_code;
    }

    @Override
    public String toString(Integer level, Integer shift) {
        StringBuilder b = new StringBuilder();

        b.append("\n");
        for (int i = 0; i < level * shift; i++) {
            /*if (i%4 == 0) {
                b.append("|");
            } else {
                b.append("_");
            }*/
            b.append(" ");
        }
        b.append("|");
        for (int i = 0; i < shift - 1; i++) {
            b.append(".");
        }
        b.append("Operator: ").append(operator).append(" {" + (source_code == null ? "" : source_code) + "}{" + (runtime_code == null ? "" : runtime_code) + "}(" + (runtimeTypeClass == null ? "" : runtimeTypeClass) + ")");
        for (Operandum operandum : operanda) {
            b.append(operandum.toString(level + 1, shift));
        }
        return b.toString();
    }

    static public String getMethodTypeClass(String name) throws Exception {
        return method_typeClass.get(name);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Operator: ").append(operator).append(" {" + (source_code == null ? "" : source_code) + "}{" + (runtime_code == null ? "" : runtime_code) + "}(" + (runtimeTypeClass == null ? "" : runtimeTypeClass) + ")").toString();
    }

    @Override
    public Boolean isOperator() {
        return true;
    }

    @Override
    public Boolean isOperator(Operator.OPERATION operation) {
        return this.operation == operation;
    }

    @Override
    public Boolean isOperand() {
        return false;
    }

    @Override
    public Boolean isOperator(Operator.OPERATOR operator) {
        return this.operator == operator;
    }

    @Override
    public Boolean isOperand(Operand.OPERAND operand) {
        return false;
    }

    @Override
    public Operator getOperator() {
        return this;
    }

    @Override
    public Operand getOperand() {
        return null;
    }

    @Override
    public String getType() {
        return runtimeTypeClass;
    }

    private Operand getOperand(Integer index) throws Exception {
        if (index >= operands.size()) {
            //throw new Exception("Operator " + source_code + " doesn't has operand at index [" + index + "]");
            return null;
        }
        return operands.get(index);
    }

    private Operand getOperand(Operand.OPERAND operan) throws Exception {
        for (int i = 0; i < operands.size(); i++) {
            Operand oprnd = operands.get(i);
            if (oprnd.operand == operan) {
                return oprnd;
            }
        }
        throw new Exception("Operator " + source_code + " doesn't has operand '" + operan + "'");
    }

    private Operator getOperator(Integer index) throws Exception {
        if (index >= operators.size()) {
            throw new Exception("Operator " + source_code + " doesn't has operator at index [" + index + "]");
        }
        return operators.get(index);
    }

    private Operator getOperator(OPERATOR operat) throws Exception {
        for (int i = 0; i < operators.size(); i++) {
            Operator oprtr = operators.get(i);
            if (oprtr.operator == operat) {
                return oprtr;
            }
        }
        //throw new Exception("Operator " + source_code + " doesn't has operator '" + operat + "'");
        return null;
    }

    public void type(Operandum operandum) throws Exception {
        if (operandum.isOperator(OPERATION.CONTROL_END) == true
                || operandum.isOperator(OPERATION.ARITHMETIC) == true) {
            return;
        }
        type(operandum.getType());
    }

    public void type(String newType) throws Exception {
        if (newType == null) {
            throw new Exception("New Type Class is null");
        }
        try {
            if (runtimeTypeClass != null && runtimeTypeClass.equalsIgnoreCase("java.lang.String") == true) {
                runtimeTypeClass = "java.lang.String";
            } else if (runtimeTypeClass != null && runtimeTypeClass.equalsIgnoreCase(newType) == false) {
                if (newType.equalsIgnoreCase("java.lang.String")) {
                    runtimeTypeClass = "java.lang.String";
                } else if (newType.equalsIgnoreCase("java.lang.Integer")
                        || newType.equalsIgnoreCase("java.lang.Long")
                        || newType.equalsIgnoreCase("java.lang.Double")
                        || newType.equalsIgnoreCase("java.lang.Float")) {
                    if (runtimeTypeClass.equalsIgnoreCase("java.sql.Date")
                            || runtimeTypeClass.equalsIgnoreCase("java.sql.Time")
                            || runtimeTypeClass.equalsIgnoreCase("java.sql.Timestamp")) {
                        runtimeTypeClass = "java.lang.String";
                    } else if (runtimeTypeClass.equalsIgnoreCase(newType) == false) {
                        if (newType.equalsIgnoreCase("java.lang.Integer")
                                || newType.equalsIgnoreCase("java.lang.Long")) {
                            if (runtimeTypeClass.equalsIgnoreCase("java.lang.Double")
                                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Float")) {
                                runtimeTypeClass = "java.lang.Double";
                            } else if ((newType.equalsIgnoreCase("java.lang.Integer")
                                    && runtimeTypeClass.equalsIgnoreCase("java.lang.Long"))
                                    || (newType.equalsIgnoreCase("java.lang.Long")
                                    && runtimeTypeClass.equalsIgnoreCase("java.lang.Integer"))) {
                                runtimeTypeClass = "java.lang.Long";
                            } else {
                                throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
                            }
                        } else if (newType.equalsIgnoreCase("java.lang.Float")
                                || newType.equalsIgnoreCase("java.lang.Double")) {
                            if (runtimeTypeClass.equalsIgnoreCase("java.lang.Long")
                                    || runtimeTypeClass.equalsIgnoreCase("java.lang.Integer")) {
                                runtimeTypeClass = "java.lang.Long";
                            } else if ((newType.equalsIgnoreCase("java.lang.Float")
                                    && runtimeTypeClass.equalsIgnoreCase("java.lang.Double"))
                                    || (newType.equalsIgnoreCase("java.lang.Double")
                                    && runtimeTypeClass.equalsIgnoreCase("java.lang.Float"))) {
                                runtimeTypeClass = "java.lang.Double";
                            } else {
                                throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
                            }
                        } else {
                            throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
                        }
                    } else {
                        throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
                    }
                } else if (newType.equalsIgnoreCase("java.sql.Date")
                        || newType.equalsIgnoreCase("java.sql.Time")
                        || newType.equalsIgnoreCase("java.sql.Timestamp")) {
                    if (runtimeTypeClass.equalsIgnoreCase("java.lang.Integer")
                            || runtimeTypeClass.equalsIgnoreCase("java.lang.Long")
                            || runtimeTypeClass.equalsIgnoreCase("java.lang.Double")
                            || runtimeTypeClass.equalsIgnoreCase("java.lang.Float")) {
                        runtimeTypeClass = "java.lang.String";
                    } else if (runtimeTypeClass.equalsIgnoreCase(newType) == false) {
                        runtimeTypeClass = "java.sql.Timestamp";
                    } else {
                        throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
                    }
                } else {
                    throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
                }
            } else if (runtimeTypeClass == null) {
                runtimeTypeClass = newType;
            } else if (runtimeTypeClass.equalsIgnoreCase(newType) == true) {//ignore
            } else {
                throw new Exception("Undefined runtimeTypeClass '" + runtimeTypeClass + "' behaviour for newTypeClass '" + newType + "'");
            }
        } catch (Exception exception) {
            throw exception;
        }
    }
}
