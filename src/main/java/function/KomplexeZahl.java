package function;

import java.util.stream.Stream;

public class KomplexeZahl implements Zahl {

    double value = 0;
    boolean fest = false;

    public KomplexeZahl(double v, boolean fest){
        value = v;
        this.fest = fest;
    }

    @Override
    public boolean isFinal() {
        return fest;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void setValue(double v) {
        value = v;
    }

    @Override
    public Zahl multiply(Zahl zahl) {
            if (!zahl.isComplex()) {
                return new KomplexeZahl(value * zahl.getValue(), true);
            } else {
                return new ReeleZahl(-(value * zahl.getValue()), true);
            }
    }

    @Override
    public Zahl add(Zahl zahl) {
        if (zahl.isComplex()){
            return new KomplexeZahl(value+zahl.getValue(), true);
        }
        return null;
    }

    @Override
    public String toString(){
        if (fest) {
            return "(" + value + "i)";
        } else {
            return "(z" + this.hashCode() + ")";
        }
    }
}
