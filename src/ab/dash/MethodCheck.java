package ab.dash;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;

public class MethodCheck {
	int error_count = 0;
	StringBuffer errorSB = new StringBuffer();

	public MethodCheck() {
	}

	public void emitErrorMessage(String msg) {
		System.err.println(msg);
		error_count++;
		errorSB.append(msg);
	}

	public int getErrorCount() {
		return error_count;
	}

	public String getErrors() {
		return errorSB.toString();
	}

	public void check(DashAST t) {
		switch (t.getType()) {
		case DashLexer.CALL: {
			DashAST call_id = (DashAST) t.getChild(0);
			DashAST def = call_id.symbol.def;

			if (def != null) {
				if (def.hasAncestor(DashLexer.PROCEDURE_DECL)
						&& call_id.hasAncestor(DashLexer.FUNCTION_DECL)) {
					emitErrorMessage("line " + call_id.getLine() + ": Can not call procedure inside function.");
				}
			}
			break;
		}
		}

		for (int i = 0; i < t.getChildCount(); i++) {
			DashAST child = (DashAST) t.getChild(i);
			check(child);
		}
	}
}
