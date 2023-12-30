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

import net.reyadeyat.flp.compiler.Operator.*;
import net.reyadeyat.flp.compiler.Operand.*;
import java.util.ArrayList;
import java.util.HashMap;
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
public class Expression implements Operandum {
    
    
    final static public Integer SELECT = 0;
    final static public Integer INSERT = 1;
    final static public Integer UPDATE = 2;
    final static public Integer DELETE = 3;
    final static public Integer FUNCTION = 4;
    final static public Integer FIELD = 5;
    final static public Integer FROM = 6;
    final static public Integer TABLE = 7;
    final static public Integer WHERE = 8;
    final static public Integer HAVING = 9;
    final static public Integer MOPERATOR = 10;
    final static public Integer COPERATOR = 11;
    final static public Integer GROUP_BY = 12;
    final static public Integer ORDER_BY = 13;
    final static public Integer LIMIT = 14;
    final static public Integer OFFSET = 15;
    
    private Integer type;
    private Integer id;
    
    final static private HashMap<Integer, String> typeByID = new HashMap<>();
    final static private HashMap<String, Integer> typeByName = new HashMap<>();
    
    Boolean terminal = true;
    
    private Token token;
    private Expression parent;
    private ArrayList<Expression> children;
    private ArrayList<Token> tokens;
    
    public Expression(Expression parent, Integer type, Integer id, Scanner lexer, Token token) {
        this.token = token;
        this.parent = parent;
        this.type = type;
        this.id = id;
        tokens = new ArrayList<>();
        
        if (typeByID != null) {
            typeByID.put(SELECT, "SELECT");
            typeByID.put(INSERT, "INSERT");
            typeByID.put(UPDATE, "UPDATE");
            typeByID.put(DELETE, "DELETE");
            typeByID.put(FUNCTION, "FUNCTION");
            typeByID.put(FIELD, "FIELD");
            typeByID.put(FROM, "FROM");
            typeByID.put(TABLE, "TABLE");
            typeByID.put(WHERE, "WHERE");
            typeByID.put(HAVING, "HAVING");
            typeByID.put(MOPERATOR, "MOPERATOR");
            typeByID.put(COPERATOR, "COPERATOR");
            typeByID.put(GROUP_BY, "GROUP_BY");
            typeByID.put(ORDER_BY, "ORDER_BY");
            typeByID.put(LIMIT, "LIMIT");
            typeByID.put(OFFSET, "OFFSET");
        }
        
        if (typeByName != null) {
            typeByName.put("SELECT", SELECT);
            typeByName.put("INSERT", INSERT);
            typeByName.put("UPDATE", UPDATE);
            typeByName.put("DELETE", DELETE);
            typeByName.put("FUNCTION", FUNCTION);
            typeByName.put("FIELD", FIELD);
            typeByName.put("FROM", FROM);
            typeByName.put("TABLE", TABLE);
            typeByName.put("WHERE", WHERE);
            typeByName.put("HAVING", HAVING);
            typeByName.put("MOPERATOR", MOPERATOR);
            typeByName.put("COPERATOR", COPERATOR);
            typeByName.put("GROUP_BY", GROUP_BY);
            typeByName.put("ORDER_BY", ORDER_BY);
            typeByName.put("LIMIT", LIMIT);
            typeByName.put("OFFSET", OFFSET);
        }
        
        Token ctoken;//child token
        if (token != null) {
            if (token.isClause()) {
                buildClause();
            } else if (token.isFunction()) {
//                buidlFunction();
            } else if (token.isMOperator()) {
//                buidlMOperator();
            } else if (token.isFunction()) {
//                buidlCOperator();
            }
        }
    }
    
    public String getType(Integer type) {
        return typeByID.get(type);
    }
    
    public Integer getType(String type) {
        return typeByName.get(type);
    }
    
    private void buildClause() {
        //Select Clause
        //Field Segments
        //Function
        //Field
        //SKIP
        //FIRST
        
        //FROM Clause
        //Table Segments
        //(SQL) AS Table
        //Table AS Table
        //Table
        
        //WHERE
        //Condition Segments
        //FUNCTION = FIELD
        
        //GROUP BY
        //Field Segments
        
        //HAVING
        //Condition Segments
        
        //ORDER BY
        //Field Segments
        
        //LIMIT
        
        
    }
    
    

    @Override
    public Boolean isOperator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isOperand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isOperator(OPERATOR operator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isOperator(OPERATION operation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isOperand(OPERAND operand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Operator getOperator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Operand getOperand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String evaluate(Stack<Operandum> stack, Stack<Operator> device, Stack<Operand> register) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString(Integer level, Integer shift) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
