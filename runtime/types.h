#ifndef TYPES_H_
#define TYPES_H_
struct Interval {
	int32_t lower;
	int32_t upper;
};

struct Vector {
	int32_t size;
	void* 	data;
};

struct Matrix {
	int32_t rows;
	int32_t columns;
	void* 	data;
};

enum gc_type {INTERVAL, VECTOR, MATRIX};

#endif // TYPES_H_
