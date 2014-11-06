#include "stdio.h"

int stream_state = 0;

int readBool() {
	char input;
	scanf("%c\n", &input);
	
	if (input == 'T')
		return 1;
	else if (input == 'F')
		return 0;

	stream_state = 1;

	return 0;
}

char readChar() {
	char input;
	if (scanf("%c\n", &input) != 1) {
		stream_state = 1;
	}
	return input;
}

int readInt() {
	int input;
	if (scanf("%d\n", &input) != 1) {
		stream_state = 1;
	}
	return input;
}

float readFloat() {
	float input;
	if (scanf("%g\n", &input) != 1) {
		stream_state = 1;
	}
	return input;
}

int getStreamState() {
	return stream_state;
}

