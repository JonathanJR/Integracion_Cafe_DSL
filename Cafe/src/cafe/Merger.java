package cafe;

public class Merger {

    Slot entryHot, entryCold, outAgregator;

    public Merger(Slot entryHot, Slot entryCold, Slot outAgregator) {
        this.entryHot = entryHot;
        this.entryCold = entryCold;
        this.outAgregator = outAgregator;
    }

    public void doWork() {
        System.out.println("\n----------- Soy el Merger y comienzo a encaminar documentos que me llegan desde los ContextEnricher hacia el Agregator -----------");

        while (!entryHot.getBuffer().isEmpty()) {
            outAgregator.write(entryHot.read());
        }
        while (!entryCold.getBuffer().isEmpty()) {
            outAgregator.write(entryCold.read());
        }
    }
}
