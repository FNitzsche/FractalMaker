package function;

public interface Zahl {
    public boolean isFinal();
    public boolean isComplex();
    public double getValue();
    public void setValue(double v);
    public Zahl multiply(Zahl zahl);
    public Zahl add(Zahl zahl);
}
