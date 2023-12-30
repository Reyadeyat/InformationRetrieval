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

import net.reyadeyat.nlp.information.retrieval.data.structure.Book;
import net.reyadeyat.nlp.information.retrieval.data.structure.SearchingDocument;
import net.reyadeyat.nlp.information.retrieval.data.structure.ParsingDocument;
import net.reyadeyat.nlp.information.retrieval.parser.InformationRetrievalParser;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import net.reyadeyat.nlp.json.JsonResultset;
import net.reyadeyat.nlp.json.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

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
public class InformationRetrieval {
    
    private final static ArrayList<Character> ignore_char_list = new ArrayList<Character>(Arrays.asList(new Character[]{
        '\r', '\n', '\t',
        '\\','#',',','،','ـ','“','”','^','+','(',')','%','!','@','$','~','%','.','\'','\"',':','*','?','<','>','{','}','=','-'
    }));
    private final static ArrayList<Character> include_char_list = new ArrayList<Character>(Arrays.asList(new Character[]{
//        '\r', '\n', '\t'
//        '\\','#',',','،','ـ','“','”','^','+','(',')','%','!','@','$','~','%','.','\'','\"',':','*','?','<','>','{','}','=','-'
    }));
    private final static HashMap<Character, Character> substitute_char_list;
    static {
        substitute_char_list = new HashMap<Character, Character>();
        substitute_char_list.put('أ', 'ا');
        substitute_char_list.put('إ', 'ا');
        substitute_char_list.put('آ', 'ا');
        substitute_char_list.put('ء', 'ا');
        substitute_char_list.put('\u0670', 'ا');
        substitute_char_list.put('ة', 'ه');
        //substitute_char_list.put('ي', 'ى');
        substitute_char_list.put('ؤ', 'و');
    }
    private final static HashMap<Character, Character> substitute_end_char_list;
    static {
        substitute_end_char_list = new HashMap<Character, Character>();
        substitute_end_char_list.put('ي', 'ى');
        substitute_end_char_list.put('ئ', 'ى');
    }
    
    private Connection jdbc_connection;
    private InformationRetrievalParser document_parser;
    
    public InformationRetrieval(Connection jdbc_connection) throws Exception {
        this(jdbc_connection, null);
    }
    
    public InformationRetrieval(Connection jdbc_connection, InformationRetrievalParser document_parser) throws Exception {
        this.jdbc_connection = jdbc_connection;
        this.document_parser = document_parser;
    }
    
