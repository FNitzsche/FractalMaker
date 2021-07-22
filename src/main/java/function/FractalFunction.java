package function;

import javafx.beans.property.SimpleStringProperty;
import main.AppStart;

import java.util.ArrayList;

public class FractalFunction {

    public ArrayList<double[]> nullStellen = new ArrayList<>();

    public SummenKlammer function = null;

    Zahl[] variable = {new ReeleZahl(0, false), new KomplexeZahl(0, false)};

    public SimpleStringProperty functionString = new SimpleStringProperty("No Function");

    public boolean changed = true;

    double sIm = 0;
    double sRe = 0;

    AppStart app;

    public FractalFunction(AppStart appStart){
        app = appStart;
    }


    public void addN(double[] n){
        nullStellen.add(n);
        functionString.set(toString());
        changed = true;
    }

    public void removeN(int n){
        nullStellen.remove(n);
        functionString.set(toString());
        changed = true;
    }

    public void clearN(){
        nullStellen.clear();
        functionString.set(toString());
        changed = true;
    }

    @Override
    public String toString(){
        String string = "";
        for (double[] n:nullStellen){
            if (string.equals("")){
                string = string.concat("(z-(" + n[0] + "+" + n[1] + "i))");
            } else {
                string = string.concat(" * (z-(" + n[0] + "+" + n[1] + "i))");
            }
        }
        return string;
    }

    public void createFunction(){

        variable[0].clearValues();
        variable[1].clearValues();

        sIm = app.startIm;
        sRe = app.startReel;
        if (nullStellen.size() > 0){
            SummenKlammer v = null;
            for (double[] n: nullStellen){
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
            //System.out.println(function);
        } else {
            function = null;
            System.out.println("No Function");
        }
    }

    public int calculatePoint(double[] point, int reps, double border){
        if (function == null){
            return -2;
        }
        int iter = -1;
        double real = sRe, img = sIm;
        int hash = point.hashCode();
        L:
        for (int i = 0; i < reps; i++){
            variable[0].addValue(real, hash);
            variable[1].addValue(img, hash);
            //System.out.println(variable[0].getValue(hash));
            Zahl[] tmp = function.evaluate(hash);
            real = tmp[0].getValue(hash) + point[0];
            img = tmp[1].getValue(hash) + point[1];
            if (Math.pow(real, 2)+Math.pow(img, 2) > border){
                iter = i;
                break L;
            }
        }
        //System.out.println(iter);
        return iter;
    }

}
