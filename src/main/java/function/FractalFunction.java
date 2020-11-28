package function;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import jdk.tools.jaotc.Main;
import main.AppStart;
import main.FractalScreenCon;

import java.util.ArrayList;

public class FractalFunction {

    public ArrayList<float[]> nullStellen = new ArrayList<>();

    public SummenKlammer function = null;

    Zahl[] variable = {new ReeleZahl(0, false), new KomplexeZahl(0, false)};

    public SimpleStringProperty functionString = new SimpleStringProperty("No Function");

    public boolean changed = true;

    float sIm = 0;
    float sRe = 0;

    AppStart app;

    public FractalFunction(AppStart appStart){
        app = appStart;
    }


    public void addN(float[] n){
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
        sIm = app.startIm;
        sRe = app.startReel;
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
            //System.out.println(function);
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
        float real = sRe, img = sIm;
        L:
        for (int i = 0; i < reps; i++){
            variable[0].setValue(real);
            variable[1].setValue(img);
            Zahl[] tmp = function.evaluate();
            real = tmp[0].getValue() + point[0];
            img = tmp[1].getValue() + point[1];
            if (Math.pow(real, 2)+Math.pow(img, 2) > border){
                iter = i;
                break L;
            }
        }
        //System.out.println(iter);
        return iter;
    }

}
