#include <stdio.h>
#include <stdint.h>

int stream_state = 0;

int readBoolean() {
	char input;
	scanf("%c\n", &input);
	
	if (input == 'T')
		return 1;
	else if (input == 'F')
		return 0;

	stream_state = 1;

	return 0;
}

int8_t readCharacter() {
	char input;
	if (scanf("%c\n", &input) != 1) {
		stream_state = 1;
	}
	return input;
}

int32_t readInteger() {
	int input;
	if (scanf("%d\n", &input) != 1) {
		stream_state = 1;
	}
	return input;
}

float readReal() {
	float input;
	if (scanf("%g\n", &input) != 1) {
		stream_state = 1;
	}
	return input;
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