    public void indexDocument() throws Exception {
        String document_select = "SELECT `document`.`document_raw_text` FROM `document` WHERE `document`.`document_book_id`=? AND `document`.`document_id`=?";
        try (PreparedStatement select_document_stmt = jdbc_connection.prepareStatement(document_select)) {
            select_document_stmt.setInt(1, document_parser.getBookID());
            select_document_stmt.setInt(2, document_parser.getDocumentID());
            try (ResultSet rs = select_document_stmt.executeQuery()) {
                if (rs.next() == false) {
                    throw new Exception("Failed to select reader for indexing Document '"+document_parser.getDocumentName()+"' already exists in Book '"+document_parser.getBookName()+"'");
                }
                Reader document_reader = rs.getCharacterStream("document_raw_text");
                indexDocument(document_reader);
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public void indexDocument(Reader document_reader) throws Exception {
        if (this.document_parser == null) {
            throw new Exception("Error - document_parser instance is null");
        }
        ArrayList<Word> word_list;
        Integer document_word_count = 0;
        while (document_parser.hasMoreWords()) {
            word_list = document_parser.getNextWords(document_reader);
            document_word_count += word_list.size();
            for (int i = 0; i < word_list.size(); i++) {
                Word word = word_list.get(i);
                indexWordList(jdbc_connection, document_parser.getParsingDocument().document_id, word);
            }
        }
        
        String document_name_insert = "UPDATE `document` SET `document_word_count`=?, `document_indexed`=? WHERE `document_book_id`=? AND `document_id`=?";
        try (PreparedStatement update_document_stmt = jdbc_connection.prepareStatement(document_name_insert, Statement.RETURN_GENERATED_KEYS)) {
            update_document_stmt.setInt(1, document_word_count);
            update_document_stmt.setInt(2, 1);
            update_document_stmt.setInt(3, document_parser.getBookID());
            update_document_stmt.setInt(4, document_parser.getDocumentID());
            int rows_affected = update_document_stmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
        
        /*String lock_sql = "LOCK TABLES `word` WRITE";
        try (PreparedStatement lock_stmt = jdbc_connection.prepareStatement(lock_sql)) {
            if (lock_stmt.execute() == false) {
                throw new Exception("Can't lock table 'word' for write, solution synchronize your app!");
            }
        } catch (Exception ex) {
            throw ex;
        }*/
        
        String word_insert_sql = "INSERT INTO `word` (`word_text`) SELECT DISTINCT `document_word`.`document_word_text` FROM `document_word` WHERE `document_word_text` NOT IN (SELECT `word_text` FROM `word`)";
        try (PreparedStatement word_insert_stmt = jdbc_connection.prepareStatement(word_insert_sql)) {
            Integer affected_rows = word_insert_stmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
        
        String document_word_id_update_sql = "UPDATE `document_word` INNER JOIN `word` ON `document_word`.`document_word_text` = `word`.`word_text` AND `document_word`.`document_word_id` IS NULL SET `document_word`.`document_word_id` =`word`.`word_id`";
        try (PreparedStatement document_word_id_update_stmt = jdbc_connection.prepareStatement(document_word_id_update_sql)) {
            Integer affected_rows = document_word_id_update_stmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
        
        String document_word_conflect_sql = "SELECT `document_word_text`, `document_word_id`, `word_text`, `word_id` FROM `document_word` INNER JOIN `word` ON `document_word`.`document_word_text` <> `word`.`word_text` AND `document_word`.`document_word_id` = `word`.`word_id`";
        try (PreparedStatement document_word_id_update_stmt = jdbc_connection.prepareStatement(document_word_conflect_sql)) {
            ResultSet rs = document_word_id_update_stmt.executeQuery();
            JsonArray word_conflicts = JsonResultset.resultset(rs);
            if (word_conflicts != null && word_conflicts.size() > 0) {
                Gson gson = JsonUtil.gsonPretty();
                String word_conflicts_json_text = gson.toJson(word_conflicts);
                JsonUtil.reclaimGsonPretty(gson);
                throw new Exception("document_word to Word conflicts\n"+word_conflicts_json_text);
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        /*String unlock_sql = "UNLOCK TABLES `word`";
        try (PreparedStatement unlock_stmt = jdbc_connection.prepareStatement(unlock_sql)) {
            if (unlock_stmt.execute() == false) {
                throw new Exception("Can't unlock table 'word' for write, solution synchronize your app!");
            }
        } catch (Exception ex) {
            throw ex;
        }*/
    }
    
    public static Integer createBookIfNotExists(Connection jdbc_connection, String book_name, String book_metadata, String book_lang, Integer book_release_year) throws Exception {
        String document_book_name_select = "SELECT `book`.`book_id` FROM `book` WHERE `book_name`=?";
        try (PreparedStatement select_book_stmt = jdbc_connection.prepareStatement(document_book_name_select)) {
            select_book_stmt.setString(1, book_name);
            try (ResultSet rs = select_book_stmt.executeQuery()) {
                if (rs.next() == true) {
                    return rs.getInt(1);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        String document_book_name_insert = "INSERT INTO `book` (`book_name`, `book_metadata`, `book_lang`, `book_release_year`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insert_book_stmt = jdbc_connection.prepareStatement(document_book_name_insert, Statement.RETURN_GENERATED_KEYS)) {
            insert_book_stmt.setString(1, book_name);
            insert_book_stmt.setString(2, book_metadata);
            insert_book_stmt.setString(3, book_lang);
            insert_book_stmt.setInt(4, book_release_year);
            insert_book_stmt.executeUpdate();
            try (ResultSet generatedKeys = insert_book_stmt.getGeneratedKeys()) {
                if (generatedKeys.next() == true) {
                    return generatedKeys.getInt(1);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        throw new Exception("Failed to create Book '"+book_name+"'");
    }
    
    public static Book selectBook(Connection jdbc_connection, Integer book_id) throws Exception {
        ArrayList<Book> book_list = selectBookList(jdbc_connection, new ArrayList<Integer>(Arrays.asList(new Integer[] {book_id})));
        return book_list == null || book_list.size() == 0 ? null : book_list.get(0);
    }
    
    public static ArrayList<Book> selectBookList(Connection jdbc_connection, ArrayList<Integer> book_id_list) throws Exception {
        StringBuilder in_list = new StringBuilder();
        for (Integer book_id : book_id_list) {
            in_list.append(book_id).append(","); 
        }
        in_list.deleteCharAt(in_list.length()-1);
        String document_select = in_list.insert(0, "SELECT `book`.`book_id`, `book`.`book_name`, `book`.`book_metadata`, `book`.`book_lang`, `book`.`book_release_year` FROM `book` WHERE `book_id` IN (").append(")").toString();
        try (PreparedStatement select_document_stmt = jdbc_connection.prepareStatement(document_select)) {
            try (ResultSet rs = select_document_stmt.executeQuery()) {
                ArrayList<Book> book_list = JsonResultset.resultset(rs, Book.class);
                return book_list;
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static Integer createBookDocument(Connection jdbc_connection, ParsingDocument parsing_document, Reader document_reader) throws Exception {
        String document_name_select = "SELECT `document`.`document_id` FROM `document` WHERE `document`.`document_book_id`=? AND `document`.`document_name`=?";
        try (PreparedStatement select_document_stmt = jdbc_connection.prepareStatement(document_name_select)) {
            select_document_stmt.setInt(1, parsing_document.document_book_id);
            select_document_stmt.setString(2, parsing_document.document_name);
            try (ResultSet rs = select_document_stmt.executeQuery()) {
                if (rs.next() == true) {
                    //return rs.getInt(1);
                    throw new Exception("Failed to create Document '"+parsing_document.document_name+"' already exists in Book '"+parsing_document.document_book_name+"'");
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        String document_name_insert = "INSERT INTO `document` (`document_book_id`, `document_name`, `document_metadata`, `document_color`, `document_parser_class_name`, `document_raw_text`) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insert_document_stmt = jdbc_connection.prepareStatement(document_name_insert, Statement.RETURN_GENERATED_KEYS)) {
            insert_document_stmt.setInt(1, parsing_document.document_book_id);
            insert_document_stmt.setString(2, parsing_document.document_name);
            insert_document_stmt.setString(3, parsing_document.document_metadata);
            insert_document_stmt.setString(4, parsing_document.document_color);
            insert_document_stmt.setString(5, parsing_document.document_parser_class_name);
            insert_document_stmt.setCharacterStream(6, document_reader);
            insert_document_stmt.executeUpdate();
            try (ResultSet generatedKeys = insert_document_stmt.getGeneratedKeys()) {
                if (generatedKeys.next() == true) {
                    Integer document_id = generatedKeys.getInt(1);
                    parsing_document.setNewDocumentID(document_id);
                    return document_id;
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        throw new Exception("Failed to create Document '"+parsing_document.document_name+"' in Book '"+parsing_document.document_book_name+"'");
    }
    
    public static void removeBookDocument(Connection jdbc_connection, Integer document_id, Integer document_book_id) throws Exception {
        String document_word_delete = "DELETE FROM `document_word` WHERE document_id=?";
        try (PreparedStatement delete_document_word_stmt = jdbc_connection.prepareStatement(document_word_delete)) {
            delete_document_word_stmt.setInt(1, document_id);
            delete_document_word_stmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
        
        String document_delete = "DELETE FROM `document` WHERE document_id=? AND document_book_id=?";
        try (PreparedStatement delete_document_stmt = jdbc_connection.prepareStatement(document_delete)) {
            delete_document_stmt.setInt(1, document_id);
            delete_document_stmt.setInt(2, document_book_id);
            delete_document_stmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static ParsingDocument selectParsingDocument(Connection jdbc_connection, Integer parsing_document_id) throws Exception {
        String document_select = "SELECT `book`.`book_id`, `book`.`book_name`, `book`.`book_metadata`, `book`.`book_lang`, `book`.`book_release_year`, `document`.`document_id`, `document`.`document_name`, `document`.`document_metadata`, `document`.`document_word_count`, `document`.`document_color`, `document`.`document_parser_class_name` FROM `book` INNER JOIN `document` ON `book`.`book_id`=`document`.`document_book_id` WHERE `document_id`=?";
        try (PreparedStatement select_document_stmt = jdbc_connection.prepareStatement(document_select)) {
            select_document_stmt.setInt(1, parsing_document_id);
            try (ResultSet rs = select_document_stmt.executeQuery()) {
                ArrayList<ParsingDocument> parsing_doucment_list = JsonResultset.resultset(rs, ParsingDocument.class);
                ParsingDocument parsing_document = parsing_doucment_list == null || parsing_doucment_list.size() == 0 ? null : parsing_doucment_list.get(0);
                return parsing_document;
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static SearchingDocument selectSearchingDocument(Connection jdbc_connection, Integer searching_document_id) throws Exception {
        String document_select = "SELECT `book`.`book_id` AS `document_book_id`, `book`.`book_name` AS `document_book_name`, `book`.`book_metadata` AS `document_book_metadata`, `book`.`book_lang` AS `document_book_lang`, `book`.`book_release_year` AS `document_book_release_year`, `document`.`document_id`, `document`.`document_name`, `document`.`document_metadata`, `document`.`document_word_count`, `document`.`document_color`, `document`.`document_indexed` FROM `book` INNER JOIN `document` ON `book`.`book_id`=`document`.`document_book_id` WHERE `document_id`=?";
        try (PreparedStatement select_document_stmt = jdbc_connection.prepareStatement(document_select)) {
            select_document_stmt.setInt(1, searching_document_id);
            try (ResultSet rs = select_document_stmt.executeQuery()) {
                ArrayList<SearchingDocument> searching_doucment_list = JsonResultset.resultset(rs, SearchingDocument.class);
                SearchingDocument searching_document = searching_doucment_list == null || searching_doucment_list.size() == 0 ? null : searching_doucment_list.get(0);
                return searching_document;
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static ArrayList<SearchingDocument> selectBookSearchingDocumentList(Connection jdbc_connection, Integer book_id) throws Exception {
        String document_select = "SELECT `book`.`book_id` AS `document_book_id`, `book`.`book_name` AS `document_book_name`, `book`.`book_metadata` AS `document_book_metadata`, `book`.`book_lang` AS `document_book_lang`, `book`.`book_release_year` AS `document_book_release_year`, `document`.`document_id`, `document`.`document_name`, `document`.`document_metadata`, `document`.`document_word_count`, `document`.`document_color`, `document`.`document_indexed` FROM `book` INNER JOIN `document` ON `book`.`book_id`=`document`.`document_book_id` WHERE `book`.`book_id`=?";
        try (PreparedStatement select_document_stmt = jdbc_connection.prepareStatement(document_select)) {
            select_document_stmt.setInt(1, book_id);
            try (ResultSet rs = select_document_stmt.executeQuery()) {
                ArrayList<SearchingDocument> searching_doucment_list = JsonResultset.resultset(rs, SearchingDocument.class);
                return searching_doucment_list;
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private Integer createStemIfNotExists(String stem_text) throws Exception {
        String stem_select = "SELECT `stem`.`stem_id` FROM `stem` WHERE `stem_text`=?";
        try (PreparedStatement select_stem_stmt = jdbc_connection.prepareStatement(stem_select)) {
            select_stem_stmt.setString(1, stem_text);
            try (ResultSet rs = select_stem_stmt.executeQuery()) {
                if (rs.next() == true) {
                    return rs.getInt(1);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        String stem_insert = "INSERT INTO `stem` (`stem_text`) VALUES (?)";
        try (PreparedStatement insert_stem_stmt = jdbc_connection.prepareStatement(stem_insert, Statement.RETURN_GENERATED_KEYS)) {
            insert_stem_stmt.setString(1, stem_text);
            insert_stem_stmt.executeUpdate();
            try (ResultSet generatedKeys = insert_stem_stmt.getGeneratedKeys()) {
                if (generatedKeys.next() == true) {
                    return generatedKeys.getInt(1);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        throw new Exception("Failed to create Stem '"+stem_text+"'");
    }
    
    private String selectStemText(Integer stem_id) throws Exception {
        String stem_select = "SELECT `stem`.`stem_text` FROM `stem` WHERE `stem_id`=?";
        try (PreparedStatement select_stem_stmt = jdbc_connection.prepareStatement(stem_select)) {
            select_stem_stmt.setInt(1, stem_id);
            try (ResultSet rs = select_stem_stmt.executeQuery()) {
                if (rs.next() == true) {
                    return rs.getString("stem_text");
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        throw new Exception("Failed to create Stem @'"+stem_id+"'");
    }
    
    private Integer createLexemeIfNotExists(Integer stem_id, String stem_text, String lexeme_text, String lexeme_part) throws Exception {
        String lexeme_select = "SELECT `lexeme`.`lexeme_id`, `lexeme`.`stem_id`, `lexeme`.`lexeme_text`, `lexeme_part` FROM `lexeme` WHERE `lexeme_text`=?";
        try (PreparedStatement select_lexeme_stmt = jdbc_connection.prepareStatement(lexeme_select)) {
            select_lexeme_stmt.setString(1, lexeme_text);
            try (ResultSet rs = select_lexeme_stmt.executeQuery()) {
                if (rs.next() == true) {
                    Integer stored_stem_id = rs.getInt("stem_id");
                    /*if (stem_id != stored_stem_id) {
                        String stored_stem_text = selectStemText(stored_stem_id);
                        throw new Exception("Multi Stem Error Prevention - Lexeme '"+lexeme_text+"' already exists under stored Stem '"+stored_stem_text+"'@"+stored_stem_id+" while provided Stem '"+stem_text+"'@"+stem_id+"");
                    }*/
                    if (stem_id.equals(stored_stem_id) == true) {
                        return rs.getInt(1);
                    }
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        String lexeme_insert = "INSERT INTO `lexeme` (`stem_id`, `lexeme_text`, `lexeme_part`) VALUES (?, ?)";
        try (PreparedStatement insert_lexeme_stmt = jdbc_connection.prepareStatement(lexeme_insert, Statement.RETURN_GENERATED_KEYS)) {
            insert_lexeme_stmt.setInt(1, stem_id);
            insert_lexeme_stmt.setString(2, lexeme_text);
            insert_lexeme_stmt.setString(3, lexeme_part);
            insert_lexeme_stmt.executeUpdate();
            try (ResultSet generatedKeys = insert_lexeme_stmt.getGeneratedKeys()) {
                if (generatedKeys.next() == true) {
                    return generatedKeys.getInt(1);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        throw new Exception("Failed to create Lexeme '"+lexeme_text+"' under Stem '"+stem_text+"'");
    }
    
    private Integer indexWordList(Connection jdbc_connection, Integer document_id, Word word) throws Exception {
        Integer document_raw_word_text_length = word.word_length;
        String document_word_text = cleanString(word.raw_word);
        String document_word_insert = "INSERT INTO `document_word` (`document_id`, `document_raw_word_index`, `document_raw_word_text`, `document_raw_word_text_start`, `document_raw_word_text_length`, `document_word_text`) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insert_document_word_stmt = jdbc_connection.prepareStatement(document_word_insert, Statement.RETURN_GENERATED_KEYS)) {
            insert_document_word_stmt.setInt(1, document_id);
            insert_document_word_stmt.setInt(2, word.word_index);
            insert_document_word_stmt.setString(3, word.raw_word);
            insert_document_word_stmt.setInt(4, word.word_start);
            insert_document_word_stmt.setInt(5, word.word_length);
            insert_document_word_stmt.setString(6, document_word_text);
            return insert_document_word_stmt.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private static StringBuilder cleanPhrase(String phrase) throws Exception {
        try (StringReader reader = new StringReader(phrase)) {
            StringBuilder cleansed_phrase = new StringBuilder();
            char[] buffer = new char[phrase.length()];
            char[] buffer_processed = new char[phrase.length()];
            int len;
            while ((len = reader.read(buffer, 0, buffer.length)) > -1) {
                int drops = cleanBuffer(buffer, buffer_processed, len);
                cleansed_phrase.append(buffer_processed, 0, len - drops);
            }
            return cleansed_phrase;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static String fixText(String text) {
        char[] buffer = text.toCharArray();
        char[] buffer_processed = new char[buffer.length];
        int drops = 0;
        int len = buffer.length;
        for (int i = 0; i < len; i++) {
            char check_char = buffer[i];
            if (Character.getType(check_char) == Character.DIRECTIONALITY_WHITESPACE) {
                check_char = ' ';
            }
            buffer_processed[i - drops] = check_char;
        }
        return new String(buffer_processed, 0, len - drops);
    }
    
    public static String cleanString(String word) {
        char[] buffer = word.toCharArray();
        char[] buffer_processed = new char[buffer.length];
        int drops = cleanBuffer(buffer, buffer_processed, buffer.length);
        return new String(buffer_processed, 0, buffer.length - drops);
    }
    
    public static Integer cleanBuffer(char[] buffer, char[] buffer_processed, int len) {
        int drops_marks = 0;
        //process buffer
        for (int i = 0; i < len; i++) {
            char check_char = buffer[i];
            if (ignore_char_list.contains(check_char) == true
                    || Character.getType(check_char) == Character.NON_SPACING_MARK) {
                drops_marks++;
                continue;
            }
            Character check_char_next = i == len - 1 ? null : buffer[i + 1];
            if (Character.isAlphabetic(check_char) == false
                    //&& Character.isDigit(check_char) == false //digit is not knowledge either const or var
                    && include_char_list.contains(check_char) == false
                    && Character.isWhitespace(check_char) == false) {
                if (check_char_next != null
                        && Character.isWhitespace(check_char) == false
                        && (Character.isLetter(check_char_next) == true
                        || Character.isDigit(check_char_next) == true)) {
                    //is non readable char before readable char, replace with white space
                    buffer_processed[i - drops_marks] = ' ';
                    continue;
                }
                drops_marks++;
                continue;
            }
            buffer_processed[i - drops_marks] = check_char;
        }
        System.arraycopy(buffer_processed, 0, buffer, 0, len - drops_marks);
        len = len - drops_marks;
        int drops = 0;
        for (int i = 0; i < len; i++) {
            char check_char = buffer[i];
            if (substitute_char_list.get(check_char) != null) {
                check_char = substitute_char_list.get(check_char);
            }
            Character check_char_next = i == len - 1 ? null : buffer[i + 1];
            Character substitute_end_char = substitute_end_char_list.get(check_char);
            if (substitute_end_char != null && ((check_char_next != null && Character.isLetter(check_char_next) == false) || i == len - 1)) {
                check_char = substitute_end_char;
            }
            buffer_processed[i - drops] = check_char;
        }
        return drops + drops_marks;
    }
    
    public static ArrayList<Word> selectWordMatchList(Connection jdbc_connection, String[] word_list) throws Exception {
        ArrayList<Word> word_match_list = new ArrayList<>();
        StringBuilder select_word_list = new StringBuilder();
        TreeMap<String, Integer> word_index_map = new TreeMap<>();
        for (int i = 0; i < word_list.length; i++) {
            String word_text = word_list[i];
            word_index_map.put(word_text, i);
            select_word_list.append("'").append(word_text).append("',");
        }
        select_word_list.deleteCharAt(select_word_list.length()-1);
        select_word_list.insert(0, "SELECT `document_id`,`document_raw_word_index`, `document_raw_word_text`, `document_raw_word_text_start`, `document_raw_word_text_length`, `document_word_text` FROM `word` INNER JOIN `document_word` ON `document_word`.`document_word_id` = `word`.`word_id` WHERE `word_text` IN (").append(") ORDER BY `document_id`, `document_raw_word_index`");
        try (PreparedStatement select_word_list_stmt = jdbc_connection.prepareStatement(select_word_list.toString())) {
            try (ResultSet rs = select_word_list_stmt.executeQuery()) {
                while (rs.next() == true) {
                    Integer document_id = rs.getInt("document_id");
                    Integer document_raw_word_index = rs.getInt("document_raw_word_index");
                    String document_raw_word_text = rs.getString("document_raw_word_text");
                    Integer document_raw_word_text_start = rs.getInt("document_raw_word_text_start");
                    Integer document_raw_word_text_length = rs.getInt("document_raw_word_text_length");
                    String document_word_text = rs.getString("document_word_text");
                    Integer search_index = word_index_map.get(document_word_text);
                    Word word = new Word(document_id, document_raw_word_text, document_word_text, document_raw_word_index, document_raw_word_text_start, document_raw_word_text_length, search_index);
                    word_match_list.add(word);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        
        return word_match_list;
    }
    
    public static InformationRetrievalParser getParserInstance(ParsingDocument parsing_document, Integer buffer_size) throws Exception {
        ClassLoader class_loader = InformationRetrievalParser.class.getClassLoader();
        Class loaded_class = class_loader.loadClass(parsing_document.document_parser_class_name);
        Constructor[] loaded_class_consructors = loaded_class.getDeclaredConstructors();
        Constructor loaded_class_default_consructor = null;
        for (Constructor constructor : loaded_class_consructors) {
            //TypeVariable[] tv = constructor.getTypeParameters();
            //if (tv != null && tv.length == 0) {
            //if (tv != null && tv.length == 1) {
            Class[] pt = constructor.getParameterTypes();
            //if (pt != null && pt.length == 0) {
            if (pt != null && pt.length == 2) {
                loaded_class_default_consructor = constructor;
            }
        }
        return (InformationRetrievalParser) loaded_class_default_consructor.newInstance(parsing_document, buffer_size);
    }

    
}
