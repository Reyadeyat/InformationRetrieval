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

import java.util.Stack;

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
public class Operand implements Operandum, Cloneable, Comparable<Operand> {

    public enum OPERAND {
        FIELD(100), VARIABLE(200), STRUCTURE(300), BOOLEAN(1), BYTE(2), SHORT(2), INTEGER(4), FLOAT(4), LONG(8), DOUBLE(8), UDATE(1000), DATE(1001), TIME(1002), TIMESTAMP(1003), RUNTIME(8888), STRING(9999);
        private final int value;

        OPERAND(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };
    public OPERAND operand;
    public Boolean interpret;
    public String typeClass;
    public String source_code;
    public String runtime_code;
    public Object runtime_value;

    public Operand(OPERAND operand, String source_code) throws Exception {
        interpret = false;
        this.operand = operand;
        this.source_code = source_code;
        this.typeClass = operand(operand);
        typeClass(this.typeClass);
    }

    public Operand(OPERAND operand, String source_code, String typeClass) throws Exception {
        interpret = false;
        this.operand = operand;
        this.source_code = source_code;
        this.typeClass = typeClass;
        typeClass(this.typeClass);
    }

    public Operand(OPERAND operand, String source_code, String runtime_code, String typeClass) throws Exception {
        interpret = false;
        this.operand = operand;
        this.source_code = source_code;
        this.runtime_code = runtime_code;
        this.typeClass = typeClass;
        typeClass(this.typeClass);
    }

    @Override
    public Operand clone() throws CloneNotSupportedException {
        //Operand operand = (Operand) super.clone();
        //all members are shallow copies with same references
        //clonedObj.city = this.city.clone();//deep copy
        return (Operand) super.clone();
    }

    static Operand toOperand(Operator operator) throws Exception {
        return new Operand(OPERAND.RUNTIME, operator.runtime_code, operator.runtime_code, operator.runtimeTypeClass);
    }

    @Override
    public String evaluate(Stack<Operandum> stack, Stack<Operator> device, Stack<Operand> register) throws Exception {
        if (interpret == false && runtime_code != null) {
            return runtime_code;
        }
        StringBuilder b = new StringBuilder();
        OPERAND oprnd = operand;
        if (oprnd == OPERAND.FIELD
                || oprnd == OPERAND.VARIABLE
                || oprnd == OPERAND.RUNTIME) {
            b.append(source_code);
        } else {
            if (oprnd == OPERAND.STRING) {
                //b.append("new String(\"").append(source_code).append("\")");
                b.append("\"").append(source_code).append("\"");
            } else if (oprnd == OPERAND.BYTE) {
                b.append("Byte.valueOf(\"").append(source_code).append("\")");
            } else if (oprnd == OPERAND.SHORT) {
                b.append("Short.valueOf(\"").append(source_code).append("\")");
            } else if (oprnd == OPERAND.INTEGER) {
                b.append("Integer.valueOf(\"").append(source_code).append("\")");
            } else if (oprnd == OPERAND.LONG) {
                b.append("Long.valueOf(\"").append(source_code).append("\")");
            } else if (oprnd == OPERAND.FLOAT) {
                b.append("Float.valueOf(\"").append(source_code).append("\")");
            } else if (oprnd == OPERAND.DOUBLE) {
                b.append("Double.valueOf(\"").append(source_code).append("\")");
            } else if (oprnd == OPERAND.UDATE) {
                b.append(source_code);
            } else if (oprnd == OPERAND.DATE) {
                b.append("java.sql.Date.from(").append(source_code).append(")");
            } else if (oprnd == OPERAND.TIME) {
                b.append("java.sql.Time.from(").append(source_code).append(")");
            } else if (oprnd == OPERAND.TIMESTAMP) {
                b.append("java.sql.Timestamp.from(").append(source_code).append(")");
            } else if (oprnd == OPERAND.RUNTIME) {
                b.append(source_code);
            } else {
                throw new Exception("Undefined typeClass '" + typeClass + "'");
            }
        }
        runtime_code = b.toString();
        return runtime_code;
    }

