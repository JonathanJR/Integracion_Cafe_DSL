package cafe;

public class Correlator {

    Slot entryBarista, entryReplicator, outBarista, outReplicator;

    public Correlator(Slot entryBarista, Slot entryReplicator, Slot outBarista, Slot outReplicator) {
        this.entryBarista = entryBarista;
        this.entryReplicator = entryReplicator;
        this.outBarista = outBarista;
        this.outReplicator = outReplicator;
    }

    public void doWork() {
        System.out.println("\n----------- Soy el Correlator y comienzo a encaminar documentos desde mis entradas a mis salidas hacia el ContextEnricher -----------");
        while (!entryBarista.getBuffer().isEmpty()) {
            outBarista.write(entryBarista.read());
        }
        while (!entryReplicator.getBuffer().isEmpty()) {
            outReplicator.write(entryReplicator.read());
        }
    }
}
