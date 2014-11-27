package ab.dash.memory;

public class VariableMemoryNode implements MemoryNode {
	public int id;
	public int type;
	public boolean local;
	
	public VariableMemoryNode(int id, int type, boolean local) {
		this.id = id;
		this.type = type;
		this.local = local;
	}
	
	public int getNodeType() { return MemoryManagment.VARIABLE_MEMORY_NODE; }
}
