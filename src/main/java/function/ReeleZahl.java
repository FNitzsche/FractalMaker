package function;

public class ReeleZahl implements Zahl {

    float value = 0;

    public ReeleZahl(float v){
        value = v;
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
        if (zahl.isComplex()){
            return new KomplexeZahl(value*zahl.getValue());
        } else {
            return new ReeleZahl(value*zahl.getValue());
        }
    }

    @Override
    public Zahl add(Zahl zahl) {
        if (!zahl.isComplex()){
            return new ReeleZahl(value+zahl.getValue());
        }
        return null;
    }
}
