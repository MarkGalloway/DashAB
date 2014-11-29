#include <stdio.h>
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

/*
 * Assumes that the vector 'to' has been initialized to the correct size and
 * element type (to be the same as vector 'from').
 */
void copyVector(struct Vector* to, struct Vector* from, size_t element_size) {
	memcpy(to->data, from->data, from->size * element_size);
}

// Declarations

extern int powi(int a,int n);
extern void* xmalloc(size_t n);
extern void xfree(void* ptr);
extern void gc_add_object(void* object, int32_t type);

void int_allocVector(struct Vector* vector, int32_t size);

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
	for (int i = interval->lower; i <= interval->upper; i++)
		printf("%d", i);
}

int int_IntervalRange(struct Interval* interval) {
	return interval->upper - interval->lower + 1;
}

void int_releaseInterval(struct Interval* interval) {
	xfree(interval);
}

//////////////////////////
// 	BOOLEAN  	//
//////////////////////////

void bool_copyVector(struct Vector* to, struct Vector* from) {
	copyVector(to, from, sizeof(int8_t));
}

void bool_allocVector(struct Vector* vector, int32_t size) {
	if (vector->size != 0) {
		// TODO: Error
	} else {
		vector->size = size;
		vector->data = xmalloc(sizeof(int8_t) * size);
	}
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
	
	int i;
	for (i = 0; i < lhs->size && i < rhs->size; i++)
		lhs_data[i] = rhs_data[i];
	
	while (i < lhs->size) {
		lhs_data[i] = 0;
		i++;
	}
}

void bool_assignVectorScalar(struct Vector* lhs, int8_t rhs) {
	int8_t* lhs_data = (int8_t*)lhs->data;

	int i;
	for (i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs;
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
	if (vector->data != 0) {
		// TODO: Error
	} else {
		vector->size = size;
		vector->data = xmalloc(sizeof(int8_t) * size);
	}
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

	int i;
	for (i = 0; i < lhs->size && i < rhs->size; i++)
		lhs_data[i] = rhs_data[i];
	
	while (i < lhs->size) {
		lhs_data[i] = 0;
		i++;
	}
}

void char_assignVectorScalar(struct Vector* lhs, int8_t rhs) {
	int8_t* lhs_data = (int8_t*)lhs->data;

	int i;
	for (i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs;
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
	if (vector->data != 0) {
		// TODO: Error
	} else {
		vector->size = size;
		vector->data = xmalloc(sizeof(int32_t) * size);
	}
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

	int i;
	for (i = 0; i < lhs->size && i < rhs->size; i++)
		lhs_data[i] = rhs_data[i];
	
	while (i < lhs->size) {
		lhs_data[i] = 0;
		i++;
	}
}

void int_assignVectorScalar(struct Vector* lhs, int32_t rhs) {
	int32_t* lhs_data = (int32_t*)lhs->data;

	int i;
	for (i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs;
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

void int_VectorAddScalar(struct Vector* out, struct Vector* lhs, int32_t rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] + rhs;
}

void int_VectorSubtractScalar(struct Vector* out, struct Vector* lhs, int32_t rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] - rhs;
}

void int_ScalarSubtractVector(struct Vector* out, int32_t lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *rhs_data = (int32_t*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs - rhs_data[i];
}

void int_VectorMultiplyScalar(struct Vector* out, struct Vector* lhs, int32_t rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i]*rhs;
}

void int_VectorDivideScalar(struct Vector* out, struct Vector* lhs, int32_t rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i]/rhs;
}

void int_ScalarDivideVector(struct Vector* out, int32_t lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *rhs_data = (int32_t*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs/rhs_data[i];
}

void int_VectorModulusScalar(struct Vector* out, struct Vector* lhs, int32_t rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] % rhs;
}

void int_ScalarModulusVector(struct Vector* out, int32_t lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *rhs_data = (int32_t*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs % rhs_data[i];
}

void int_VectorPowerScalar(struct Vector* out, struct Vector* lhs, int32_t rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *lhs_data = (int32_t*) lhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] ^ rhs;
}

void int_ScalarPowerVector(struct Vector* out, int32_t lhs, struct Vector* rhs) {
	int32_t *out_data = (int32_t*) out->data;	
	int32_t *rhs_data = (int32_t*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs ^ rhs_data[i];
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
// 	REALS  		//
//////////////////////////

void real_allocVector(struct Vector* vector, int32_t size) {
	if (vector->data != 0) {
		// TODO: Error
	} else {
		vector->size = size;
		vector->data = xmalloc(sizeof(float) * size);
	}
}

void real_copyVector(struct Vector* to, struct Vector* from) {
	copyVector(to, from, sizeof(float));
}

int real_getElement(float* out, struct Vector* vector, int32_t index) {
	if (index > vector->size || index < 1)
		return 1;

	*out = ((float*)vector->data)[index - 1];
	
	return 0;
}

int real_setElement(struct Vector* vector, int32_t index, float value) {
	if (index > vector->size || index < 1)
		return 1;

	((float*)vector->data)[index - 1] = value;
	
	return 0;
}

void real_assignVector(struct Vector* lhs, struct Vector* rhs) {
	float* lhs_data = (float*)lhs->data;
	float* rhs_data = (float*)rhs->data;

	int i;
	for (i = 0; i < lhs->size && i < rhs->size; i++)
		lhs_data[i] = rhs_data[i];
	
	while (i < lhs->size) {
		lhs_data[i] = 0;
		i++;
	}
}

void real_assignVectorScalar(struct Vector* lhs, float rhs) {
	float* lhs_data = (float*)lhs->data;

	int i;
	for (i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs;
}

void real_VectorAddVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] + rhs_data[i];
}

void real_VectorSubtractVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] - rhs_data[i];
}

