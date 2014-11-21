package ab.dash;

import java.util.Arrays;
import java.util.List;

import ab.dash.DashLexer;
import ab.dash.ast.*;

/*
 * Move constant string, tuple, array, and matrix literals to global variables,
 * so the (complex) literal values won't have to be constructed every time they
 * are evaluated.
 *
 * For example, transform the following code,
 *
 *     const z = 2;
 *
 *     procedure main() returns integer {
 *     ...
 *        a = b + [1, 2, 3];
 *        c = d + [4, z, 5];
 *     ...
 *     }
 *
 * into something like this,
 *
 *     const z = 2;
 *     const __constant_1762 = [1, 2, 3]
 *     const __constant_1763 = [1, z, 3]
 *
 *     procedure main() returns integer {
 *     ...
 *        a = b + __constant_1762;
 *        c = d + __constant_1763;
 *     ...
 *     }
 *
 * This optimization is expected to provide significant performance gains when
 * literals are used inside loops.
 *
 * NOTES:
 *
 * Be sure to apply this to static ranges, such as the following:
 *
 *     a = b + 1..7
 *
 * Be sure to run this pass after the constant folding and propagation
 * optimizations.
 *
 */
public class LiteralsToGlobalConstants {
	private List<Integer> primaryLiterals;
	private DashAST programNode;

	public LiteralsToGlobalConstants(DashAST root) {
		primaryLiterals = (Arrays.asList(new Integer[] {
				DashLexer.True,
				DashLexer.False,
				DashLexer.INTEGER,
				DashLexer.REAL,
				DashLexer.CHARACTER
		}));

		programNode = (DashAST) root;
		if (root.getType() != DashLexer.PROGRAM) {
			/* TODO: Throw exception. */
		}
	}

	public void walk(DashAST t) {

		switch (t.getType()) {
		case DashLexer.TUPLE_LIST:
			if (childrenExprsAreLiterals(t)) {
				/* TODO: create a global variable with this value. */
				/* TODO: swap this tuple with a use of the new global variable. */
			}
			break;
		/* TODO: Handle other data types. */
		}

		for (int i = 0; i < t.getChildCount(); i++) {
			DashAST child = (DashAST) t.getChild(i);
			walk(child);
		}
	}

	private boolean childrenExprsAreLiterals(DashAST t) {
		DashAST child;

		for (int i = 0; i < t.getChildCount(); i++) {
			child = (DashAST) t.getChild(i);

			if (child.getType() == DashLexer.EXPR) {
				if (!exprIsLiteral(child)) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean exprIsLiteral(DashAST expr) {
		DashAST exprContent = (DashAST) expr.getChild(0);

		int astType = exprContent.getType();

		if (primaryLiterals.contains(astType)) {
			return true;
		}

		/* TODO: Detect more possible valid cases. */

		return false;
	}

	private VariableSymbol createGlobal(DashAST rhsExpr) {
		/* TODO: Create new variable symbol. */
		/* TODO: Insert VAR_DECL node under root, after the latest other globals var declarations. */
		return null;
	}
}
