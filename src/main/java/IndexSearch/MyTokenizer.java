package IndexSearch;


import org.apache.lucene.util.AttributeFactory;

/**
 * Created by jet on 2017/7/21.
 */
public class MyTokenizer extends MyCharTokenizer {

    public MyTokenizer() {
    }

    public MyTokenizer(AttributeFactory factory) {
        super(factory);
    }

    protected boolean isTokenChar(int c) {
        return !Character.isWhitespace(c);
    }
}


