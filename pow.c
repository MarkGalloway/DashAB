
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

int main() {
	int a = 2;
	int b = 3;
	int r = powi(a, b);
	return r;
}
