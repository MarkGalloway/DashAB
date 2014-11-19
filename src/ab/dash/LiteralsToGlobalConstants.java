package ab.dash;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;

/*
 * Move constant string, tuple, array, and matrix literals to global variables,
 * so the (complex) literal values won't have to be constructed every time they
 * are evaluated.
 *
 * For example, transform the following code,
 *
 *     procedure main() returns integer {
 *     ...
 *        a = b + [1, 2, 3];
 *     ...
 *     }
 *
 * into something like this,
 *
 *     const __constant_1762 = [1, 2, 3]
 *
 *     procedure main() returns integer {
 *     ...
 *        a = b + __constant_1762;
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
 * Be sure to run this pass after the constant folding and propogation
 * optimizations.
 *
 */
public class LiteralsToGlobalConstants {
	public LiteralsToGlobalConstants() {
	}

	public void walk(DashAST t) {
		/* TODO: Implement. */
	}
}
