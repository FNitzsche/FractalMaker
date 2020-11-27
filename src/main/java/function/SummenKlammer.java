package function;

import java.util.ArrayList;

public class SummenKlammer {

    ArrayList<Produkt> produkte = new ArrayList<>();

    public SummenKlammer(ArrayList<Produkt> p){
        produkte = p;
    }

    public SummenKlammer(Zahl reeleZahl, Zahl komplexeZahl){
        produkte.add(new Produkt(reeleZahl));
        produkte.add(new Produkt(reeleZahl));
    }

    public SummenKlammer(float reeleZahl, float komplexeZahl, boolean fest){
        produkte.add(new Produkt(new ReeleZahl(reeleZahl, fest)));
        produkte.add(new Produkt(new KomplexeZahl(komplexeZahl, fest)));
    }

    public void addProdukt(Produkt produkt){
        produkte.add(produkt);
    }

    public ArrayList<Produkt> getProdukte(){
        return produkte;
    }

    public SummenKlammer multiply(SummenKlammer klammer){
        ArrayList<Produkt> list = new ArrayList<>();
        for (Produkt pr: produkte){
            list.addAll(pr.multiply(klammer).getProdukte());
        }
        return new SummenKlammer(list);
    }

    public Zahl[] evaluate(){
        float vKomplex = 0;
        float vReel = 0;
        for (Produkt produkt: produkte){
            Zahl zahl = produkt.evaluate();
            if (zahl != null) {
                if (zahl.isComplex()) {
                    vKomplex += zahl.getValue();
                } else {
                    vReel += zahl.getValue();
                }
            }
        }
        return new Zahl[]{new ReeleZahl(vReel, true), new KomplexeZahl(vKomplex, true)};
    }

    @Override
    public String toString(){
        String string = "";
        for(Produkt produkt: produkte){
            if (string.equals("")){
                string = string.concat(produkt.toString());
            } else {
                string = string.concat(" + " + produkt.toString());
            }
        }

        return "(" + string + ")";
    }

}
