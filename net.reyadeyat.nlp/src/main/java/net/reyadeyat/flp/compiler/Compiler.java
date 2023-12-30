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
public class Compiler {
    
    HashMap<Operandum, ArrayList<Syntax>> syntaxTree;
    
    public Compiler() throws Exception {

        Token.init();
        
        Token.addKeywords(new String[] {
            "select", "insert", "update", "delete", "distinct",
            "count", "sum", "as", "if",
            "from", "inner", "outer", "left", "right", "join", "on",
            "where", "order", "by", "group", "having", "asc", "desc",
            "limit", "skip", "first", "offset", "unique",
            "ADD CONSTRAINT",


        });

        Token.addFunctions(new String[] {
            "count", "sum", "replace", "trunc", "truncate", "nvl", "date", "extend",
            "cast", "substr", "to_char", "to_date", "date_format",
            "cast", "trunc", "nvl"
        });
        
        Token.addMOperators(new String[] {
            "+", "-", "*", "/"
        });
        
        Token.addCOperators(new String[] {
            "between", "in", "like", "and", "or", "<>", "="
        });
    }

    public void lex() throws Exception {
        String code = "SELECT 500 '%c''%' as Chapter,\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status IN ('new','assigned') ) AS 'New',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='document_interface' ) AS 'Document\\\n" +
" Interface',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='interface_development' ) AS 'Inter\\\n" +
"face Development',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='interface_check' ) AS 'Interface C\\\n" +
"heck',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='document_routine' ) AS 'Document R\\\n" +
"outine',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='full_development' ) AS 'Full Devel\\\n" +
"opment',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='peer_review_1' ) AS 'Peer Review O\\\n" +
"ne',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%'AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='peer_review_2' ) AS 'Peer Review Tw\\\n" +
"o',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='qa' ) AS 'QA',\n" +
"(SELECT count(ticket.id) AS Matches FROM engine.ticket INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%'AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine' AND ticket.status='closed' ) AS 'Closed',\n" +
"count(id) AS Total,\n" +
"ticket.id AS _id\n" +
"FROM engine.ticket\n" +
"INNER JOIN engine.ticket_custom ON ticket.id = ticket_custom.ticket\n" +
"WHERE ticket_custom.name='chapter' AND ticket_custom.value LIKE '%c%' AND type='New material' AND milestone='1.1.12' AND component NOT LIKE 'internal_engine'";
        
        long t1, t2;
        t1 = System.nanoTime();
        Scanner lexer = new Scanner(code, Scanner.INFORMIX_MYSQL);
        t2 = System.nanoTime();
        System.out.println("Lex = " + ((t2 - t1)/1000000d) + " milliseconds");
        System.out.println("------------------------------------------------------------------------------");
        for (int i = 0; i < lexer.tokens.size(); i++) {
            Token token = lexer.tokens.get(i);
            System.out.println(token);
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Compiler compiler = new Compiler();
            compiler.lex();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

}
