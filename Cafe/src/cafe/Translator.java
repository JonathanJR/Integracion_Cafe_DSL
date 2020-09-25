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

public class Translator {

    Slot entry, out;

    public Translator(Slot entry, Slot out) {
        this.entry = entry;
        this.out = out;
    }

    public void doWork() throws XPathExpressionException, ParserConfigurationException {
        System.out.println("\n----------- Soy el Translator y comienzo a leer documentos que me manda el Replicator por mi Slot de entrada -----------");
        while (!entry.getBuffer().isEmpty()) {
            Document doc = entry.read();
            String consulta;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document drinkDoc = builder.newDocument();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/drink";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            Node nNode = nodeList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String nombre = eElement.getElementsByTagName("name").item(0).getTextContent();
                String type = eElement.getElementsByTagName("type").item(0).getTextContent();

                if (type.equals("hot")) {
                    consulta = "SELECT * from HOTDRINK where name = '" + nombre + "'";
                } else {
                    consulta = "SELECT * from COLDDRINK where name = '" + nombre + "'";
                }
                System.out.println("Soy el Translator preparo la consulta: " + consulta);

                Element drink = drinkDoc.createElement("drink");
                Element consult = drinkDoc.createElement("consult");
                Element name = drinkDoc.createElement("name");
                consult.setTextContent(consulta);
                name.setTextContent(nombre);
                drinkDoc.appendChild(drink);
                drink.appendChild(consult);
                drink.appendChild(name);
            }
            out.write(drinkDoc);
        }
    }
}
