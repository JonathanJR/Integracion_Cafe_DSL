package cafe;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Splitter {

    Slot entry, out;

    public Splitter(Slot entry, Slot out) {
        this.entry = entry;
        this.out = out;
    }

    void doWork() throws XPathExpressionException, ParserConfigurationException {
        Document doc = entry.read();
        System.out.println("\n----------- Soy el Splitter y me ha llegado el documento -----------");
        Document drinkDoc = null;

        //xpath me quedo con las bebidas y las voy sacando en xml cada bebida
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/cafe_order/drinks/drink";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            drinkDoc = builder.newDocument();

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Element drink = drinkDoc.createElement(nNode.getNodeName());
                Element name = drinkDoc.createElement("name");
                Element type = drinkDoc.createElement("type");
                name.setTextContent(eElement.getElementsByTagName("name").item(0).getTextContent());
                type.setTextContent(eElement.getElementsByTagName("type").item(0).getTextContent());
                System.out.println("Soy el Splitter y troceo el elemento: " + name.getTextContent() + " " + type.getTextContent());
                drinkDoc.appendChild(drink);
                drink.appendChild(name);
                drink.appendChild(type);
            }
            out.write(drinkDoc);
        }

    }

}
