package IndexSearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jet on 2017/7/20.
 */
public class SearchXML {
    public static void main(String[] args) throws IOException, ParseException {
        String indexDir = "E:/index";
        String query = "修车";
        SearchXML searcher = new SearchXML();
        searcher.search(indexDir,query,5);
    }

    //这个方法是搜索索引的方法，传入索引路径和查询表达式
    public static void search(String indexDir,String query,int n) throws IOException, ParseException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
        String[] fields = new String[] {"Contents","FileName","Role"};
        Map<String,Float> map = new HashMap<String,Float>();
        map.put("Contents",1.0f);
        map.put("FileName",1.0f);
        map.put("Role",1.0f);
        MyAnalyzer analyzer = new MyAnalyzer();
        QueryParser parser = new MultiFieldQueryParser(fields,analyzer,map);
        Query q = parser.parse(query);
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(q,n);
        long end = System.currentTimeMillis();
        System.out.println("maxTokenLength : " + analyzer.getMaxTokenLength());
        System.out.println("totalhits: " + hits.totalHits);

        System.out.println("How long it takes to search: "+(end-start));
        for (ScoreDoc scoredoc:hits.scoreDocs
                ) {
            Document doc = searcher.doc(scoredoc.doc);
            System.out.println("score: "+ scoredoc.score);
//          System.out.println("doc.getValues(Contents):  "+doc.getValues("Contents"));
            System.out.println("getFields: "+doc.get("Contents"));
            System.out.println(doc.get("FileName"));

        }
    }
}
