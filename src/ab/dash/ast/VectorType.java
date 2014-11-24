package ab.dash.ast;

public class VectorType extends Symbol implements Type {
	public Type elementType;
	public int size;
    public VectorType(Type elementType, int size) {
        super(elementType+"[]");
        this.elementType = elementType;
        this.size = size;
    }
    
    public String getName() 
    { 
        return elementType+"[]";
    }
    
    public String toString() {
    	return getName();
    }
    
    public int getTypeIndex() { return SymbolTable.tVECTOR; }
}
