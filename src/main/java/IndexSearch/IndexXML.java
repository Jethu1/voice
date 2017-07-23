package IndexSearch;

import parseXML.ParseXML;
import parseXML.Pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jet on 2017/7/20.
 */
public class IndexXML {

    public static void main(String[] args) throws Exception {
        /**
         *  Get the text content of the voice to be indexed.
         */
        ParseXML test = new ParseXML();
        File fileDir = new File("file");
        File[] xmlFiles = fileDir.listFiles();

        ArrayList<ArrayList<Pojo>> arrayLists= new ArrayList<ArrayList<Pojo>>(20);
        for (int i=0;i<xmlFiles.length;i++){
            if(xmlFiles[i].getName().endsWith(".xml")&&!xmlFiles[i].getName().equals("a.xml")&&!xmlFiles[i].getName().equals("practice.xml"))
                arrayLists.add(test.test(xmlFiles[i].getName()));
        }

        String indexDir = "E:/index";// the dirctory that stores index.
        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        Iterator iterator = arrayLists.iterator();
        while(iterator.hasNext()){
            indexer.indexFile((ArrayList<Pojo>) iterator.next());
        }
        indexer.close();
        long end = System.currentTimeMillis();
        System.out.println("Time cost to store index: " + (end-start));
    }
}
