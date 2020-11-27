package function;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Produkt {

    ArrayList<Zahl> faktoren= new ArrayList();

    public Produkt(ArrayList<Zahl> faktoren){
        this.faktoren = faktoren;
    }

    public void addFactor(Zahl zahl){
        faktoren.add(zahl);
    }

    public ArrayList<Zahl> getFactoren(){
        return faktoren;
    }

    public Produkt multiply(Produkt produkt){
        ArrayList<Zahl> combined = new ArrayList<>();
        combined.addAll(faktoren);
        combined.addAll(produkt.getFactoren());
        return new Produkt(combined);
    }

    public SummenKlammer multiply(SummenKlammer klammer){
        ArrayList<Produkt> list = new ArrayList<>();
        for (Produkt pr: klammer.getProdukte()){
            list.add(pr.multiply(this));
        }
        return new SummenKlammer(list);
    }

    @Override
    public String toString(){
        String string = "";
        for(Zahl zahl: faktoren){
            string.concat(zahl.toString());
        }

        return "+(" + string + ")";
    }

}
