package IndexSearch;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jet on 2017/7/21.
 */
public class MyAnalyzer  extends StopwordAnalyzerBase{

    public static final CharArraySet ENGLISH_STOP_WORDS_SET;
    public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;
    private int maxTokenLength;
    public static final CharArraySet STOP_WORDS_SET;

    public MyAnalyzer(CharArraySet stopWords) {
        super(stopWords);
        this.maxTokenLength = 255;
    }

    public MyAnalyzer() {
        this(STOP_WORDS_SET);
    }

    public MyAnalyzer(Reader stopwords) throws IOException {
        this(loadStopwordSet(stopwords));
    }

    public void setMaxTokenLength(int length) {
        this.maxTokenLength = length;
    }

    public int getMaxTokenLength() {
        return this.maxTokenLength;
    }

    protected TokenStreamComponents createComponents(String fieldName) {
        final MyCharTokenizer src = new MyTokenizer();
        StandardFilter tok = new StandardFilter(src);
        LowerCaseFilter tok1 = new LowerCaseFilter(tok);
        final StopFilter tok2 = new StopFilter(tok1, this.stopwords);
        return new TokenStreamComponents(src, tok2) {
            protected void setReader(Reader reader) {
                super.setReader(reader);
            }
        };
    }

    protected TokenStream normalize(String fieldName, TokenStream in) {
        StandardFilter result = new StandardFilter(in);
        LowerCaseFilter result1 = new LowerCaseFilter(result);
        return result1;
    }

    static {
        List stopWords = Arrays.asList(new String[]{"a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with"});
        CharArraySet stopSet = new CharArraySet(stopWords, false);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
        STOP_WORDS_SET = ENGLISH_STOP_WORDS_SET;
    }


}
