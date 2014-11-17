package ab.dash.ast;

public class MatrixType extends Symbol implements Type {
	public Type elementType;
	public int rows;
	public int columns;
    public MatrixType(Type elementType, int rows, int columns) {
    	super(elementType+"[][]");
    	String rows_string = "*";
    	String columns_string = "*";
    	if (rows > 0)
    		rows_string = Integer.toString(rows);
    	if (columns > 0)
    		columns_string = Integer.toString(columns);
    	this.name = elementType+"[" + rows_string + "][" + columns_string + "]";
        this.elementType = elementType;
        this.rows = rows;
        this.columns = columns;
    }
    public int getTypeIndex() { return 0; }
}