package ab.dash.ast;

public class IntervalType extends Symbol implements Type {
	public int lower;
	public int upper;
    public IntervalType(int lower, int upper) {
        super("interval");
        this.lower = lower;
        this.upper = upper;
    }
    public int getTypeIndex() { return SymbolTable.tINTEGER; }
}