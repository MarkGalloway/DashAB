#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

/*
 * Assumes that the vector 'to' has been initialized to the correct size and
 * element type (to be the same as vector 'from').
 */
void copyVector(struct Vector* to, struct Vector* from, size_t element_size) {
	memcpy(to->data, from->data, from->size * element_size);
}

// Declarations

extern int powi(int a,int n);
extern int32_t min(int32_t a, int32_t b);
extern int32_t max(int32_t a, int32_t b);

extern void* xmalloc(size_t n);
extern void xfree(void* ptr);
extern void gc_add_object(void* object, int32_t type);

//////////////////////////
// 	BOOLEAN  	//
//////////////////////////


#define TEMPLATE_NAME bool
#define TEMPLATE_TYPE int8_t
#include "vector.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void bool_VectorNot(struct Vector* out, struct Vector* lhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (!lhs_data[i]) & 1;
}

void bool_VectorOrVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] || rhs_data[i]) & 1;
}

void bool_VectorXOrVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] ^ rhs_data[i]) & 1; 
}

void bool_VectorAndVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] && rhs_data[i]) & 1;
}

void bool_VectorOrScalar(struct Vector* out, struct Vector* lhs, int8_t rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] || rhs) & 1;
}

void bool_VectorXOrScalar(struct Vector* out, struct Vector* lhs, int8_t rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] ^ rhs) & 1; 
}

void bool_VectorAndScalar(struct Vector* out, struct Vector* lhs, int8_t rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] && rhs) & 1;
}

void bool_printVector(struct Vector* vector) {
	int8_t *vector_data = (int8_t*) vector->data;

	for (int i = 0; i < vector->size; i++) {
		if (vector_data[i] == 0)
			printf("F");
		else
			printf("T");

		if (i < vector->size - 1)
			printf(" ");
	}

	printf("\n");
}


//////////////////////////
// 	CHARACTER  	//
//////////////////////////

#define TEMPLATE_NAME char
#define TEMPLATE_TYPE int8_t
#include "vector.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void char_printVector(struct Vector* vector) {
	int8_t *vector_data = (int8_t*) vector->data;

	for (int i = 0; i < vector->size; i++) {
		if (vector_data[i] == '\0')
			break;
		
		printf("%c", vector_data[i]);
	}
}

//////////////////////////
// 	INTEGER  	//
//////////////////////////

#define TEMPLATE_NAME int
#define TEMPLATE_TYPE int32_t
#include "vector.h"
#include "vector_arithmetic.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void int_VectorToReal(struct Vector* out, struct Vector* vector) {
	float *out_data = (float*) out->data;
	int32_t *vector_data = (int32_t*) vector->data;

	for (int i = 0; i < vector->size; i++)
		out_data[i] = (float)vector_data[i];
}

void int_printVector(struct Vector* vector) {
	int32_t *vector_data = (int32_t*) vector->data;

	for (int i = 0; i < vector->size; i++) {
		printf("%d", vector_data[i]);
		
		if (i < vector->size - 1)
			printf(" ");
	}

	printf("\n");
}

//////////////////////////
// 	REALS  		//
//////////////////////////

#define TEMPLATE_NAME real
#define TEMPLATE_TYPE float
#include "vector.h"
#include "vector_arithmetic.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void real_printVector(struct Vector* vector) {
	float *vector_data = (float*) vector->data;

	for (int i = 0; i < vector->size; i++) {
		printf("%g", vector_data[i]);
		
		if (i < vector->size - 1)
			printf(" ");
	}

	printf("\n");
}

//////////////////////////
// 	INTERVAL  	//
//////////////////////////

struct Interval* int_allocInterval(int32_t lower, int32_t upper) {
	struct Interval* interval = (struct Interval*) xmalloc(sizeof(struct Interval));
	interval->lower = lower;
	interval->upper = upper;

	gc_add_object(interval, INTERVAL);

	return interval;
}

void int_assignInterval(struct Interval* dest, struct Interval* value) {
	dest->lower = value->lower;
	dest->upper = value->upper;
}

