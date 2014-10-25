package ab.dash.exceptions;

import antlr.RecognitionException;

public class LexerException extends RecognitionException {

	public LexerException(String message) {
		super(message);
	}
}
