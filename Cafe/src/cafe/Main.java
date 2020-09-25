package cafe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

    static FileWriter fileOut;
    static Document docFinal;
    static String tableHot = "HOTDRINK";
    static String tableCold = "COLDDRINK";

    public static void main(String argv[]) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, ClassNotFoundException, SQLException {
        conexionBD conexion = new conexionBD();

        Slot entry = new Slot();
        Slot outSplitter = new Slot();
        Slot outCold = new Slot();
        Slot outHot = new Slot();
        Slot outBaristaHot = new Slot();
        Slot outWaiterHot = new Slot();
        Slot outBaristaCold = new Slot();
        Slot outWaiterCold = new Slot();
        Slot outConectorBDHot = new Slot();
        Slot outConectorBDCold = new Slot();
        Slot outBDHot = new Slot();
        Slot outBDCold = new Slot();
        Slot outCorrelatorHotConector = new Slot();
        Slot outCorrelatorHotReplicator = new Slot();
        Slot outCorrelatorColdConector = new Slot();
        Slot outCorrelatorColdReplicator = new Slot();
        Slot outEnricherHot = new Slot();
        Slot outEnricherCold = new Slot();
        Slot outMerger = new Slot();
        Slot outFinal = new Slot();

        EntryPort ep = new EntryPort(entry);

        Conector con = new Conector(ep);
        con.doWork();

        //Intercepto el doc para coger los nodos
        Document docu = con.getDoc();
        NodeList nodos = docu.getChildNodes();

        Splitter sp = new Splitter(entry, outSplitter);
        sp.doWork();

        Distributor distributor = new Distributor(outSplitter, outCold, outHot);
        distributor.doWork();

        Replicator replicatorHot = new Replicator(outHot, outBaristaHot, outWaiterHot);
        Replicator replicatorCold = new Replicator(outCold, outBaristaCold, outWaiterCold);

        replicatorHot.doWork();
        replicatorCold.doWork();

        Translator translatorHot = new Translator(outBaristaHot, outConectorBDHot);
        Translator translatorCold = new Translator(outBaristaCold, outConectorBDCold);

        translatorHot.doWork();
        translatorCold.doWork();

        ConectorBaristas baristaHot = new ConectorBaristas(outConectorBDHot, outBDHot, conexion);
        ConectorBaristas baristaCold = new ConectorBaristas(outConectorBDCold, outBDCold, conexion);

        baristaHot.doWork(tableHot);
        baristaCold.doWork(tableCold);

        Correlator correlatorHot = new Correlator(outBDHot, outWaiterHot, outCorrelatorHotConector, outCorrelatorHotReplicator);
        Correlator correlatorCold = new Correlator(outBDCold, outWaiterCold, outCorrelatorColdConector, outCorrelatorColdReplicator);

        correlatorHot.doWork();
        correlatorCold.doWork();

        ContextEnricher contextEnricherHot = new ContextEnricher(outCorrelatorHotConector, outCorrelatorHotReplicator, outEnricherHot);
        ContextEnricher contextEnricherCold = new ContextEnricher(outCorrelatorColdConector, outCorrelatorColdReplicator, outEnricherCold);

        contextEnricherHot.doWork();
        contextEnricherCold.doWork();

        Merger merger = new Merger(outEnricherHot, outEnricherCold, outMerger);
        merger.doWork();

        Agregator agregator = new Agregator(outMerger, outFinal);
        agregator.doWork(nodos);

        docFinal = agregator.getDocument();

        exportFile();
    }

    private static void exportFile() throws IOException {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fc.showOpenDialog(fc);
        fileOut = new FileWriter(fc.getSelectedFile().getAbsolutePath() + "/salida.xml");

        try {
            TransformerFactory transFact = TransformerFactory.newInstance();

            //Formateamos el fichero. Añadimos sangrado y la cabecera de XML
            transFact.setAttribute("indent-number", 3);
            Transformer trans = transFact.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            //Hacemos la transformación
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            DOMSource domSource = new DOMSource(docFinal);
            trans.transform(domSource, sr);

            //Escribimos todo el árbol en el fichero
            try ( //Creamos fichero para escribir en modo texto
                    PrintWriter writer = new PrintWriter(fileOut)) {
                //Escribimos todo el árbol en el fichero
                writer.println(sw.toString());
                //Cerramos el fichero
                System.out.println("\nFichero guardado correctamente en la ruta: " + fc.getSelectedFile().getAbsolutePath() + "\\salida.xml");
            }
        } catch (IllegalArgumentException | TransformerException ex) {
        }

    }
}
