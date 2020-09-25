package cafe;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Conector {

    EntryPort ep;
    File file;
    Document doc;

    public Conector(EntryPort ep) {
        this.ep = ep;
    }

    public void doWork() throws SAXException, IOException, ParserConfigurationException {

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.XML", "xml"); // Filtro solo xml
        fileChooser.setFileFilter(filtro); // Le paso el filtro al FileChooser
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.showOpenDialog(fileChooser);
        try {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            file = new File(ruta);

        } catch (NullPointerException e) {
            System.out.println("No se ha seleccionado ningún fichero");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        ep.in(doc);

        System.out.println("----------- Soy el conector y ya le he mandado el doc al entryPort del Splitter -----------");
    }

    public Document getDoc() {
        return doc;
    }

}
