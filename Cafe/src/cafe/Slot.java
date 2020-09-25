package cafe;

import java.util.ArrayList;
import org.w3c.dom.Document;

public class Slot {

    ArrayList<Document> buffer;

    public Slot() {
        buffer = new ArrayList<>();
    }

    public Document read() {
        Document d = buffer.get(0);
        buffer.remove(d);
        return d;
    }

    public void write(Document mensaje) {
        buffer.add(mensaje);
    }

    public ArrayList<Document> getBuffer() {
        return buffer;
    }    
}