void int_IntervalAdd(struct Interval* out, struct Interval* lhs, struct Interval* rhs) {
	out->lower = lhs->lower + rhs->lower;
	out->upper = lhs->upper + rhs->upper;
}

void int_IntervalSubtract(struct Interval* out, struct Interval* lhs, struct Interval* rhs) {
	out->lower = lhs->lower - rhs->upper;
	out->upper = lhs->upper - rhs->lower;
}

void int_IntervalMultiply(struct Interval* out, struct Interval* lhs, struct Interval* rhs) {
	int a = lhs->lower;
	int b = lhs->upper;
	int c = rhs->lower;
	int d = rhs->upper;

	int ac = a*c;
	int ad = a*d;
	int bc = b*c;
	int bd = b*d;
	
	out->lower = min( min(ac, ad), min(bc, bd) );
	out->upper = max( max(ac, ad), max(bc, bd) );
}

void int_IntervalDivide(struct Interval* out, struct Interval* lhs, struct Interval* rhs) {
	int a = lhs->lower;
	int b = lhs->upper;
	int c = rhs->lower;
	int d = rhs->upper;

	int ac = a/c;
	int ad = a/d;
	int bc = b/c;
	int bd = b/d;
	
	out->lower = min( min(ac, ad), min(bc, bd) );
	out->upper = max( max(ac, ad), max(bc, bd) );
}

void int_IntervalUniaryMinus(struct Interval* out, struct Interval* lhs) {
	out->lower = -lhs->upper;
	out->upper = -lhs->lower;
}

int int_IntervalEq(struct Interval* lhs, struct Interval* rhs) {
	if (lhs->lower == rhs->lower && lhs->upper == rhs->upper)
		return 1;
	
	return 0;
}

int int_IntervalNe(struct Interval* lhs, struct Interval* rhs) {
	if (lhs->lower == rhs->lower && lhs->upper == rhs->upper)
		return 0;
	
	return 1;
}

int int_IntervalBy(struct Vector* out, struct Interval* lhs, int32_t by) {
	if (by < 1) {
		printf("RuntimeError: Right hand side of by operator must be an integer greater than 0.\n");
		return 1;
	}
	
	int diff = lhs->upper - lhs->lower;
	int size = (int) ceil(((float)(diff + 1))/by);

	int_allocVector(out, size);

	int32_t *out_data = (int32_t*) out->data;

	for (int i = 0; i < size; i++)
		out_data[i] = lhs->lower + i*by;

	return 0;
}

void int_printInterval(struct Interval* interval) {
	for (int i = interval->lower; i <= interval->upper; i++) {
		printf("%d", i);
		
		if (i < interval->upper)
			printf(" ");
	}

	printf("\n");
}

int int_IntervalRange(struct Interval* interval) {
	return interval->upper - interval->lower + 1;
}

//////////////////////////
// 	GENERIC  	//
//////////////////////////

void* getData(struct Vector* v) {
    return v->data;
}

struct Vector* allocVector() {
	struct Vector* vector = (struct Vector*) xmalloc(sizeof(struct Vector));
	vector->size = 0;
	vector->data = 0;

	gc_add_object(vector, VECTOR);
	
	return vector;
}

int32_t getVectorSize(struct Vector* vector) {
	return vector->size;
}

int checkVectorsAreOfSameLength(struct Vector* op1, struct Vector* op2) {
	if (op1->size == op2->size)
		return 1;

	printf("RuntimeError: Vectors are not of same length.");
	return 0;
}

int checkVectorsRHSLength(struct Vector* op1, struct Vector* op2) {
	if (op1->size >= op2->size)
		return 1;

	printf("RuntimeError: Right hand side vector must be smaller or equal to the declared size of left hand side vector.");
	return 0;
}

void printIndexingOutOfBounds() {
	printf("RuntimeError: Vector indexing out of bounds.");
}

void printInvalidBy() {
	printf("RuntimeError: Right hand side of by operator must be an integer greater than 0.\n");
}

