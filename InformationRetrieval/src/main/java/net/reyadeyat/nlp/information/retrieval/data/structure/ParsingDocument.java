package net.reyadeyat.nlp.information.retrieval.data.structure;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class ParsingDocument {
    public Integer document_id;
    public String document_name;
    @SerializedName("book_id")
    public Integer document_book_id;
    @SerializedName("book_name")
    public String document_book_name;
    @SerializedName("book_lang")
    public String document_book_lang;
    @SerializedName("book_release_year")
    public Integer document_book_release_year;
    @SerializedName("book_metadata")
    public String document_book_metadata;
    public String document_metadata;
    public String document_color;
    public String document_parser_class_name;
    
    public ParsingDocument(Integer document_book_id, String document_book_name, String document_book_metadata, String document_book_lang, Integer document_book_release_year, String document_name, String document_metadata, String document_color, String document_parser_class_name) {
        this.document_name = document_name;
        this.document_book_id = document_book_id;
        this.document_book_name = document_book_name;
        this.document_book_metadata = document_book_metadata;
        this.document_metadata = document_metadata;
        this.document_book_lang = document_book_lang;
        this.document_book_release_year = document_book_release_year;
        this.document_color = document_color;
        this.document_parser_class_name = document_parser_class_name;
    }
    
    public void setNewDocumentID(Integer document_id) {
        this.document_id = document_id;
    }
}
