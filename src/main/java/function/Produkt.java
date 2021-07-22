package function;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Produkt {

    ArrayList<Zahl> faktoren= new ArrayList();

    public boolean isNull = false;
    public boolean fest = false;

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

    public Zahl evaluate(int hash){
        Zahl ret = null;
        for (Zahl zahl:faktoren){
            if (ret == null){
                ret = zahl;
            } else {
                ret = ret.multiply(zahl, hash);
            }
        }
        return ret;
    }

    public Zahl shortenInPlace(){
        Zahl ret = null;
        boolean noNull = true;
        boolean noLoose = true;
        Zahl combined = null;
        ArrayList<Zahl> toRemove = new ArrayList<>();

        L:
        for (Zahl zahl:faktoren){
            if (zahl.isFinal() && zahl.getValue() == 0){
                noNull = false;
                break L;
            } else if (zahl.isFinal()){
                if (combined == null){
                    combined = zahl;
                    toRemove.add(zahl);
                } else {
                    combined = combined.multiply(zahl, -1);
                    toRemove.add(zahl);
                }
            } else if (!zahl.isFinal()){
                noLoose = false;
            }
        }
        isNull = !noNull;
        fest = noLoose;

        if (noLoose && noNull){

            ret = combined;
        } else if (noNull && combined != null){
            faktoren.removeAll(toRemove);
            faktoren.add(combined);
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
