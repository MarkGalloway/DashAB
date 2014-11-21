package ab.dash.ast;

public class MatrixType extends Symbol implements Type {
	public Type elementType;
	public int rows;
	public int columns;
    public MatrixType(Type elementType, int rows, int columns) {
    	super(elementType+"[][]");
    	this.elementType = elementType;
        this.rows = rows;
        this.columns = columns;
    }
    public int getTypeIndex() { return elementType.getTypeIndex(); }
}