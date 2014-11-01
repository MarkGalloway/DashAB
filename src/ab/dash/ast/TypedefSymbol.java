package ab.dash.ast;

public class TypedefSymbol extends Symbol implements Type {
    Type type;
    public TypedefSymbol(String name, Type type) {
        super(name);
        this.type = type;
    }
    public int getTypeIndex() { return type.getTypeIndex(); }
    public String toString() { return getName(); }
}