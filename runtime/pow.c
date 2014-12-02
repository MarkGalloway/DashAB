#include <stdint.h>
#include <math.h>

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

int32_t min(int32_t a, int32_t b) {
	if (a < b)
		return a;
	return b;
}

int32_t max(int32_t a, int32_t b) {
	if (a > b)
		return a;
	return b;
}

int32_t int_mod(int32_t a, int32_t b) {
	return fmodf(a, b);
}

int32_t int_power(int32_t a, int32_t b) {
	return powi(a, b);
}

float real_mod(float a, float b) {
	return fmodf(a, b);
}

float real_power(float a, float b) {
	return powf(a, b);
}
