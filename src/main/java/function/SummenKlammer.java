package function;

import java.util.ArrayList;

public class SummenKlammer {

    ArrayList<Produkt> produkte = new ArrayList<>();

    public SummenKlammer(ArrayList<Produkt> p){
        produkte = p;
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

    @Override
    public String toString(){
        String string = "";
        for(Produkt produkt: produkte){
            string.concat(produkt.toString());
        }

        return "+(" + string + ")";
    }

}
