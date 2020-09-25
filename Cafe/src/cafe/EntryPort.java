package cafe;

import org.w3c.dom.Document;

public class EntryPort {

    Slot s;

    public EntryPort(Slot s) {
        this.s = s;
    }

    void in(Document d) {
        s.write(d);
    }
}
