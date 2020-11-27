package function;

public interface Zahl {
    public boolean isFinal();
    public boolean isComplex();
    public float getValue();
    public void setValue(float v);
    public Zahl multiply(Zahl zahl);
    public Zahl add(Zahl zahl);
}