    public String cast(String newTypeClass) throws Exception {
        StringBuilder b = new StringBuilder();

        if (newTypeClass == "java.lang.String" && typeClass.equalsIgnoreCase("java.lang.String") == true) {
            b.append(runtime_code);
        } else if (newTypeClass == "java.lang.String" && typeClass.equalsIgnoreCase("java.lang.String") == false) {
            b.append(runtime_code).append("toString()");
        } else if (newTypeClass == "java.lang.Byte") {
            if (typeClass.equalsIgnoreCase("java.lang.String") == true) {
                b.append("Byte.valueOf(\"").append(runtime_code).append("\")");
            } else {
                b.append("Byte.valueOf(").append(runtime_code).append(")");
            }
        } else if (newTypeClass == "java.lang.Short") {
            if (typeClass.equalsIgnoreCase("java.lang.String") == true) {
                b.append("Short.valueOf(\"").append(runtime_code).append("\")");
            } else {
                b.append("Short.valueOf(").append(runtime_code).append(")");
            }
        } else if (newTypeClass == "java.lang.Integer") {
            if (typeClass.equalsIgnoreCase("java.lang.String") == true) {
                b.append("Integer.valueOf(\"").append(runtime_code).append("\")");
            } else {
                b.append("Integer.valueOf(").append(runtime_code).append(")");
            }
        } else if (newTypeClass == "java.lang.Long") {
            if (typeClass.equalsIgnoreCase("java.lang.String") == true) {
                b.append("Long.valueOf(\"").append(runtime_code).append("\")");
            } else {
                b.append("Long.valueOf(").append(runtime_code).append(")");
            }
        } else if (newTypeClass == "java.lang.Float") {
            if (typeClass.equalsIgnoreCase("java.lang.String") == true) {
                b.append("Float.valueOf(\"").append(runtime_code).append("\")");
            } else {
                b.append("Float.valueOf(").append(runtime_code).append(")");
            }
        } else if (newTypeClass == "java.lang.Double") {
            if (typeClass.equalsIgnoreCase("java.lang.String") == true) {
                b.append("Double.valueOf(\"").append(runtime_code).append("\")");
            } else {
                b.append("Double.valueOf(").append(runtime_code).append(")");
            }
        } else {
            throw new Exception("Undefined typeClass '" + typeClass + "'");
        }

        return b.toString();
    }

    public Operand typeClass(String typeClass) throws Exception {
        this.typeClass = typeClass;
        if (operand == OPERAND.FIELD
                || operand == OPERAND.VARIABLE
                || operand == OPERAND.RUNTIME) {
            return this;
        }
        if (typeClass.equalsIgnoreCase("java.lang.String")) {
            operand = OPERAND.STRING;
        } else if (typeClass.equalsIgnoreCase("java.lang.Byte")) {
            operand = OPERAND.BYTE;
        } else if (typeClass.equalsIgnoreCase("java.lang.Short")) {
            operand = OPERAND.SHORT;
        } else if (typeClass.equalsIgnoreCase("java.lang.Integer")) {
            operand = OPERAND.INTEGER;
        } else if (typeClass.equalsIgnoreCase("java.lang.Long")) {
            operand = OPERAND.LONG;
        } else if (typeClass.equalsIgnoreCase("java.lang.Float")) {
            operand = OPERAND.FLOAT;
        } else if (typeClass.equalsIgnoreCase("java.lang.Double")) {
            operand = OPERAND.DOUBLE;
        } else if (typeClass.equalsIgnoreCase("java.util.Date")) {
            operand = OPERAND.UDATE;
        } else if (typeClass.equalsIgnoreCase("java.sql.Date")) {
            operand = OPERAND.DATE;
        } else if (typeClass.equalsIgnoreCase("java.sql.Time")) {
            operand = OPERAND.DATE;
        } else if (typeClass.equalsIgnoreCase("java.sql.Timestamp")) {
            operand = OPERAND.DATE;
        } else {
            throw new Exception("Undefined typeClass '" + typeClass + "'");
        }

        return this;
    }

    static public OPERAND operand(String typeClass) throws RuntimeException {
        if (typeClass.equalsIgnoreCase("java.lang.String")) {
            return OPERAND.STRING;
        } else if (typeClass.equalsIgnoreCase("java.lang.Byte")) {
            return OPERAND.BYTE;
        } else if (typeClass.equalsIgnoreCase("java.lang.Short")) {
            return OPERAND.SHORT;
        } else if (typeClass.equalsIgnoreCase("java.lang.Integer")) {
            return OPERAND.INTEGER;
        } else if (typeClass.equalsIgnoreCase("java.lang.Long")) {
            return OPERAND.LONG;
        } else if (typeClass.equalsIgnoreCase("java.lang.Float")) {
            return OPERAND.FLOAT;
        } else if (typeClass.equalsIgnoreCase("java.lang.Double")) {
            return OPERAND.DOUBLE;
        } else if (typeClass.equalsIgnoreCase("java.util.Date")) {
            return OPERAND.UDATE;
        } else if (typeClass.equalsIgnoreCase("java.sql.Date")) {
            return OPERAND.DATE;
        } else if (typeClass.equalsIgnoreCase("java.sql.Time")) {
            return OPERAND.TIME;
        } else if (typeClass.equalsIgnoreCase("java.sql.Timestamp")) {
            return OPERAND.TIMESTAMP;
        } else {
            throw new RuntimeException("Undefined typeClass '" + typeClass + "'");
        }
    }

