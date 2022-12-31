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
public class Book {
    public Integer book_id;
    public String book_name;
    public String book_metadata;
    public String book_lang;
    public Integer book_release_year;
    
    public Book(Integer book_id, String book_name, String book_metadata, String book_lang, Integer book_release_year) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.book_metadata = book_metadata;
        this.book_lang = book_lang;
        this.book_release_year = book_release_year;
    }
}
