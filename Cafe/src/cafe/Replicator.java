package cafe;

import org.w3c.dom.Document;

public class Replicator {

    Slot entry, outBarista, outWaiter;

    public Replicator(Slot entry, Slot outBarista, Slot outWaiter) {
        this.entry = entry;
        this.outBarista = outBarista;
        this.outWaiter = outWaiter;
    }

    public void doWork() {
        System.out.println("\n----------- Soy el Replicator y comienzo a leer documentos que me manda el Distributor por mi Slot de entrada para sacarlos por mis salidas hacia el Barista y el Waiter -----------");
        while (!entry.getBuffer().isEmpty()) {
            Document doc = entry.read();
            outBarista.write(doc);
            outWaiter.write(doc);
        }
    }
}
