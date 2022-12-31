package net.reyadeyat.nlp.information.retrieval.parser;

import net.reyadeyat.nlp.information.retrieval.data.structure.ParsingDocument;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import java.io.Reader;
import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class InformationRetrievalParserQuran implements InformationRetrievalParser {
    private ParsingDocument parsing_document;
    private final Integer buffer_size;
    private Integer buffer_last_read_cursor;
    private Integer buffer_last_read_length;
    private Integer remaining_char_count;
    private Integer document_char_cursor;
    private Integer document_word_index;
    private Boolean done;
    private final char[] buffer;
    
    public InformationRetrievalParserQuran(ParsingDocument parsing_document, Integer buffer_size) throws Exception {
        this.parsing_document = parsing_document;
        this.buffer_size = buffer_size;
        this.buffer_last_read_length = 0;
        this.remaining_char_count = 0;
        this.buffer_last_read_cursor = 0;
        this.buffer = new char[this.buffer_size];
        this.document_char_cursor = 0;
        this.document_word_index = 0;
        done = false;
    }
    
    @Override
    public ParsingDocument getParsingDocument() {
        return parsing_document;
    }
    
    @Override
    public Integer getBookID() {
        return parsing_document.document_book_id;
    }
    
    @Override
    public String getBookName() {
        return parsing_document.document_book_name;
    }
    
    @Override
    public String getBookMetadata() {
        return parsing_document.document_book_metadata;
    }
    
    @Override
    public Integer getDocumentID() {
        return parsing_document.document_id;
    }
    
    @Override
    public String getDocumentName() {
        return parsing_document.document_name;
    }
    
    @Override
    public String getDocumentMetadata() {
        return parsing_document.document_metadata;
    }
    
    @Override
    public String getDocumentColor() {
        return parsing_document.document_color;
    }
    
    @Override
    public ArrayList<Word> getNextWords(Reader document_reader) throws Exception {        
        if (remaining_char_count.compareTo(buffer_size) == 0) {
            throw new Exception("Buffer size must be grater than max word size in language! current size is ["+buffer_size+"] -> solution exceed buffer size!!");
        }
        ArrayList<Word> word_list = new ArrayList<Word>();
        if (remaining_char_count > 0) {
            System.arraycopy(buffer, buffer_last_read_cursor, buffer, 0, remaining_char_count);
            document_char_cursor -= remaining_char_count;
        }
        Integer read_length = document_reader.read(buffer, remaining_char_count, buffer_size - remaining_char_count);
        if (read_length.compareTo(buffer_size - remaining_char_count) < 0) {
            buffer_last_read_length = read_length == -1 ? remaining_char_count : read_length + remaining_char_count;
            buffer[buffer_last_read_length] = ' ';
            buffer_last_read_length++;
            done = true;
        } else {
            buffer_last_read_length = read_length + remaining_char_count;
        }
        Integer word_start = -1;
        Integer word_length = -1;
        for (int c = 0; c < buffer_last_read_length; c++) {
            Character token = buffer[c];
            if (Character.isLetter(token) == true
                    || Character.getType(token) == Character.NON_SPACING_MARK) {
                if (word_length == -1) {
                    word_start = c;
                    word_length = 0;
                    buffer_last_read_cursor = c;
                }
                word_length++;
            } else if (word_start > -1) {
                String raw_word = new String(buffer, word_start, word_length);
                Word word = new Word(raw_word, null, document_word_index++, document_char_cursor - word_length, word_length);
                //If word starts with و  check lexcical if not exist separate and check
                word_list.add(word);
                word_length = -1;
                word_start = -1;
                buffer_last_read_cursor = c;
            }
            document_char_cursor++;
        }
        remaining_char_count = buffer_size - buffer_last_read_cursor;
        return word_list;
    }
    
    @Override
    public Boolean hasMoreWords() {
        return !done;
    }
}
