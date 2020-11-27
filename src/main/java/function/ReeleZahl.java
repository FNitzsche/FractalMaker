package function;

public class ReeleZahl implements Zahl {

    float value = 0;
    boolean fest = false;

    public ReeleZahl(float v, boolean fest){
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
    public float getValue() {
        return value;
    }

    @Override
    public Zahl multiply(Zahl zahl) {
        if (fest && zahl.isFinal()) {
            if (zahl.isComplex()) {
                return new KomplexeZahl(value * zahl.getValue(), true);
            } else {
                return new ReeleZahl(value * zahl.getValue(), true);
            }
        }
        return null;
    }

    @Override
    public Zahl add(Zahl zahl) {
        if (!zahl.isComplex() && fest && zahl.isFinal()){
            return new ReeleZahl(value+zahl.getValue(), true);
        }
        return null;
    }

    @Override
    public String toString(){
        if (fest) {
            return " +(" + value + ")";
        } else {
            return " +(r" + this.hashCode() + ")";
        }
    }
}
