package IndexSearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import parseXML.Pojo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jet on 2017/7/16.
 */
public class Indexer {

    private IndexWriter writer;//这个类用来写入索引


    //下面这个类是FileFilter的实现类，用来过滤符合条件的文档。
    private static class TextFilesFilter implements FileFilter{

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".txt");
        }
    }

    //构造方法，用来传入索引存放路径
    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
//        Directory dir = new  RAMDirectory();
//        CJKAnalyzer analyzer = new CJKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(new MyAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        writer = new IndexWriter(dir,config);
    }
/*
* Directory dir=FSDirectory.open(Paths.get(indexDir));这个方法是获取索引存放路径的方法，
* 早期版本的Lucene在open方法中用new File或new一个输入流的方式获取路径，
* 目前这里采用的是nio的方式获取文件夹路径，效率较高。
IndexWriterConfig config=new IndexWriterConfig(new StandardAnalyzer());
config.setOpenMode(OpenMode.CREATE_OR_APPEND);
writer=new IndexWriter(dir,config);
这里IndexWriterConfig对象是IndexWriter的配置对象，有很多配置。第二行的openMode是索引路径打开的方式，
这里选择的是创建或追加内容。
* */
    //关闭indexWriter
    public void close() throws IOException {
        writer.close();
    }


    //这个方法是写入索引的方法，将生成的document对象写入到索引中
    public  void indexFile(ArrayList<Pojo> arrayList) throws IOException {
        Iterator iterator = arrayList.iterator();
        while(iterator.hasNext()){
            Pojo pojo = (Pojo) iterator.next();
            if(pojo.getText()==null||pojo.getText()=="")
                continue;
            Document doc = getDocument(pojo);
            writer.addDocument(doc);
        }



    }

    //这个方法是生成Document对象的方法，Document对象就是对文档各个属性的封装
    private Document getDocument(Pojo pojo) throws IOException {
        Document doc = new Document();
            String text = conduct(pojo);
            doc.add(new Field("Contents",text, TextField.TYPE_STORED));//need to know more
            doc.add(new Field("FileName",pojo.getFileName(),StringField.TYPE_STORED));
            doc.add(new Field("Role",pojo.getRole(), StringField.TYPE_STORED)); //what's the meaning of getCanonicalPath()
       return doc;
    }

    //字符串切分算法。
    public String conduct(Pojo pojo) {
        String text = pojo.getText();
        String time = pojo.getTime();
        String [] textArr = text.split("\\s+");
        for(String ss : textArr){
            System.out.println(ss);
        }
        String [] timeArr = time.split("\\s+");
        for(String ss : timeArr){
            System.out.println(ss);
        }
        String bindStr = "";
        if(timeArr.length==textArr.length){
            for (int i = 0; i <textArr.length ; i++) {
                bindStr+= textArr[i]+"$"+timeArr[i]+" "; // we use "$" to divide text and time, in the Tokenizer,we will need it to split the string;we use space to divide each unit.
            }
        }else{
            System.out.println("error happen: textArray is not the same length as the timeArr");
        }
        return bindStr;
    }

    public static void main(String[] args) {
        Pojo pojo = new Pojo();


    }
}
