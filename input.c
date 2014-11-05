#include "stdio.h"

int stream_state = 0;

int readBool() {
	char input;
	scanf("%c", &input);
	
	if (input == 'T')
		return 1;
	else if (input == 'F')
		return 0;

	stream_state = 1;

	return 0;
}

char readChar() {
	char input;
	scanf("%c", &input);
	return input;
}

int readInt() {
	int input;
	scanf("%d", &input);
	return input;
}

float readFloat() {
	float input;
	scanf("%g", &input);
	return input;
}

int main(void) 
{
   printf("%d", readInt());
 
   return 0;
}
