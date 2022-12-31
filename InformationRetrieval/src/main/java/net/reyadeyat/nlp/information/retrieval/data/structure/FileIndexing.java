package net.reyadeyat.nlp.information.retrieval.data.structure;

import java.io.Reader;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class FileIndexing {
    public String document_name;
    public Reader document_reader;
    
    public FileIndexing(String document_name, Reader document_reader) {
        this.document_name = document_name;
        this.document_reader = document_reader;
    }
}