void real_VectorMultiplyVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] * rhs_data[i];
}

void real_VectorDivideVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] / rhs_data[i];
}

void real_VectorModulusVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = fmodf(lhs_data[i], rhs_data[i]);
}

void real_VectorPowerVector(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = powf(lhs_data[i], rhs_data[i]);
}

void real_VectorUniaryMinus(struct Vector* out, struct Vector* lhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = -lhs_data[i];
}

void real_VectorDot(float *result, struct Vector* lhs, struct Vector* rhs) {
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;

	*result = 0;
	for (int i = 0; i < lhs->size; i++)
		*result += lhs_data[i]*rhs_data[i];
}

void real_VectorAddScalar(struct Vector* out, struct Vector* lhs, float rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] + rhs;
}

void real_VectorSubtractScalar(struct Vector* out, struct Vector* lhs, float rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] - rhs;
}

void real_ScalarSubtractVector(struct Vector* out, float lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *rhs_data = (float*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs - rhs_data[i];
}

void real_VectorMultiplyScalar(struct Vector* out, struct Vector* lhs, float rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i]*rhs;
}

void real_VectorDivideScalar(struct Vector* out, struct Vector* lhs, float rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i]/rhs;
}

void real_ScalarDivideVector(struct Vector* out, float lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *rhs_data = (float*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs/rhs_data[i];
}

void real_VectorModulusScalar(struct Vector* out, struct Vector* lhs, float rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = fmodf(lhs_data[i], rhs);
}

void real_ScalarModulusVector(struct Vector* out, float lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *rhs_data = (float*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = fmodf(lhs, rhs_data[i]);
}

void real_VectorPowerScalar(struct Vector* out, struct Vector* lhs, float rhs) {
	float *out_data = (float*) out->data;	
	float *lhs_data = (float*) lhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = powf(lhs_data[i], rhs);
}

void real_ScalarPowerVector(struct Vector* out, float lhs, struct Vector* rhs) {
	float *out_data = (float*) out->data;	
	float *rhs_data = (float*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = powf(lhs, rhs_data[i]);
}

int real_VectorEq(struct Vector* lhs, struct Vector* rhs) {
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 1;

	return 0;
}

int real_VectorNe(struct Vector* lhs, struct Vector* rhs) {
	float *lhs_data = (float*) lhs->data;
	float *rhs_data = (float*) rhs->data;

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

	printf("RuntimeError: Vectors are not of same lenght.");
	return 0;
}

void releaseVector(struct Vector* vector) {
	xfree(vector->data);
	xfree(vector);
}

