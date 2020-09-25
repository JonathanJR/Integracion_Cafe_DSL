package cafe;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Distributor {

    Slot entry, outCold, outHot;

    public Distributor(Slot entry, Slot outCold, Slot outHot) {
        this.entry = entry;
        this.outCold = outCold;
        this.outHot = outHot;
    }

    void doWork() throws XPathExpressionException {
        System.out.println("\n----------- Soy el Distributor y comienzo a leer documentos que me manda el Splitter por mi Slot de entrada -----------");
        while (!entry.getBuffer().isEmpty()) {
            Document doc = entry.read();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/drink";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            Node nNode = nodeList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                if (type.equals("hot")) {
                    outHot.write(doc);
                    System.out.println("Soy el distributor y encamino " + name + " hacia BaristaHot");
                } else {
                    outCold.write(doc);
                    System.out.println("Soy el distributor y encamino " + name + " hacia BaristaCold");
                }
            }

        }
    }
}
