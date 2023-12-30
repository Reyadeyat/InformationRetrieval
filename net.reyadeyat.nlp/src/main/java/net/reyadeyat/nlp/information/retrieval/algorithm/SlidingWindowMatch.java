/*
 * Copyright (C) 2023 Reyadeyat
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
public class SlidingWindowMatch {
    private Integer[] phrase;
    private Integer[] text;
    private Integer[] hash;
    private ArrayList<Integer> result;
    
    public SlidingWindowMatch(Integer[] phrase, Integer[] text) {
        this.phrase = phrase;
        this.text = text;
        hash = new Integer[phrase.length];
        result = new ArrayList<Integer>();
        if (text.length < phrase.length) {
            return;
        }
        Integer match = -1;
        for (int i = 0; i <= text.length - phrase.length; i++) {
            for (int x = 0; x < phrase.length; x++) {
                hash[x] = phrase[x] - text[x+i] == 0 ? 1 : 0;
            }
            match = i;
            for (int x = 0; x < phrase.length; x++) {
                if (hash[x] == 0) {
                    match = -1;
                    break;
                }
            }
            if (match != -1) {
                result.add(match);
                i += phrase.length - 1;
            }
        }
    }
    
    public Boolean isMatch() {
        return result.size() > 0;
    }
    
    public ArrayList<Integer> getMatchResult() {
        return result;
    }
}
