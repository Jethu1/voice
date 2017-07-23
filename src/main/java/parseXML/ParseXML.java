package parseXML;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jet on 2017/7/18.
 */
public class ParseXML {

    public static void main(String[] args) throws Exception {
        ParseXML test = new ParseXML();
       File fileDir = new File("file");
        File[] xmlFiles = fileDir.listFiles();

        ArrayList<ArrayList<Pojo>> arrayLists= new ArrayList<ArrayList<Pojo>>(20);
        for (int i=0;i<xmlFiles.length;i++){
//            System.out.println(xmlFiles[i].getName());
            if(xmlFiles[i].getName().endsWith(".xml")&&!xmlFiles[i].getName().equals("a.xml")&&!xmlFiles[i].getName().equals("practice.xml"))
            arrayLists.add(test.test(xmlFiles[i].getName()));
        }
//        System.out.println(arrayLists);
      for (ArrayList<Pojo> pojo:arrayLists
             ) {
//            System.out.intln("what...................");
//            System.out.println(pojo.getBegin()+" "+ pojo.getEnd()+" "+pojo.getRole()+" "+ pojo.getText()+" "+pojo.getFileName() );
            System.out.println(pojo);
        }
    }

    public ArrayList<Pojo> test(String file1) throws Exception {

        // 创建saxReader对象
        SAXReader reader = new SAXReader();
        // 通过read方法读取一个文件 转换成Document对象
        Document document = reader.read(new File("file/"+file1));
        //获取根节点元素对象
        Element node = document.getRootElement();

        ArrayList<Pojo> list = new ArrayList<Pojo>(30);
        Pojo pojo= new Pojo();
        pojo.setFileName(file1);
        //遍历所有的元素节点
        listNodes(node,list,pojo);

        return list;
    }

    /**
     * 遍历当前节点元素下面的所有(元素的)子节点
     *
     * @param node
     */
    public void listNodes(Element node,ArrayList<Pojo> arrayList,Pojo pojo) {

        System.out.println("当前节点的名称：：" + node.getName());
        // 获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        for (Attribute attr : list) {
            System.out.println(attr.getText() + "-----" + attr.getName()
                    + "---" + attr.getValue());
         if(attr.getName().equals("Begin")){
             pojo.setBegin(attr.getValue());
         }else if(attr.getName().equals("End")){
             pojo.setEnd(attr.getValue());
//             pojo.setText(node.getText());
         }
            if(attr.getText().equals("R1")){
                pojo.setRole("R1");
            }else if(attr.getText().equals("R0")){
                pojo.setRole("R0");
            }
        }

        if (!(node.getTextTrim().equals(""))) {
            System.out.println("文本内容：：：：" + node.getText());
            if(node.getName().equals("Text")){
                pojo.setText(node.getText());
            }
        }

            if(node.getName().equals("Time")){
                pojo.setTime(node.getText());
                arrayList.add(pojo);
            }


       if(node.getName().equals("Item")) {
           Pojo pojo1 = new Pojo();
           pojo1.setFileName(pojo.getFileName());
           pojo1.setRole(pojo.getRole());
           pojo1.setBegin(pojo.getBegin());
           pojo1.setEnd(pojo.getEnd());
           Iterator<Element> it = node.elementIterator();
           // 遍历
           while (it.hasNext()) {
               // 获取某个子节点对象
               Element e = it.next();
               // 对子节点进行遍历
               listNodes(e,arrayList,pojo1);
           }
       }else{
           // 当前节点下面子节点迭代器
           Iterator<Element> it = node.elementIterator();
           // 遍历
           while (it.hasNext()) {
               // 获取某个子节点对象
               Element e = it.next();
               // 对子节点进行遍历
               listNodes(e,arrayList,pojo);
           }
       }
    }
}
