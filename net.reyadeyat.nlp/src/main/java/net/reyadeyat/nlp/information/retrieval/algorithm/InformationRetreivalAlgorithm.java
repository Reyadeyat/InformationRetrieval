/*
 * Copyright (C) 2022 Reyadeyat
 *
 * Reyadeyat/NLP is licensed under the
 * BSD 3-Clause "New" or "Revised" License
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://reyadeyat.net/NLP.LICENSE  
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.reyadeyat.nlp.information.retrieval.algorithm;

import net.reyadeyat.nlp.information.retrieval.data.structure.Citation;
import net.reyadeyat.nlp.information.retrieval.data.structure.Sentence;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2023.01.01
 */
public interface InformationRetreivalAlgorithm {
    public Boolean isMatch();
    public ArrayList<ArrayList<Word>> getResultWordList();
    public ArrayList<Sentence> getResultSentenceList();
    public ArrayList<Citation> getCitationList(Connection connection) throws Exception;
}
