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

package net.reyadeyat.nlp.information.retrieval.data.structure;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2023.01.01
 */
public class Word {
    public Integer document_id;
    public String raw_word;
    public String word;
    public Integer word_index;
    public Integer word_start;
    public Integer word_length;
    public Integer search_index;
    
    public Word(Integer document_id, String raw_word, String word, Integer word_index, Integer word_start, Integer word_length, Integer search_index) {
        this.document_id = document_id;
        this.raw_word = raw_word;
        this.word = word;
        this.word_index = word_index;
        this.word_start = word_start;
        this.word_length = word_length;
        this.search_index = search_index;
    }
    
    public Word(String raw_word, String word, Integer word_index, Integer word_start, Integer word_length) {
        this(-1, raw_word, word, word_index, word_start, word_length, -1);
    }
    
    @Override
    public String toString() {
        return "["+document_id+"-"+word_index+"]@"+word_start+"."+word_length + " - " + word;
    }
}
