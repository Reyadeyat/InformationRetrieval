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
public interface Operandum {
    public Boolean isOperator();
    public Boolean isOperand();
    public Boolean isOperator(Operator.OPERATOR operator);
    public Boolean isOperator(Operator.OPERATION operation);
    public Boolean isOperand(Operand.OPERAND operand);
    public Operator getOperator();
    public Operand getOperand();
    public String getType();
    public String evaluate(Stack<Operandum> stack, Stack<Operator> device, Stack<Operand> register) throws Exception;
    public String toString(Integer level, Integer shift);
}
