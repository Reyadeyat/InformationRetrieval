package net.reyadeyat.nlp.information.retrieval.data.structure;

import net.reyadeyat.nlp.json.JsonExclusionStrategy;
import com.google.gson.ExclusionStrategy;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class Citation {
    public String phrase_original;
    public SearchingDocument document;
    public String cited_text;
    public Integer citation_start_at;
    public Integer citation_end_at;
    public String citation_start_word;
    public String citation_end_word;
    private transient Connection connection;
    private transient Word start_word;
    private transient Word end_word;
    private transient Integer words_before;
    private transient Integer words_after;
    
    public Citation(Connection jdbc_connection, String phrase_original, SearchingDocument document, Word start_word, Word end_word, Integer words_before, Integer words_after) throws Exception {
        this.connection = connection;
        this.phrase_original = phrase_original;
        this.document = document;
        this.start_word = start_word;
        this.end_word = end_word;
        this.citation_start_word = this.start_word.raw_word;
        this.citation_end_word = this.end_word.raw_word;
        this.words_before = words_before;
        this.words_after = words_after;
        Integer cite_start_word_index = this.start_word.word_index - this.words_before < 0 ? 0 : this.start_word.word_index - this.words_before;
        Integer cite_end_word_index = this.end_word.word_index + this.words_after > document.document_word_count ? document.document_word_count - 1 : this.end_word.word_index + this.words_after;
        Integer cite_block_start = 0;
        Integer cite_block_end = 0;
        String select_cite_start_end = "SELECT `document_word`.`document_raw_word_index`, " +
            "CASE WHEN `document_word`.`document_raw_word_index` = ? THEN `document_word`.`document_raw_word_text_start` " +
            "WHEN `document_word`.`document_raw_word_index` = ? THEN `document_word`.`document_raw_word_text_start` + `document_word`.`document_raw_word_text_length` " +
            "ELSE NULL END AS cite_index " +
            "FROM `document_word` " +
            "WHERE `document_word`.`document_id` = ? AND `document_word`.`document_raw_word_index` IN(?, ?)";
        try (PreparedStatement select_cite_start_end_stmt = connection.prepareStatement(select_cite_start_end)) {
            select_cite_start_end_stmt.setInt(1, cite_start_word_index);
            select_cite_start_end_stmt.setInt(2, cite_end_word_index);
            select_cite_start_end_stmt.setInt(3, document.document_id);
            select_cite_start_end_stmt.setInt(4, cite_start_word_index);
            select_cite_start_end_stmt.setInt(5, cite_end_word_index);
            try (ResultSet rs = select_cite_start_end_stmt.executeQuery()) {
                while (rs.next() == true) {
                    if (rs.getInt("document_raw_word_index") == cite_start_word_index.intValue()) {
                        cite_block_start = rs.getInt("cite_index");
                    }
                    if (rs.getInt("document_raw_word_index") == cite_end_word_index.intValue()) {
                        cite_block_end = rs.getInt("cite_index");
                    }
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        this.citation_start_at = start_word.word_start - cite_block_start;
        this.citation_end_at = end_word.word_start - cite_block_start + end_word.word_length;
        String select_citation = "SELECT `document`.`document_raw_text` FROM `document` WHERE `document_book_id`=? AND `document_id`=?";
        try (PreparedStatement select_citation_stmt = connection.prepareStatement(select_citation)) {
            select_citation_stmt.setInt(1, document.document_book_id);
            select_citation_stmt.setInt(2, document.document_id);
            try (ResultSet rs = select_citation_stmt.executeQuery()) {
                if (rs.next() == true) {
                    char[] buffer = new char[cite_block_end - cite_block_start];
                    InputStreamReader isr = new InputStreamReader(rs.getBinaryStream("document_raw_text"), StandardCharsets.UTF_8);
                    Long skipped = isr.skip(cite_block_start);
                    Integer count = isr.read(buffer, 0, cite_block_end - cite_block_start);
                    cited_text = new String(buffer, 0, count);
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static ExclusionStrategy getJsonExclusionStrategy() {
        ArrayList<String> expose_field_list = new ArrayList<>();
        expose_field_list.add("document");
        expose_field_list.add("document_id");
        expose_field_list.add("document_name");
        expose_field_list.add("document_book_id");
        expose_field_list.add("document_book_name");
        expose_field_list.add("document_book_lang");
        expose_field_list.add("document_book_release_year");
        expose_field_list.add("document_book_metadata");
        expose_field_list.add("document_metadata");
        expose_field_list.add("document_word_count");
        expose_field_list.add("document_color");
        expose_field_list.add("cited_text");
        expose_field_list.add("citation_start_at");
        expose_field_list.add("citation_end_at");
        expose_field_list.add("citation_start_word");
        expose_field_list.add("citation_end_word");
        
        return new JsonExclusionStrategy(expose_field_list);
    }

    
}
