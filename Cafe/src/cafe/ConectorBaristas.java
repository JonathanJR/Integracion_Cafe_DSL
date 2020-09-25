package cafe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class ConectorBaristas {

    Slot entry, out;
    conexionBD conexion = null;
    PreparedStatement ps = null;
    ResultSet rs;

    public ConectorBaristas(Slot entry, Slot out, conexionBD c) throws ClassNotFoundException {
        this.entry = entry;
        this.out = out;
        conexion = c;
    }

    public void doWork(String tabla) throws XPathExpressionException, SQLException, ParserConfigurationException {
        System.out.println("\n----------- Soy el ConectorBaristas y comienzo a leer documentos que me manda el Translator por mi Slot de entrada -----------");
        while (!entry.getBuffer().isEmpty()) {
            Document doc = entry.read();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document drinkDoc = builder.newDocument();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/drink";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            Node nNode = nodeList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String consulta = eElement.getElementsByTagName("consult").item(0).getTextContent();
                ps = conexion.con.prepareStatement(consulta);
                rs = ps.executeQuery();
                rs.next();
                String stockDrink = rs.getString(2);
                System.out.println("Soy el ConectorBaristas y ejecuto: " + consulta + " ------> Stock: " + stockDrink);

                if (Integer.parseInt(stockDrink) >= 1) {
                    Element service = drinkDoc.createElement("service");
                    Element name = drinkDoc.createElement("name");
                    Element stock = drinkDoc.createElement("stock");

                    name.setTextContent(eElement.getElementsByTagName("name").item(0).getTextContent());

                    stock.setTextContent("true");

                    drinkDoc.appendChild(service);
                    service.appendChild(name);
                    service.appendChild(stock);

                    String nombre = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String updateStock = "UPDATE " + tabla + " SET STOCK= '" + (Integer.parseInt(stockDrink) - 1) + "' WHERE NAME= '" + nombre + "'";

                    ps = conexion.con.prepareStatement(updateStock);
                    ps.executeUpdate();

                    out.write(drinkDoc);
                } else {
                    Element service = drinkDoc.createElement("service");
                    Element name = drinkDoc.createElement("name");
                    Element stock = drinkDoc.createElement("stock");

                    name.setTextContent(eElement.getElementsByTagName("name").item(0).getTextContent());
                    stock.setTextContent("false");

                    drinkDoc.appendChild(service);
                    service.appendChild(name);
                    service.appendChild(stock);

                    out.write(drinkDoc);
                }
            }
        }
    }
}
