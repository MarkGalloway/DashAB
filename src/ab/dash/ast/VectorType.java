package ab.dash.ast;

public class VectorType extends Symbol implements Type {
	public Type elementType;
	public int size;
    public VectorType(Type elementType, int size) {
        super(elementType+"[]");
        String size_string = "*";
        if (size > 0)
        	size_string = Integer.toString(size);
    	this.name = elementType+"[" + size_string + "]";
        this.elementType = elementType;
        this.size = size;
    }
    public int getTypeIndex() { return 0; }
}
