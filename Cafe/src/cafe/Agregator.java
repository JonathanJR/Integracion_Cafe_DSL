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

public class Agregator {

    Slot entry, out;
    Document docFinal;

    public Agregator(Slot entry, Slot out) {
        this.entry = entry;
        this.out = out;
    }

    public void doWork(NodeList nodos) throws XPathExpressionException, ParserConfigurationException {
        System.out.println("\n----------- Soy el Agregator y comienzo a leer documentos que me manda el Merger por mi Slot de entrada para construir las Ordenes -----------");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        docFinal = builder.newDocument();
        Element order = docFinal.createElement("order");
        docFinal.appendChild(order);

        //Añado orderId a la cabecera
        Node nodeCabecera = nodos.item(0);
        Element eElementCabecera = (Element) nodeCabecera;
        String cabecera = eElementCabecera.getElementsByTagName("order_id").item(0).getTextContent();
        Element elementCabecera = docFinal.createElement("order_id");
        elementCabecera.setTextContent(cabecera);
        order.appendChild(elementCabecera);

        while (entry.getBuffer().size() >= 1) {
            Document doc = entry.read();
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/service";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            Node nNode = nodeList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String tipo = eElement.getElementsByTagName("type").item(0).getTextContent();
                String nombre = eElement.getElementsByTagName("name").item(0).getTextContent();
                String stoc = eElement.getElementsByTagName("stock").item(0).getTextContent();

                Element drink = docFinal.createElement("drink");
                Element name = docFinal.createElement("name");
                Element type = docFinal.createElement("type");
                Element stock = docFinal.createElement("stock");

                name.setTextContent(nombre);
                type.setTextContent(tipo);
                stock.setTextContent(stoc);

                System.out.println("Estoy en el agregator añadiendo bebida: " + nombre + ", tipo: " + tipo + ", Stock: " + stoc + ", y el orderID es: " + cabecera);

                order.appendChild(drink);
                drink.appendChild(name);
                drink.appendChild(type);
                drink.appendChild(stock);
            }
            out.write(docFinal);
        }
    }

    public Document getDocument() {
        return docFinal;
    }
}
