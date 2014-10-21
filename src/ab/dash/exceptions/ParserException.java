package ab.dash.exceptions;

import antlr.RecognitionException;

public class ParserException extends RecognitionException {

	public ParserException(String message) {
		super(message);
	}
}
