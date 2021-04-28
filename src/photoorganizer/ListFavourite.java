/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoorganizer;

/**
 *
 * @author User
 */
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File;  
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class ListFavourite {

    private File file;
    private Document document;
    private NodeList nodeList;  
    private Element root;
    private ArrayList<String> images;

    public ListFavourite() throws SAXException, IOException, ParserConfigurationException {
        
        file = new File("./src/photoorganizer/favourites.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();  
        document = documentBuilder.parse(file); 
        root = document.getDocumentElement();
        document.getDocumentElement().normalize();
        nodeList = document.getElementsByTagName("images");
        reloadArray();
    }
    
    public void reloadArray(){
        nodeList = document.getElementsByTagName("images");
        images = new ArrayList<String>();
        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element e = (Element) node;  
            String path = e.getElementsByTagName("imageFile").item(0).getTextContent();
            images.add(path);
        }
    }
    
    public ArrayList<String> getArrayList(){
        return images;
    }
    
    public void addElement(String newImage) throws TransformerException{
        Element newImagen = document.createElement("images");
        Element newPath = document.createElement("imageFile");
        newPath.setTextContent(newImage);
        newImagen.appendChild(newPath);
        root.appendChild(newImagen);
        saveFile();
        reloadArray();
    }
    public void deleteElement(String oldImage) throws TransformerException{
        if(images.contains(oldImage)){
            int indexOld = images.indexOf(oldImage);
            for(int i = 0; i < nodeList.getLength(); i++){
                if(indexOld == i ){
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                }
            }
        }
        saveFile();
        reloadArray();
    }
    public boolean contains(String check){
        return images.contains(check);
    }
    
    public void saveFile() throws TransformerConfigurationException, TransformerException{
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(file);
        Source input = new DOMSource(document);
        transformer.transform(input, output);
    }
}