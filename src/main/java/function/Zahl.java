package function;

public interface Zahl {
    public boolean isFinal();
    public boolean isComplex();
    public double getValue();
    public double getValue(int hash);
    public void setValue(double v);
    public void addValue(double v, int hash);
    public void clearValues();
    public Zahl multiply(Zahl zahl, int hash);
    public Zahl add(Zahl zahl, int hash);
    public int vSize();
}
