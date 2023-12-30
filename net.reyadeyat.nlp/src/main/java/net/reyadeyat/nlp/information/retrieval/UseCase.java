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
import java.sql.DriverManager;
import net.reyadeyat.nlp.information.retrieval.data.structure.ParsingDocument;
import net.reyadeyat.nlp.information.retrieval.parser.InformationRetrievalParser;

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
public class UseCase {
    public static void main(String[] args) {
        try {
            //Connection jdbc_connection = DriverManager.getConnection("jdbc:mysql://localhost:51970/information-retrieval", "root", "123456");
            Connection jdbc_connection = DriverManager.getConnection("jdbc:mysql://192.168.0.160:57391/information-retrieval", "remote", "Z@Ibra3zimovitgsh43^@!&%$#@");
            jdbc_connection.setAutoCommit(false);
            
            //////Index Quran
            //String quran_file_path = "/linux/projects/System Hesabat/-/backend/Kernel/InformationRetrieval/quran_sora";
            //String book_name = "القرآن الكريم";
            //Integer book_id = InformationRetrieval.createBookIfNotExists(jdbc_connection, book_name, "", "", 0);
            //String document_book_metadata = "";
            //String document_book_lang = "";
            //Integer document_book_release_year = 1;
            //
            //File dir = new File(quran_file_path);
            //File[] directory_list = dir.listFiles();
            //Arrays.sort(directory_list, Comparator.comparing(File::getName));
            //if (directory_list != null) {
            //  for (File document_file : directory_list) {
            //    if (document_file.isFile() == false){
            //        continue;
            //    }
            //    String document_name = InformationRetrieval.soura_name.get(document_file.getName().split("\\.")[0]);
            //    String document_metadata = "";
            //    String document_color = "white";
            //    String document_parser_class_name = "net.reyadeyat.information.retrieval.parser.InformationRetrievalParserQuran";
            //    ParsingDocument parsing_document = new ParsingDocument(book_id, book_name, document_book_metadata, document_book_lang, document_book_release_year, document_name, document_metadata, document_color, document_parser_class_name);
            //    BufferedReader document_reader = new BufferedReader(new FileReader(document_file));
            //    InformationRetrieval.createBookDocument(jdbc_connection, parsing_document, document_reader);
            //    Integer buffer_size = 1024*8;
            //    parsing_document = InformationRetrieval.selectParsingDocument(jdbc_connection, parsing_document.document_id);
            //    InformationRetrievalParser irp = InformationRetrieval.getParserInstance(parsing_document, buffer_size);
            //    InformationRetrieval ir = new InformationRetrieval(jdbc_connection, irp);
            //    ir.indexDocument();
            //  }
            //}
            
            String parse_text = "فَلَعَلَّكَ بَٰخِعٌ نَّفْسَكَ عَلَىٰٓ ءَاثَٰرِهِمْ إِن لَّمْ يُؤْمِنُواْ بِهَٰذَا ٱلْحَدِيثِ أَسَفًا";
            String book_name = "test Small";
            Integer book_id = InformationRetrieval.createBookIfNotExists(jdbc_connection, book_name, "", "", 0);
            String document_book_metadata = "";
            String document_book_lang = "";
            Integer document_book_release_year = 1;
            String document_name = "test Small-"+Math.random();
            String document_metadata = "";
            String document_color = "white";
            String document_parser_class_name = "net.reyadeyat.information.retrieval.parser.InformationRetrievalParserArabic";
            ParsingDocument parsing_document = new ParsingDocument(book_id, book_name, document_book_metadata, document_book_lang, document_book_release_year, document_name, document_metadata, document_color, document_parser_class_name);
            Reader document_reader = new StringReader(parse_text);
            InformationRetrieval.createBookDocument(jdbc_connection, parsing_document, document_reader);
            Integer buffer_size = 100;//3;//3;//5;//6;//7;//10;//11;
            parsing_document = InformationRetrieval.selectParsingDocument(jdbc_connection, parsing_document.document_id);
            InformationRetrievalParser irp = InformationRetrieval.getParserInstance(parsing_document, buffer_size);
            InformationRetrieval ir = new InformationRetrieval(jdbc_connection, irp);
            ir.indexDocument();
            
            //String parse_text = "2|284|لِّلَّهِ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ وَإِن تُبْدُوا مَا فِي أَنفُسِكُمْ أَوْ تُخْفُوهُ يُحَاسِبْكُم بِهِ اللَّهُ ۖ فَيَغْفِرُ لِمَن يَشَاءُ وَيُعَذِّبُ مَن يَشَاءُ ۗ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ\n" +
            //"2|285|آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ\n" +
            //"2|286|لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ";
            //String book_name = "test Small";
            //Integer book_id = InformationRetrieval.createBookIfNotExists(jdbc_connection, book_name, "", "", 0);
            //String document_book_metadata = "";
            //String document_book_lang = "";
            //Integer document_book_release_year = 1;
            //String document_name = "quran aya-"+Math.random();
            //String document_metadata = "";
            //String document_color = "white";
            //String document_parser_class_name = "net.reyadeyat.information.retrieval.parser.InformationRetrievalParserArabic";
            //ParsingDocument parsing_document = new ParsingDocument(book_id, book_name, document_book_metadata, document_book_lang, document_book_release_year, document_name, document_metadata, document_color, document_parser_class_name);
            //Reader document_reader = new StringReader(parse_text);
            //InformationRetrieval.createBookDocument(jdbc_connection, parsing_document, document_reader);
            //Integer buffer_size = 64;
            //parsing_document = InformationRetrieval.selectParsingDocument(jdbc_connection, parsing_document.document_id);
            //InformationRetrievalParser irp = InformationRetrieval.getParserInstance(parsing_document, buffer_size);
            //InformationRetrieval ir = new InformationRetrieval(jdbc_connection, irp);
            //ir.indexDocument();
            //jdbc_connection.commit();
            
            ////String text = "test Small";
            ////String text = "بشرا رسولا";
            //String text = "آمن الرسول";
            ////String text = "لَيْلَةِ الْقَدْرِ";
            ////String text = "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ";
            ////String text = "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْر";
            ////String text = "يَا أَيُّهَا الَّذِينَ آمَنُوا أَنفِقُوا مِمَّا رَزَقْنَاكُم مِّن قَبْلِ أَن يَأْتِيَ يَوْمٌ لَّا بَيْعٌ فِيهِ وَلَا خُلَّةٌ وَلَا شَفَاعَةٌ ۗ وَالْكَافِرُونَ هُمُ الظَّالِمُونَ اللَّهُ لَا إِلَـٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ لَا إِكْرَاهَ فِي الدِّينِ ۖ قَد تَّبَيَّنَ الرُّشْدُ مِنَ الْغَيِّ ۚ فَمَن يَكْفُرْ بِالطَّاغُوتِ وَيُؤْمِن بِاللَّهِ فَقَدِ اسْتَمْسَكَ بِالْعُرْوَةِ الْوُثْقَىٰ لَا انفِصَامَ لَهَا ۗ وَاللَّهُ سَمِيعٌ عَلِيمٌ اللَّهُ وَلِيُّ الَّذِينَ آمَنُوا يُخْرِجُهُم مِّنَ الظُّلُمَاتِ إِلَى النُّورِ ۖ وَالَّذِينَ كَفَرُوا أَوْلِيَاؤُهُمُ الطَّاغُوتُ يُخْرِجُونَهُم مِّنَ النُّورِ إِلَى الظُّلُمَاتِ ۗ أُولَـٰئِكَ أَصْحَابُ النَّارِ ۖ هُمْ فِيهَا خَالِدُونَ";
            //Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create();
            //PlagiarismChecker plagiarism_checker = new PlagiarismChecker(jdbc_connection, text, 5, 5);
            //ArrayList<Match> match_list = plagiarism_checker.getMatchList();
            //for (int i = 0; i < match_list.size(); i++) {
            //    Match match = match_list.get(i);
            //    ArrayList<Citation> citation_list = match.getCitationList(jdbc_connection);
            //    //JsonArray citation_json_array = gson.toJsonTree(citation_list, new TypeToken<ArrayList<Citation>>(){}.getType()).getAsJsonArray();
            //    JsonArray citation_json_array = gson.toJsonTree(citation_list, citation_list.getClass()).getAsJsonArray();
            //    System.out.println(gson.toJson(citation_json_array));
            //}
            
            //String quran_file_path = "D:/quran/quran-simple.txt";
            //InformationRetrieval.prepareQuranFiles(quran_file_path);
            
            //String quran_word_stem_file_path = "/linux/projects/System Hesabat/-/backend/Kernel/InformationRetrieval/qouran_words.txt";
            //InformationRetrieval ir = new InformationRetrieval(jdbc_connection);
            //ir.prepareQuranWordsFiles(quran_word_stem_file_path);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /*public static void prepareQuranFiles(String document_file_path) throws Exception {
        String base_folder = document_file_path.substring(0, document_file_path.lastIndexOf("/")+1);
        File document_file = new File(document_file_path);
        try (BufferedReader document_reader = new BufferedReader(new FileReader(document_file))) {
            String line;
            Integer last_soura_number = 0;
            FileWriter document_writer = null;
            while ((line = document_reader.readLine()) != null) {
                Integer soura_number = Integer.parseInt(line.substring(0, line.indexOf("|")));
                if (soura_number.equals(last_soura_number.intValue()) == true) {
                    if (document_writer != null) {
                        document_writer.close();
                    }
                    String document_file_out = "000".substring(0, 3 - String.valueOf(soura_number).length()) + soura_number;
                    document_file_out = base_folder + document_file_out+"-"+ soura_name.get(document_file_out) + ".txt";
                    document_writer = new FileWriter(document_file_out);
                    last_soura_number = soura_number;
                }
                document_writer.write(line, 0, line.length());
                document_writer.write('\n');
            }
            if (document_writer != null) {
                document_writer.close();
            }
        } catch(Exception ex) {
            throw ex;
        }
    }*/
    
    /*public void prepareQuranWordsFiles(String document_file_path) throws Exception {
        String base_folder = document_file_path.substring(0, document_file_path.lastIndexOf("/")+1);
        File document_file = new File(document_file_path);
        try (BufferedReader document_reader = new BufferedReader(new FileReader(document_file))) {
            String line;
            Integer last_soura_number = 0;
            FileWriter document_writer = null;
            Boolean start_new_stem = false;
            String stem = null;
            StringBuilder stem_word_list = new StringBuilder();
            while ((line = document_reader.readLine()) != null) {
                line = fixText(line);
                if (line.isEmpty() || line.isBlank()) {
                    continue;
                }
                if (line.contains("عدد الكلمات المختلفة")) {
                    if (stem != null) {
                        int pos;
                        while ((pos = stem_word_list.indexOf("  ")) > -1) {
                            stem_word_list.replace(pos, pos+2, " ");
                        }
                        if (stem_word_list.charAt(0) == ' ') {
                            stem_word_list.deleteCharAt(0);
                        }
                        if (stem_word_list.charAt(stem_word_list.length() - 1) == ' ') {
                            stem_word_list.deleteCharAt(stem_word_list.length() - 1);
                        }
                        String stem_word_list_string = stem_word_list.toString();
                        String[] lexeme_list = stem_word_list_string.split(" ");                        
                        Integer stem_id = createStemIfNotExists(stem);
                        System.out.println("Stem: " + stem + "@" + stem_id);
                        System.out.println("stem_word_list: " + stem_word_list);
                        String lexeme_part = "Noun";
                        for (String lexeme : lexeme_list) {
                            Integer lexeme_id = createLexemeIfNotExists(stem_id, stem, lexeme_text, lexeme_part);
                            System.out.println("lexeme: " + lexeme + "@" + lexeme_id);
                        }
                        stem = null;
                        stem_word_list.delete(0, stem_word_list.length());
                    }
                    continue;
                }
                if (line.contains("عدد الكلمات الكلي لهذا الجذر")) {
                    start_new_stem = true;
                    continue;
                }
                if (start_new_stem == true) {
                    stem = cleanString(line);
                    start_new_stem = false;
                } else {
                    stem_word_list.append(cleanString(line)).append(" ");
                }
            }
            if (document_writer != null) {
                document_writer.close();
            }
            jdbc_connection.commit();
        } catch(Exception ex) {
            throw ex;
        }
    }*/
    
    /*public static final HashMap<String, String> soura_name = new HashMap<>();
    static {
        soura_name.put("001", "الفاتحة");
        soura_name.put("002", "البقرة");
        soura_name.put("003", "آل عمران");
        soura_name.put("004", "النساء");
        soura_name.put("005", "المائدة");
        soura_name.put("006", "الأنعام");
        soura_name.put("007", "الأعراف");
        soura_name.put("008", "الأنفال");
        soura_name.put("009", "التوبة");
        soura_name.put("010", "يونس");
        soura_name.put("011", "هود");
        soura_name.put("012", "يوسف");
        soura_name.put("013", "الرعد");
        soura_name.put("014", "إبراهيم");
        soura_name.put("015", "الحجر");
        soura_name.put("016", "النحل");
        soura_name.put("017", "الإسراء");
        soura_name.put("018", "الكهف");
        soura_name.put("019", "مريم");
        soura_name.put("020", "طه");
        soura_name.put("021", "الأنبياء");
        soura_name.put("022", "الحج");
        soura_name.put("023", "المؤمنون");
        soura_name.put("024", "النور");
        soura_name.put("025", "الفرقان");
        soura_name.put("026", "الشعراء");
        soura_name.put("027", "النمل");
        soura_name.put("028", "القصص");
        soura_name.put("029", "العنكبوت");
        soura_name.put("030", "الروم");
        soura_name.put("031", "لقمان");
        soura_name.put("032", "السجدة");
        soura_name.put("033", "سورة الأحزاب");
        soura_name.put("034", "سبأ");
        soura_name.put("035", "فاطر");
        soura_name.put("036", "يس");
        soura_name.put("037", "الصافات");
        soura_name.put("038", "ص");
        soura_name.put("039", "الزمر");
        soura_name.put("040", "غافر");
        soura_name.put("041", "فصلت");
        soura_name.put("042", "الشورى");
        soura_name.put("043", "الزخرف");
        soura_name.put("044", "الدخان");
        soura_name.put("045", "الجاثية");
        soura_name.put("046", "الأحقاف");
        soura_name.put("047", "محمد");
        soura_name.put("048", "الفتح");
        soura_name.put("049", "الحجرات");
        soura_name.put("050", "ق");
        soura_name.put("051", "الذاريات");
        soura_name.put("052", "الطور");
        soura_name.put("053", "النجم");
        soura_name.put("054", "القمر");
        soura_name.put("055", "الرحمن");
        soura_name.put("056", "الواقعة");
        soura_name.put("057", "الحديد");
        soura_name.put("058", "المجادلة");
        soura_name.put("059", "الحشر");
        soura_name.put("060", "الممتحنة");
        soura_name.put("061", "الصف");
        soura_name.put("062", "الجمعة");
        soura_name.put("063", "المنافقون");
        soura_name.put("064", "التغابن");
        soura_name.put("065", "الطلاق");
        soura_name.put("066", "التحريم");
        soura_name.put("067", "الملك");
        soura_name.put("068", "القلم");
        soura_name.put("069", "الحاقة");
        soura_name.put("070", "المعارج");
        soura_name.put("071", "نوح");
        soura_name.put("072", "الجن");
        soura_name.put("073", "المزمل");
        soura_name.put("074", "المدثر");
        soura_name.put("075", "القيامة");
        soura_name.put("076", "الإنسان");
        soura_name.put("077", "المرسلات");
        soura_name.put("078", "النبأ");
        soura_name.put("079", "النازعات");
        soura_name.put("080", "عبس");
        soura_name.put("081", "التكوير");
        soura_name.put("082", "الانفطار");
        soura_name.put("083", "المطففين");
        soura_name.put("084", "الإنشقاق");
        soura_name.put("085", "البروج");
        soura_name.put("086", "الطارق");
        soura_name.put("087", "الأعلى");
        soura_name.put("088", "الغاشية");
        soura_name.put("089", "الفجر");
        soura_name.put("090", "البلد");
        soura_name.put("091", "الشمس");
        soura_name.put("092", "الليل");
        soura_name.put("093", "الضحى");
        soura_name.put("094", "الشرح");
        soura_name.put("095", "التين");
        soura_name.put("096", "العلق");
        soura_name.put("097", "القدر");
        soura_name.put("098", "البينة");
        soura_name.put("099", "الزلزلة");
        soura_name.put("100", "العاديات");
        soura_name.put("101", "القارعة");
        soura_name.put("102", "التكاثر");
        soura_name.put("103", "العصر");
        soura_name.put("104", "الهمزة");
        soura_name.put("105", "الفيل");
        soura_name.put("106", "قريش");
        soura_name.put("107", "الماعون");
        soura_name.put("108", "الكوثر");
        soura_name.put("109", "الكافرون");
        soura_name.put("110", "النصر");
        soura_name.put("111", "المسد");
        soura_name.put("112", "الإخلاص");
        soura_name.put("113", "الفلق");
        soura_name.put("114", "الناس");
    }*/
}
