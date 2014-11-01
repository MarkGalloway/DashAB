package ab.dash.ast;

public class TypedefSymbol extends Symbol implements Type {
    public Type def_type;
    public TypedefSymbol(String name, Type type) {
        super(name);
        this.def_type = type;
    }
    public int getTypeIndex() { return def_type.getTypeIndex(); }
    public String toString() { return getName(); }
}