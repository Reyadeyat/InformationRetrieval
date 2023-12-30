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
public class Statement {
    public ArrayList<Operator> statements;
    
    public Statement() {
        statements = new ArrayList<Operator>();
    }
    
    public void add(Operator operator) {
        statements.add(operator);
    }
    public Operator get(int index) {
        return statements.get(index);
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Operator operator : statements) {
            b.append("\n").append(operator.toString(0,4));
        }
        return b.toString();
    }
}
