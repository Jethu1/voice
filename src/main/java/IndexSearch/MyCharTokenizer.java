package IndexSearch;

import org.apache.lucene.analysis.CharacterUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * Created by jet on 2017/7/21.
 */
public abstract class MyCharTokenizer extends Tokenizer {
    private int offset = 0;
    private int bufferIndex = 0;
    private int dataLen = 0;
    private int finalOffset = 0;
    private static final int MAX_WORD_LEN = 255;
    private static final int IO_BUFFER_SIZE = 4096;
    private final CharTermAttribute termAtt = (CharTermAttribute)this.addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = (OffsetAttribute)this.addAttribute(OffsetAttribute.class);
    private final CharacterUtils.CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(4096);

  /*  public void practice(CharTermAttribute termAtt,OffsetAttribute offsetAtt){
           termAtt = this.termAtt;
        offsetAtt = this.offsetAtt;
        offsetAtt.setOffset(1,12);


    }*/

    public MyCharTokenizer() {
    }

    public MyCharTokenizer(AttributeFactory factory) {
        super(factory);
    }

    public static MyCharTokenizer fromTokenCharPredicate(IntPredicate tokenCharPredicate) {
        return fromTokenCharPredicate(DEFAULT_TOKEN_ATTRIBUTE_FACTORY, tokenCharPredicate);
    }

    public static MyCharTokenizer fromTokenCharPredicate(AttributeFactory factory, IntPredicate tokenCharPredicate) {
        return fromTokenCharPredicate(factory, tokenCharPredicate, IntUnaryOperator.identity());
    }

    public static MyCharTokenizer fromTokenCharPredicate(IntPredicate tokenCharPredicate, IntUnaryOperator normalizer) {
        return fromTokenCharPredicate(DEFAULT_TOKEN_ATTRIBUTE_FACTORY, tokenCharPredicate, normalizer);
    }

    public static MyCharTokenizer fromTokenCharPredicate(final AttributeFactory factory, final IntPredicate tokenCharPredicate, final IntUnaryOperator normalizer) {
        Objects.requireNonNull(tokenCharPredicate, "predicate must not be null.");
        Objects.requireNonNull(normalizer, "normalizer must not be null");
        return new MyCharTokenizer(factory) {
            protected boolean isTokenChar(int c) {
                return tokenCharPredicate.test(c);
            }

            protected int normalize(int c) {
                return normalizer.applyAsInt(c);
            }
        };
    }

    public static MyCharTokenizer fromSeparatorCharPredicate(IntPredicate separatorCharPredicate) {
        return fromSeparatorCharPredicate(DEFAULT_TOKEN_ATTRIBUTE_FACTORY, separatorCharPredicate);
    }

    public static MyCharTokenizer fromSeparatorCharPredicate(AttributeFactory factory, IntPredicate separatorCharPredicate) {
        return fromSeparatorCharPredicate(factory, separatorCharPredicate, IntUnaryOperator.identity());
    }

    public static MyCharTokenizer fromSeparatorCharPredicate(IntPredicate separatorCharPredicate, IntUnaryOperator normalizer) {
        return fromSeparatorCharPredicate(DEFAULT_TOKEN_ATTRIBUTE_FACTORY, separatorCharPredicate, normalizer);
    }

    public static MyCharTokenizer fromSeparatorCharPredicate(AttributeFactory factory, IntPredicate separatorCharPredicate, IntUnaryOperator normalizer) {
        return fromTokenCharPredicate(factory, separatorCharPredicate.negate(), normalizer);
    }

    protected abstract boolean isTokenChar(int var1);

    protected int normalize(int c) {
        return c;
    }

    public final boolean incrementToken() throws IOException {
        this.clearAttributes();
        int length = 0;
        int start = -1;
        int end = -1;
        char[] buffer = this.termAtt.buffer();

        while(true) {
            if(this.bufferIndex >= this.dataLen) {
                this.offset += this.dataLen;
                CharacterUtils.fill(this.ioBuffer, this.input);
                if(this.ioBuffer.getLength() == 0) {
                    this.dataLen = 0;
                    if(length <= 0) {
                        this.finalOffset = this.correctOffset(this.offset);
                        return false;
                    }
                    break;
                }
                this.dataLen = this.ioBuffer.getLength();
                this.bufferIndex = 0;
            }

            int c = Character.codePointAt(this.ioBuffer.getBuffer(), this.bufferIndex, this.ioBuffer.getLength());
            int charCount = Character.charCount(c);
            this.bufferIndex += charCount;
            if(this.isTokenChar(c)) {
                if(length == 0) {
                    assert start == -1;

                    start = this.offset + this.bufferIndex - charCount;
                    end = start;
                } else if(length >= buffer.length - 1) {
                    buffer = this.termAtt.resizeBuffer(2 + length);
                }

                end += charCount;
                length += Character.toChars(this.normalize(c), buffer, length);
                if(length >= 255) {
                    break;
                }
            } else if(length > 0) {
                break;
            }
        }
        /**
         * divide chinese text and  time and write the time into offset
         */

        int count=0;
        String time = "";
        int begin1=0;
        int end1 =0;
        for (int i = 0; i < buffer.length ; i++) {
            if(buffer[i]=='$'){
                count = i;
            }
        }
        for (int i = count+1; i < length; i++) {
            time+=buffer[i];
        }
        String[] timeArr = time.split(",");
        begin1 = Integer.parseInt(timeArr[0]);
        end1 = Integer.parseInt(timeArr[1]);

        this.termAtt.setLength(count);

        assert start != -1;

        this.offsetAtt.setOffset(this.correctOffset(begin1), this.finalOffset = this.correctOffset(end1));
        return true;
    }

    public final void end() throws IOException {
        super.end();
        this.offsetAtt.setOffset(this.finalOffset, this.finalOffset);
    }

    public void reset() throws IOException {
        super.reset();
        this.bufferIndex = 0;
        this.offset = 0;
        this.dataLen = 0;
        this.finalOffset = 0;
        this.ioBuffer.reset();
    }

}
