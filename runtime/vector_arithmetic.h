#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

#define MAKE_NAME(name, type) type ## _ ## name
#define NAME(name, type) MAKE_NAME(name, type)

extern TEMPLATE_TYPE NAME(mod, TEMPLATE_NAME)(TEMPLATE_TYPE, TEMPLATE_TYPE);
extern TEMPLATE_TYPE NAME(power, TEMPLATE_NAME)(TEMPLATE_TYPE, TEMPLATE_TYPE);

void NAME(VectorAddVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] + rhs_data[i];
}

void NAME(VectorSubtractVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] - rhs_data[i];
}

void NAME(VectorMultiplyVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] * rhs_data[i];
}

void NAME(VectorDivideVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] / rhs_data[i];
}

void NAME(VectorModulusVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = NAME(mod, TEMPLATE_NAME)(lhs_data[i], rhs_data[i]);
}

void NAME(VectorPowerVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = NAME(power, TEMPLATE_NAME)(lhs_data[i], rhs_data[i]);
}

void NAME(VectorUniaryMinus, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = -lhs_data[i];
}

void NAME(VectorDot, TEMPLATE_NAME)(TEMPLATE_TYPE *result, struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	*result = 0;
	for (int i = 0; i < lhs->size; i++)
		*result += lhs_data[i]*rhs_data[i];
}

void NAME(VectorAddScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] + rhs;
}

void NAME(VectorSubtractScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] - rhs;
}

void NAME(ScalarSubtractVector, TEMPLATE_NAME)(struct Vector* out, TEMPLATE_TYPE lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs - rhs_data[i];
}

void NAME(VectorMultiplyScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i]*rhs;
}

void NAME(VectorDivideScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i]/rhs;
}

void NAME(ScalarDivideVector, TEMPLATE_NAME)(struct Vector* out, TEMPLATE_TYPE lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = lhs/rhs_data[i];
}

void NAME(VectorModulusScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = NAME(mod, TEMPLATE_NAME)(lhs_data[i], rhs);
}

void NAME(ScalarModulusVector, TEMPLATE_NAME)(struct Vector* out, TEMPLATE_TYPE lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = NAME(mod, TEMPLATE_NAME)(lhs, rhs_data[i]);
}

void NAME(VectorPowerScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	
	for (int i = 0; i < lhs->size; i++)
		out_data[i] = NAME(power, TEMPLATE_NAME)(lhs_data[i], rhs);
}

void NAME(ScalarPowerVector, TEMPLATE_NAME)(struct Vector* out, TEMPLATE_TYPE lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;	
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < rhs->size; i++)
		out_data[i] = NAME(power, TEMPLATE_NAME)(lhs, rhs_data[i]);
}

