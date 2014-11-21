struct Interval {
	int32_t lower;
	int32_t upper;
};

struct Vector {
	int32_t size;
	void* 	data;
};

enum type { BOOLEAN, CHARACTER, INTEGER, REAL};
