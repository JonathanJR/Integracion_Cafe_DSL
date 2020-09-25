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

public class ContextEnricher {

    Slot entryConector, entryReplicator, outEnricher;

    public ContextEnricher(Slot entryConector, Slot entryReplicator, Slot outEnricher) {
        this.entryConector = entryConector;
        this.entryReplicator = entryReplicator;
        this.outEnricher = outEnricher;
    }

    public void doWork() throws ParserConfigurationException, XPathExpressionException {
        System.out.println("\n----------- Soy el ContextEnricher y comienzo a leer documentos que me manda el Correlator por mis dos Slots de entrada -----------");
        if (entryConector.getBuffer().size() == entryReplicator.getBuffer().size()) {
            while (!entryConector.getBuffer().isEmpty() && !entryReplicator.getBuffer().isEmpty()) {
                Document docConector = entryConector.read();
                Document docReplicator = entryReplicator.read();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document docService = builder.newDocument();

                XPath xPath = XPathFactory.newInstance().newXPath();
                String expressionReplicator = "/drink";
                String expressionConector = "/service";
                NodeList nodeListConector = (NodeList) xPath.compile(expressionConector).evaluate(docConector, XPathConstants.NODESET);
                NodeList nodeListReplicator = (NodeList) xPath.compile(expressionReplicator).evaluate(docReplicator, XPathConstants.NODESET);

                Node nNodeConector = nodeListConector.item(0);
                Node nNodeReplicator = nodeListReplicator.item(0);

                if (nNodeConector.getNodeType() == Node.ELEMENT_NODE && nNodeReplicator.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElementConector = (Element) nNodeConector;
                    Element eElementReplicator = (Element) nNodeReplicator;

                    String tipo = eElementReplicator.getElementsByTagName("type").item(0).getTextContent();
                    String nombre = eElementConector.getElementsByTagName("name").item(0).getTextContent();
                    String stoc = eElementConector.getElementsByTagName("stock").item(0).getTextContent();

                    Element service = docService.createElement("service");
                    Element name = docService.createElement("name");
                    Element type = docService.createElement("type");
                    Element stock = docService.createElement("stock");

                    name.setTextContent(nombre);
                    type.setTextContent(tipo);
                    stock.setTextContent(stoc);

                    System.out.println("Estoy en el enricher añadiendo bebida: " + nombre + " " + tipo + ", Stock: " + stoc);
                    docService.appendChild(service);
                    service.appendChild(name);
                    service.appendChild(type);
                    service.appendChild(stock);
                }
                outEnricher.write(docService);
            }
        }
    }
}
