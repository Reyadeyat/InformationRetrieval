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
