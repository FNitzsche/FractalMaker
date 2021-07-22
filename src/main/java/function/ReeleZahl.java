package function;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ReeleZahl implements Zahl {

    double value = 0;
    boolean fest = false;

    ConcurrentHashMap<Integer, Double> valueMap = new ConcurrentHashMap<Integer, Double>();

    public ReeleZahl(double v, boolean fest){
        value = v;
        this.fest = fest;
    }

    @Override
    public boolean isFinal() {
        return fest;
    }

    @Override
    public boolean isComplex() {
        return false;
    }

    @Override
    public double getValue() {
        return value;
    }

    public double getValue(int hash){
        if (fest == true){
            //System.out.println("fest");
            return value;
        }
        //System.out.println("nichtfest");
        if (valueMap.containsKey((Integer) hash)) {
            return valueMap.get((Integer) hash);
        }
        return -1;
    }

    @Override
    public void setValue(double v) {
        value = v;
    }

    @Override
    public void addValue(double v, int hash) {
        if (valueMap.containsKey(hash)){
            valueMap.remove(hash);
        }
        valueMap.put(hash, v);
    }

    @Override
    public void clearValues() {
        valueMap.clear();
    }

    @Override
    public Zahl multiply(Zahl zahl, int hash) {
            if (zahl.isComplex()) {
                return new KomplexeZahl(this.getValue(hash) * zahl.getValue(hash), true);
            } else {
                return new ReeleZahl(this.getValue(hash) * zahl.getValue(hash), true);
            }
    }

    @Override
    public Zahl add(Zahl zahl, int hash) {
        if (!zahl.isComplex()){
            return new ReeleZahl(this.getValue(hash)+zahl.getValue(hash), true);
        }
        return null;
    }

    @Override
    public int vSize() {
        return valueMap.size();
    }

    @Override
    public String toString(){
        if (fest) {
            return "(" + value + ")";
        } else {
            return "(r" + this.hashCode() + ")";
        }
    }
}
