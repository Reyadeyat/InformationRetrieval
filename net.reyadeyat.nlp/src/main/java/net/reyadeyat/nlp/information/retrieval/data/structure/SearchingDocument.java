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
public class SearchingDocument {
    public Integer document_id;
    public String document_name;
    public Integer document_book_id;
    public String document_book_name;
    public String document_book_lang;
    public Integer document_book_release_year;
    public String document_book_metadata;
    public String document_metadata;
    public Integer document_word_count;
    public String document_color;
    
    public SearchingDocument(Integer document_id, String document_name, Integer document_book_id, String document_book_name, String document_book_metadata, String document_book_lang, Integer document_book_release_year, String document_metadata, Integer document_word_count, String document_color) {
        this.document_id = document_id;
        this.document_name = document_name;
        this.document_book_id = document_book_id;
        this.document_book_name = document_book_name;
        this.document_book_metadata = document_book_metadata;
        this.document_book_lang = document_book_lang;
        this.document_book_release_year = document_book_release_year;
        this.document_metadata = document_metadata;
        this.document_word_count = document_word_count;
        this.document_color = document_color;
    }
}