    static public String operand(OPERAND operand) throws Exception {
        if (operand == OPERAND.STRING) {
            return "java.lang.String";
        } else if (operand == OPERAND.BYTE) {
            return "java.lang.Byte";
        } else if (operand == OPERAND.SHORT) {
            return "java.lang.Short";
        } else if (operand == OPERAND.INTEGER) {
            return "java.lang.Integer";
        } else if (operand == OPERAND.LONG) {
            return "java.lang.Long";
        } else if (operand == OPERAND.FLOAT) {
            return "java.lang.Float";
        } else if (operand == OPERAND.DOUBLE) {
            return "java.lang.Double";
        } else if (operand == OPERAND.UDATE) {
            return "java.util.Date";
        } else if (operand == OPERAND.DATE) {
            return "java.sql.Date";
        } else if (operand == OPERAND.TIME) {
            return "java.sql.Time";
        } else if (operand == OPERAND.TIMESTAMP) {
            return "java.sql.Timestamp";
        } else if (operand == OPERAND.RUNTIME) {
            return "java.lang.Object";
        } else {
            throw new Exception("Undefined Operand '" + operand + "'");
        }
    }

    static public String defaultValue(OPERAND operand) throws Exception {
        if (operand == OPERAND.STRING) {
            return "new String(\"-\")";
        } else if (operand == OPERAND.BYTE) {
            return "Byte.valueOf((byte)0)";
        } else if (operand == OPERAND.SHORT) {
            return "Short.valueOf((short)0)";
        } else if (operand == OPERAND.INTEGER) {
            return "Integer.valueOf(0)";
        } else if (operand == OPERAND.LONG) {
            return "Long.valueOf(0)";
        } else if (operand == OPERAND.FLOAT) {
            return "Float.valueOf((float)0.0)";
        } else if (operand == OPERAND.DOUBLE) {
            return "Double.valueOf(0.0)";
        } else if (operand == OPERAND.UDATE) {
            return "java.util.Date.from(java.time.Instant.now())";
        } else if (operand == OPERAND.DATE) {
            return "java.sql.Date.from(java.time.Instant.now())";
        } else if (operand == OPERAND.TIME) {
            return "java.sql.Time.from(java.time.Instant.now())";
        } else if (operand == OPERAND.TIMESTAMP) {
            return "java.sql.Timestamp.from(java.time.Instant.now())";
            /*} else if (operand == OPERAND.RUNTIME) {
            return null;*/
        } else {
            throw new Exception("Undefined Operand '" + operand + "'");
        }
    }

    @Override
    public Boolean isOperator() {
        return false;
    }

    @Override
    public Boolean isOperator(Operator.OPERATION operation) {
        return false;
    }

    @Override
    public Boolean isOperand() {
        return true;
    }

    @Override
    public Boolean isOperator(Operator.OPERATOR operator) {
        return false;
    }

    @Override
    public Boolean isOperand(Operand.OPERAND operand) {
        return this.operand == operand;
    }

    @Override
    public Operator getOperator() {
        return null;
    }

    @Override
    public Operand getOperand() {
        return this;
    }

    @Override
    public String getType() {
        return typeClass;
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
            b.append("-");
        }
        b.append("Operand: ").append(operand).append(" {" + (source_code == null ? "" : source_code) + "}{" + (runtime_code == null ? "" : runtime_code) + "}(" + (typeClass == null ? "" : typeClass) + ")");
        return b.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Operand: ").append(operand).append(" {" + (source_code == null ? "" : source_code) + "}{" + (runtime_code == null ? "" : runtime_code) + "}(" + (typeClass == null ? "" : typeClass) + ")").toString();
    }

    @Override
    public int compareTo(Operand o) {
        OPERAND opr = o.operand;
        if (opr == OPERAND.FIELD
                || opr == OPERAND.VARIABLE
                || opr == OPERAND.STRUCTURE
                || opr == OPERAND.RUNTIME) {
            opr = operand(o.typeClass);
        }
        if (operand.getValue() > opr.getValue()) {
            return 1;
        } else if (operand.getValue() < opr.getValue()) {
            return -1;
        }
        return 0;
    }
}
