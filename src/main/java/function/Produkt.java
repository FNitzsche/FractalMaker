package function;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Produkt {

    ArrayList<Zahl> faktoren= new ArrayList();

    public Produkt(ArrayList<Zahl> faktoren){
        this.faktoren = faktoren;
    }

    public Produkt(Zahl zahl){
        faktoren.add(zahl);
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

    public Zahl evaluate(){
        Zahl ret = null;
        for (Zahl zahl:faktoren){
            if (ret == null){
                ret = zahl;
            } else {
                ret = ret.multiply(zahl);
            }
        }
        return ret;
    }

    @Override
    public String toString(){
        String string = "";
        for(Zahl zahl: faktoren){
            if (string.equals("")){
                string = string.concat(zahl.toString());
            } else {
                string = string.concat("*" + zahl.toString());
            }
        }

        return "(" + string + ")";
    }

}
