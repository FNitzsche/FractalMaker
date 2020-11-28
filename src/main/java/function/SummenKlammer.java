package function;

import java.util.ArrayList;
import java.util.HashMap;

public class SummenKlammer {

    ArrayList<Produkt> produkte = new ArrayList<>();

    public SummenKlammer(ArrayList<Produkt> p){
        produkte = p;
    }

    public SummenKlammer(Zahl reeleZahl, Zahl komplexeZahl){
        produkte.add(new Produkt(reeleZahl));
        produkte.add(new Produkt(komplexeZahl));
    }

    public SummenKlammer(double reeleZahl, double komplexeZahl, boolean fest){
        produkte.add(new Produkt(new ReeleZahl(reeleZahl, fest)));
        produkte.add(new Produkt(new KomplexeZahl(komplexeZahl, fest)));
    }

    public SummenKlammer(Produkt produkt){
        produkte.add(produkt);
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
        double vKomplex = 0;
        double vReel = 0;
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

    public Zahl[] shortenInPlace(){
        boolean noLoose = true;
        Zahl combinedReel = null;
        Zahl combinedImg = null;
        ArrayList<Produkt> toRemove = new ArrayList<>();

        L:
        for (Produkt produkt:produkte){
            Zahl zahl = produkt.shortenInPlace();
            if (zahl != null){
                if (zahl.isComplex()){
                    if (combinedImg == null){
                        combinedImg = zahl;
                    } else {
                        combinedImg = combinedImg.add(zahl);
                    }
                } else {
                    if (combinedReel == null){
                        combinedReel = zahl;
                    } else {
                        combinedReel = combinedReel.add(zahl);
                    }
                }
                toRemove.add(produkt);
            } else {
                if (produkt.isNull){
                    toRemove.add(produkt);
                } else if (!produkt.fest){
                    noLoose = false;
                }
            }
        }

        produkte.removeAll(toRemove);
        if (combinedReel != null) {
            produkte.add(new Produkt(combinedReel));
        }
        if (combinedImg != null) {
            produkte.add(new Produkt(combinedImg));
        }

        if (noLoose) {
            return new Zahl[] {combinedReel, combinedImg};
        }
        return null;
    }

    public ArrayList<ArrayList<Integer>> transcribeFunction(ArrayList<Integer> pos){
        HashMap<Integer, Integer> positions = new HashMap<>();
        int lastPos = pos.size();
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
        for (Produkt produkt:produkte){
            ArrayList<Integer> tmp = new ArrayList<>();
            for(Zahl zahl:produkt.getFactoren()){
                int h = zahl.hashCode();
                if (!positions.containsKey(h)){
                    if (!pos.contains(h)) {
                        positions.put(h, lastPos++);
                    } else {
                        positions.put(h, pos.indexOf(h));
                    }
                }
                tmp.add(positions.get(h));
            }
            ret.add(tmp);
        }
        return ret;
    }

    public ArrayList<Double> transcribeValues(ArrayList<Integer> pos){
        HashMap<Integer, Integer> positions = new HashMap<>();
        int lastPos = pos.size();
        ArrayList<Double> ret = new ArrayList<>();

        for (Produkt produkt:produkte){
            for(Zahl zahl:produkt.getFactoren()){
                int h = zahl.hashCode();
                if (!positions.containsKey(h)){
                    if (!pos.contains(h)) {
                        positions.put(h, lastPos++);
                    } else {
                        positions.put(h, pos.indexOf(h));
                    }
                    ret.add(positions.get(h), zahl.getValue());
                }
            }
        }
        return ret;
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
