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

package net.reyadeyat.nlp.information.retrieval;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import net.reyadeyat.nlp.information.retrieval.algorithm.Match;
import net.reyadeyat.nlp.information.retrieval.data.structure.Book;
import net.reyadeyat.nlp.information.retrieval.data.structure.Citation;
import net.reyadeyat.nlp.information.retrieval.data.structure.FileIndexing;
import net.reyadeyat.nlp.information.retrieval.data.structure.ParsingDocument;
import net.reyadeyat.nlp.information.retrieval.data.structure.SearchingDocument;
import net.reyadeyat.nlp.information.retrieval.parser.InformationRetrievalParser;
import net.reyadeyat.nlp.information.retrieval.plagiarism.PlagiarismChecker;
import net.reyadeyat.nlp.json.JsonResultset;

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
public class API {

    public static void documentIndex(Connection jdbc_connection, Integer indexing_buffer_size) throws Exception {
        ArrayList<ParsingDocument> parsing_document_list = new ArrayList<>();
        String select_statement = "SELECT `book`.`book_id`, `book`.`book_name`, `book`.`book_metadata`, `document`.`document_id`, `document`.`document_name`, `document`.`document_metadata`, `document`.`document_word_count`, `document`.`document_color`, `document`.`document_parser_class_name` FROM `book` INNER JOIN `document` ON `book`.`book_id`=`document`.`document_book_id` WHERE `document`.`document_indexed`=?";
        try (PreparedStatement prepared_stmt = jdbc_connection.prepareStatement(select_statement)) {
            prepared_stmt.setBoolean(1, false);
            try (ResultSet resultset = prepared_stmt.executeQuery()) {
                parsing_document_list = JsonResultset.resultset(resultset, ParsingDocument.class);
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }

        for (ParsingDocument parsing_document : parsing_document_list) {
            try {
                InformationRetrievalParser irp = InformationRetrieval.getParserInstance(parsing_document, indexing_buffer_size);
                InformationRetrieval ir = new InformationRetrieval(jdbc_connection, irp);
                ir.indexDocument();
            } catch (Exception ex) {
                throw ex;
            }
        }
    }
    
    public static void documentRemove(Connection jdbc_connection, Integer book_id, Integer document_id) throws Exception {
        InformationRetrieval.removeBookDocument(jdbc_connection, document_id, book_id);
    }
    
    public static void documentRemoveList(Connection jdbc_connection, Integer book_id, ArrayList<Integer> document_id_list) throws Exception {
        for (int i = 0; i < document_id_list.size(); i++) {
            Integer document_id = document_id_list.get(i);
            InformationRetrieval.removeBookDocument(jdbc_connection, document_id, book_id);
        }
    }
    
    public static Integer bookInsert(Connection jdbc_connection, String book_name, String book_metadata, String book_lang, Integer book_release_year) throws Exception {
        Integer book_id = InformationRetrieval.createBookIfNotExists(jdbc_connection, book_name, book_metadata, book_lang, book_release_year);
        return book_id;
    }
    
    public static Book bookSelect(Connection jdbc_connection, Integer book_id) throws Exception {
        Book book = InformationRetrieval.selectBook(jdbc_connection, book_id);
        return book;
    }
    
    public static ArrayList<Book> bookSelectList(Connection jdbc_connection, ArrayList<Integer> book_id_list) throws Exception {
        ArrayList<Book> book_list = InformationRetrieval.selectBookList(jdbc_connection, book_id_list);
        return book_list;
    }
    
    public static ArrayList<SearchingDocument>  documentListSelect(Connection jdbc_connection, Integer book_id) throws Exception {
        ArrayList<SearchingDocument> searching_document_list = InformationRetrieval.selectBookSearchingDocumentList(jdbc_connection, book_id);
        return searching_document_list;
    }
    
    public static Integer documentFileInexing(Connection jdbc_connection, Integer document_book_id, String document_name, String document_metadata, String document_color, String document_parser_class_name, Reader document_reader) throws Exception {
        
        Book book = InformationRetrieval.selectBook(jdbc_connection, document_book_id);
        String document_book_name = book.book_name;
        String document_book_metadata = book.book_metadata;
        String document_book_lang = book.book_lang;
        Integer document_book_release_year = book.book_release_year;
        
        ParsingDocument parsing_document = new ParsingDocument(document_book_id, document_book_name, document_book_metadata, document_book_lang, document_book_release_year, document_name, document_metadata, document_color, document_parser_class_name);

        Integer document_id = InformationRetrieval.createBookDocument(jdbc_connection, parsing_document, document_reader);
        
        return document_id;
    }
    
    public static ArrayList<SearchingDocument> documentFileListInexing(Connection jdbc_connection,
        Integer document_book_id, String document_metadata, String document_color, String document_parser_class_name, ArrayList<FileIndexing> file_indexing_list) throws Exception {
        
        Book book = InformationRetrieval.selectBook(jdbc_connection, document_book_id);
        String document_book_name = book.book_name;
        String document_book_metadata = book.book_metadata;
        String document_book_lang = book.book_lang;
        Integer document_book_release_year = book.book_release_year;
        
        ArrayList<SearchingDocument> document_list = new ArrayList<>();
        for (FileIndexing file_indexing : file_indexing_list) {
            ParsingDocument parsing_document = new ParsingDocument(document_book_id, document_book_name, document_book_metadata, document_book_lang, document_book_release_year, file_indexing.document_name, document_metadata, document_color, document_parser_class_name);
            Integer document_id = InformationRetrieval.createBookDocument(jdbc_connection, parsing_document, file_indexing.document_reader);
            SearchingDocument searching_document = InformationRetrieval.selectSearchingDocument(jdbc_connection, document_id);
            document_list.add(searching_document);
        }
        
        return document_list;
    }
    
    public static Integer documentTextInexing(Connection jdbc_connection, Integer document_book_id, String document_name, String document_metadata, String document_color, String document_parser_class_name, String document_raw_text) throws Exception {
        Book book = InformationRetrieval.selectBook(jdbc_connection, document_book_id);
        String document_book_name = book.book_name;
        String document_book_metadata = book.book_metadata;
        String document_book_lang = book.book_lang;
        Integer document_book_release_year = book.book_release_year;
        
        ParsingDocument parsing_document = new ParsingDocument(document_book_id, document_book_name, document_book_metadata, document_book_lang, document_book_release_year, document_name, document_metadata, document_color, document_parser_class_name);
        Reader document_reader = new StringReader(document_raw_text);
        Integer document_id = InformationRetrieval.createBookDocument(jdbc_connection, parsing_document, document_reader);
        
        return document_id;
    }
    
    public static ArrayList<Citation> plagiarismChecker(Connection jdbc_connection, String algorithm, Integer words_before_count, Integer words_after_count, String phrase) throws Exception {
        ArrayList<Citation> citation_result_set = new ArrayList<>();
        PlagiarismChecker plagiarism_checker = new PlagiarismChecker(jdbc_connection, algorithm, words_before_count, words_after_count, phrase);
        ArrayList<Match> match_list = plagiarism_checker.getMatchList();
        for (int i = 0; i < match_list.size(); i++) {
            Match match = match_list.get(i);
            ArrayList<Citation> citation_list = match.getCitationList(jdbc_connection);
            for (Citation citation : citation_list) {
                citation_result_set.add(citation);
            }
        }
        return citation_result_set;
    }
    
    public static ArrayList<Citation> plagiarismListChecker(Connection jdbc_connection, String algorithm, Integer words_before_count, Integer words_after_count, ArrayList<String> phrase_list) throws Exception {
        ArrayList<Citation> citation_result_set = new ArrayList<>();
        for (int x = 0; x < phrase_list.size(); x++) {
            String phrase = phrase_list.get(x);
            PlagiarismChecker plagiarism_checker = new PlagiarismChecker(jdbc_connection, algorithm, words_before_count, words_after_count, phrase);
            ArrayList<Match> match_list = plagiarism_checker.getMatchList();
            for (int i = 0; i < match_list.size(); i++) {
                Match match = match_list.get(i);
                ArrayList<Citation> citation_list = match.getCitationList(jdbc_connection);
                for (Citation citation : citation_list) {
                    citation_result_set.add(citation);
                }
            }
        }
        return citation_result_set;
    }
}
