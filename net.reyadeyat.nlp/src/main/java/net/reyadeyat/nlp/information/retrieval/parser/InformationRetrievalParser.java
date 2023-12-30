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

package net.reyadeyat.nlp.information.retrieval.parser;

import net.reyadeyat.nlp.information.retrieval.data.structure.ParsingDocument;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import java.io.Reader;
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
public interface InformationRetrievalParser {
    public ParsingDocument getParsingDocument();
    public Integer getBookID();
    public String getBookName();
    public String getBookMetadata();
    public Integer getDocumentID();
    public String getDocumentName();
    public String getDocumentMetadata();
    public String getDocumentColor();
    public ArrayList<Word> getNextWords(Reader document_reader) throws Exception;
    public Boolean hasMoreWords();
}
