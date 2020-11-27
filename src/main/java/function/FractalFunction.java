package function;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import jdk.tools.jaotc.Main;

import java.util.ArrayList;

public class FractalFunction {

    public ArrayList<float[]> nullStellen = new ArrayList<>();

    SummenKlammer function = null;

    Zahl[] variable = {new ReeleZahl(0, false), new KomplexeZahl(0, false)};

    public SimpleStringProperty functionString = new SimpleStringProperty("No Function");


    public void addN(float[] n){
        nullStellen.add(n);
        functionString.set(toString());
    }

    public void removeN(int n){
        nullStellen.remove(n);
        functionString.set(toString());
    }

    @Override
    public String toString(){
        String string = "";
        for (float[] n:nullStellen){
            if (string.equals("")){
                string = string.concat("(z-(" + n[0] + "+" + n[1] + "i))");
            } else {
                string = string.concat(" * (z-(" + n[0] + "+" + n[1] + "i))");
            }
        }
        return string;
    }

    public void createFunction(){
        if (nullStellen.size() > 0){
            SummenKlammer v = null;
            for (float[] n: nullStellen){
                SummenKlammer stelle = new SummenKlammer(-n[0], -n[1], true);
                stelle.addProdukt(new Produkt(variable[0]));
                stelle.addProdukt(new Produkt(variable[1]));
                if (v == null){
                    v = stelle;
                } else {
                    v = v.multiply(stelle);
                }
            }
            function = v;
            System.out.println(function);
        } else {
            function = null;
            System.out.println("No Function");
        }
    }

    public int calculatePoint(float[] point, int reps, float border){
        if (function == null){
            return -2;
        }
        int iter = -1;
        L:
        for (int i = 0; i < reps; i++){
            variable[0].setValue(point[0]);
            variable[1].setValue(point[1]);
            Zahl[] tmp = function.evaluate();
            point[0] = tmp[0].getValue();
            point[1] = tmp[1].getValue();
            if (Math.pow(point[0], 2)+Math.pow(point[1], 2) > border){
                iter = i;
                break L;
            }
        }
        return iter;
    }

}
