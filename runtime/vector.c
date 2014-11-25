#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

/*
int32_t getBooleanTypeId() {
	return BOOLEAN;
}

int32_t getCharacterTypeId() {
	return CHARACTER;
}

int32_t getIntegerTypeId() {
	return INTEGER;
}

int32_t getRealTypeId() {
	return REAL;
}
*/

inline int32_t min(int32_t a, int32_t b) {
	if (a < b)
		return a;
	return b;
}

inline int32_t max(int32_t a, int32_t b) {
	if (a > b)
		return a;
	return b;
}

/*
 * Assumes that the vector 'to' has been initialized to the correct size and
 * element type (to be the same as vector 'from').
 */
inline void copyVector(struct Vector* to, struct Vector* from, size_t element_size) {
	memcpy(to->data, from->data, from->size * element_size);
}

// Declarations

extern int powi(int a,int n);

void int_allocVector(struct Vector* vector, int32_t size);

//////////////////////////
// 	INTERVAL  	//
//////////////////////////

void int_allocInterval(struct Interval* interval, int32_t lower, int32_t upper) {
	interval = (struct Interval*) malloc(sizeof(struct Interval));
	interval->lower = lower;
	interval->upper = upper;
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
	out->lower = -lhs->lower;
	out->upper = -lhs->upper;
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
	if (by < 1)
		return 1;
	
	int diff = lhs->upper - lhs->lower;
	int size = (int) ceil(((float)(diff + 1))/by);

	int_allocVector(out, size);

	int32_t *out_data = (int32_t*) out->data;

	for (int i = 0; i < size; i++)
		out_data[i] = lhs->lower + i*by;

	return 0;
}

void int_releaseInterval(struct Interval* interval) {
	free(interval);
}

//////////////////////////
// 	BOOLEAN  	//
//////////////////////////

void bool_copyVector(struct Vector* to, struct Vector* from) {
	copyVector(to, from, sizeof(int8_t));
}

void bool_allocVector(struct Vector* vector, int32_t size) {
	vector->size = size;
	vector->data = malloc(sizeof(int8_t) * size);
}

int bool_getElement(int8_t* out, struct Vector* vector, int32_t index) {
	if (index > vector->size || index < 1)
		return 1;

	*out = ((int8_t*)vector->data)[index - 1];
	
	return 0;
}

int bool_setElement(struct Vector* vector, int32_t index, int8_t value) {
	if (index > vector->size || index < 1)
		return 1;

	((int8_t*)vector->data)[index - 1] = value;
	
	return 0;
}

void bool_assignVector(struct Vector* lhs, struct Vector* rhs) {
	int8_t* lhs_data = (int8_t*)lhs->data;
	int8_t* rhs_data = (int8_t*)rhs->data;

	for (int i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs_data[i];
}

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

int bool_VectorEq(struct Vector* lhs, struct Vector* rhs) {
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 1;

	return 0;
}

int bool_VectorNe(struct Vector* lhs, struct Vector* rhs) {
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 0;

	return 1;
}

//////////////////////////
// 	CHARACTER  	//
//////////////////////////

void char_allocVector(struct Vector* vector, int32_t size) {
	vector->size = size;
	vector->data = malloc(sizeof(int8_t) * size);
}

void char_copyVector(struct Vector* to, struct Vector* from) {
	copyVector(to, from, sizeof(int8_t));
}

int char_getElement(int8_t* out, struct Vector* vector, int32_t index) {
	if (index > vector->size || index < 1)
		return 1;

	*out = ((int8_t*)vector->data)[index - 1];
	
	return 0;
}

int char_setElement(struct Vector* vector, int32_t index, int8_t value) {
	if (index > vector->size || index < 1)
		return 1;

	((int8_t*)vector->data)[index - 1] = value;
	
	return 0;
}

void char_assignVector(struct Vector* lhs, struct Vector* rhs) {
	int8_t* lhs_data = (int8_t*)lhs->data;
	int8_t* rhs_data = (int8_t*)rhs->data;

	for (int i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs_data[i];
}

int char_VectorEq(struct Vector* lhs, struct Vector* rhs) {
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 1;

	return 0;
}

int char_VectorNe(struct Vector* lhs, struct Vector* rhs) {
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 0;

	return 1;
}

//////////////////////////
// 	INTEGER  	//
//////////////////////////

void int_allocVector(struct Vector* vector, int32_t size) {
	vector->size = size;
	vector->data = malloc(sizeof(int32_t) * size);
}

void int_copyVector(struct Vector* to, struct Vector* from) {
	copyVector(to, from, sizeof(int32_t));
}

int int_getElement(int32_t* out, struct Vector* vector, int32_t index) {
	if (index > vector->size || index < 1)
		return 1;

	*out = ((int32_t*)vector->data)[index - 1];
	
	return 0;
}

int int_setElement(struct Vector* vector, int32_t index, int32_t value) {
	if (index > vector->size || index < 1)
		return 1;

	((int32_t*)vector->data)[index - 1] = value;
	
	return 0;
}

void int_assignVector(struct Vector* lhs, struct Vector* rhs) {
	int32_t* lhs_data = (int32_t*)lhs->data;
	int32_t* rhs_data = (int32_t*)rhs->data;

	for (int i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs_data[i];
}

void int_VectorAddVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] + rhs_data[i];
}

void int_VectorSubtractVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] - rhs_data[i];
}

void int_VectorMultiplyVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] * rhs_data[i];
}

void int_VectorDivideVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] / rhs_data[i];
}

void int_VectorModulusVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] % rhs_data[i];
}

void int_VectorPowerVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = powi(lhs_data[i], rhs_data[i]);
}

void int_VectorUniaryMinus(struct Vector* out, struct Vector* lhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = -lhs_data[i];
}

void int_VectorDot(int32_t *result, struct Vector* lhs, struct Vector* rhs) {
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;

	*result = 0;
	for (int i = 0; i < lhs->size; i++)
		*result += lhs_data[i]*rhs_data[i];
}

int int_VectorEq(struct Vector* lhs, struct Vector* rhs) {
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 1;

	return 0;
}

int int_VectorNe(struct Vector* lhs, struct Vector* rhs) {
	int32_t *lhs_data = (int32_t*) lhs->data;
	int32_t *rhs_data = (int32_t*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 0;

	return 1;
}

//////////////////////////
// 	GENERIC  	//
//////////////////////////

void* getData(struct Vector* v) {
    return v->data;
}

struct Vector* allocVector() {
    return (struct Vector*) malloc(sizeof(struct Vector));
}

int32_t getVectorSize(struct Vector* vector) {
	return vector->size;
}

int areVectorsOfSameLength(struct Vector* op1, struct Vector* op2) {
	return op1->size == op2->size;
}

void releaseVector(struct Vector* vector) {
	free(vector->data);
	free(vector);
}

