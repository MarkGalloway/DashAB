#include <stdio.h>
#include <stdint.h>
#include <ctype.h>


int stream_state = 0;

int whiteSpace(char c) {
	if (c == ' ' || c == '\t' || c == '\f' || 
		c == '\r' || c == '\n')
		return 1;
	
	return 0;
}

int checkEOF(int out) {
	if (out <= 0) {
		stream_state = 2;
		return 1;
	}
	
	return 0;
}

int readBoolean() {
	char input = 0;
	int read = -1;
	stream_state = 0;
	
	while (whiteSpace(input) || (input == 0 && stream_state == 0))
		checkEOF(scanf("%c", &input));

	while (read < 0 && stream_state == 0) {
		if (input == 'T' && read < 0) {
			read = 1;
		} else if (input == 'F' && read < 0) {
			read = 0;
		} else if (!whiteSpace(input)) {
			stream_state = 1;
		}
	}

	return read;
}

int8_t readCharacter() {
	char input;
	stream_state = 0;
	scanf("%c", &input);

	return input;
}

int32_t readInteger() {
	char input = 0;
	int read = 0;
	int integer = 0;
	int sign = 1;
	int digit_found = 0;
	
	stream_state = 0;
	
	while (whiteSpace(input) || (input == 0 && stream_state == 0))
		checkEOF(scanf("%c", &input));
	
	while (!whiteSpace(input)) {
		if (isdigit(input)) {
			integer = integer*10 + ((int)input - '0');
			read = 1;
			digit_found = 1;
		} else if (input == '+' && read == 0) {
			sign = 1;
			read = 1;
		} else if (input == '-' && read == 0) {
			sign = -1;
			read = 1;
		} else if (input == '_' && digit_found == 0) {
			stream_state = 1;
		} else if (input != '_')
			stream_state = 1;

		if (scanf("%c", &input) <= 0)
			break;
	}
	
	integer = integer*sign;

	return integer;
}

float readReal() {
	char input = 0;
	int read = 0;
	char buffer[256];
	int digit_found = 0;

	stream_state = 0;
	
	while (whiteSpace(input) || (input == 0 && stream_state == 0))
		checkEOF(scanf("%c", &input));

	while (!whiteSpace(input)) {
		if (isdigit(input) ||
			input == '+' || input == '-' ||
			input == '.' || input == 'e') {
			buffer[read] = input;
			read++;
			digit_found = 1;
		} else if (input == '_' && digit_found == 0) {
			stream_state = 1;
		} else if (input != '_')
			stream_state = 1;

		if (scanf("%c", &input) <= 0)
			break;
	}


	buffer[read] = '\0';

	char *s = buffer;

	double a = 0.0;
	int e = 0;
	int c;
	int sign = 1;
	
	if (*s == '+') {
		sign = 1;
		s++;	
	} else if (*s == '-') {
		sign = -1;
		s++;
	}

	while ((c = *s++) != '\0' && isdigit(c)) {
		a = a*10.0 + (c - '0');
	}

	if (c == '.') {
		while ((c = *s++) != '\0' && isdigit(c)) {
			a = a*10.0 + (c - '0');
			e = e-1;
		}
	}
	if (c == 'e' || c == 'E') {
		int sign = 1;
		int i = 0;
		c = *s++;
		if (c == '+')
			c = *s++;
		else if (c == '-') {
			c = *s++;
			sign = -1;
		}
		while (isdigit(c)) {
			i = i*10 + (c - '0');
			c = *s++;
		}
		e += i*sign;
	}
	while (e > 0) {
		a *= 10.0;
		e--;
	}
	while (e < 0) {
		a *= 0.1;
		e++;
	}

	if (c != '\0')
		stream_state = 1;

	a = sign*a;

	return a;
}


int powi(int a,int n)
{
	int result = 1;
	int power = n;
	int value = a;
	while(power>0)
	{
		if(power&1)
		{
			result = result*value;
			result = result%1000000007;
		}
		value = value*value;
		value = value%1000000007;
		power >>= 1;
	}
	return result;
}

int getStreamState() {
	return stream_state;
}

