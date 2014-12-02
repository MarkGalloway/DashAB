#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

#define MAKE_NAME(name, type) type ## _ ## name
#define NAME(name, type) MAKE_NAME(name, type)

// ALLOC
void NAME(copyVector, TEMPLATE_NAME)(struct Vector* to, struct Vector* from) {
	copyVector(to, from, sizeof(TEMPLATE_TYPE));
}

void NAME(allocVector, TEMPLATE_NAME)(struct Vector* vector, int32_t size) {
	if (vector->size != 0) {
		// TODO: Error
	} else {
		vector->size = size;
		vector->data = xmalloc(sizeof(TEMPLATE_TYPE) * size);
	}
}

// ASSIGNMENT
int NAME(getElement, TEMPLATE_NAME)(TEMPLATE_TYPE* out, struct Vector* vector, int32_t index) {
	if (index > vector->size || index < 1)
		return 0;

	*out = ((TEMPLATE_TYPE*)vector->data)[index - 1];
	
	return 1;
}

int NAME(setElement, TEMPLATE_NAME)(struct Vector* vector, int32_t index, TEMPLATE_TYPE value) {
	if (index > vector->size || index < 1)
		return 0;

	((TEMPLATE_TYPE*)vector->data)[index - 1] = value;
	
	return 1;
}

int NAME(indexVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* vector, struct Vector* index) {
	NAME(allocVector, TEMPLATE_NAME)(out, index->size);

	TEMPLATE_TYPE* out_data = (TEMPLATE_TYPE*)out->data;
	TEMPLATE_TYPE* vector_data = (TEMPLATE_TYPE*)vector->data;
	int32_t* index_data = (int32_t*)index->data;
		
	int i;
	for (i = 0; i < index->size; i++) {
		int32_t index = index_data [i];

		if (index < 1 || index > vector->size) {
			return 0;
		}

		out_data[i] = vector_data [index - 1];
	}

	return 1;
}

void NAME(assignVector, TEMPLATE_NAME)(struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE* lhs_data = (TEMPLATE_TYPE*)lhs->data;
	TEMPLATE_TYPE* rhs_data = (TEMPLATE_TYPE*)rhs->data;
	
	int i;
	for (i = 0; i < lhs->size && i < rhs->size; i++)
		lhs_data[i] = rhs_data[i];
	
	while (i < lhs->size) {
		lhs_data[i] = 0;
		i++;
	}
}

int NAME(VectorIndexAssignVector, TEMPLATE_NAME)(struct Vector* vector, struct Vector* index, struct Vector* value) {
	TEMPLATE_TYPE* vector_data = (TEMPLATE_TYPE*)vector->data;
	int32_t* index_data = (int32_t*)index->data;
	TEMPLATE_TYPE* value_data = (TEMPLATE_TYPE*)value->data;

	if (index->size != value->size) {
		return 0;
	}
		
	int i;
	for (i = 0; i < index->size; i++) {
		int32_t index = index_data [i];

		if (index < 1 || index > vector->size) {
			return 0;
		}

		vector_data [index - 1] = value_data[i];
	}

	return 1;
}

int NAME(VectorIndexAssignScalar, TEMPLATE_NAME)(struct Vector* vector, struct Vector* index, TEMPLATE_TYPE value) {
	TEMPLATE_TYPE* vector_data = (TEMPLATE_TYPE*)vector->data;
	int32_t* index_data = (int32_t*)index->data;
		
	int i;
	for (i = 0; i < index->size; i++) {
		int32_t index = index_data [i];

		if (index < 1 || index > vector->size) {
			return 0;
		}

		vector_data [index - 1] = value;
	}

	return 1;
}

void NAME(assignVectorScalar, TEMPLATE_NAME)(struct Vector* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE* lhs_data = (TEMPLATE_TYPE*)lhs->data;

	int i;
	for (i = 0; i < lhs->size; i++)
		lhs_data[i] = rhs;
}

// Operations

void NAME(VectorLTVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] < rhs_data[i];
}

void NAME(VectorLEVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] <= rhs_data[i];
}

void NAME(VectorGTVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] > rhs_data[i];
}

void NAME(VectorGEVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int8_t *out_data = (int8_t*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i] >= rhs_data[i];
}


int NAME(VectorEq, TEMPLATE_NAME)(struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	if (lhs->size != rhs->size)
		return 0;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 1;

	return 0;
}

int NAME(VectorNe, TEMPLATE_NAME)(struct Vector* lhs, struct Vector* rhs) {
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	if (lhs->size != rhs->size)
		return 1;
	
	int32_t match = 0;
	for (int i = 0; i < lhs->size; i++)
		match += lhs_data[i] == rhs_data[i];

	if (match == lhs->size)
		return 0;

	return 1;
}

int NAME(VectorBy, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, int32_t by) {
	if (by < 1) {
		return 0;
	}
	
	int size = (int) ceil(((float)(lhs->size))/by);

	NAME(allocVector, TEMPLATE_NAME)(out, size);

	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i*by];

	return 1;
}

void NAME(VectorConcatVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, struct Vector* rhs) {
	int size = lhs->size + rhs->size;

	NAME(allocVector, TEMPLATE_NAME)(out, size);

	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;
	TEMPLATE_TYPE *rhs_data = (TEMPLATE_TYPE*) rhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i];

	for (int i = 0; i < rhs->size; i++)
		out_data[i + lhs->size] = rhs_data[i];
}

void NAME(VectorConcatScalar, TEMPLATE_NAME)(struct Vector* out, struct Vector* lhs, TEMPLATE_TYPE rhs) {
	int size = lhs->size + 1;

	NAME(allocVector, TEMPLATE_NAME)(out, size);

	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;
	TEMPLATE_TYPE *lhs_data = (TEMPLATE_TYPE*) lhs->data;

	for (int i = 0; i < lhs->size; i++)
		out_data[i] = lhs_data[i];

	out_data[lhs->size] = rhs;
}

void NAME(reverseVector, TEMPLATE_NAME)(struct Vector* out, struct Vector* vector) {
	TEMPLATE_TYPE *out_data = (TEMPLATE_TYPE*) out->data;
	TEMPLATE_TYPE *vector_data = (TEMPLATE_TYPE*) vector->data;
	
	for (int i = 0; i < vector->size; i++)
		out_data[vector->size - i - 1] = vector_data[i];
}




