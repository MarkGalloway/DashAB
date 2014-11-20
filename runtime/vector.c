#include <stdlib.h>
#include <stdint.h>

struct Vector {
	int32_t size;
	void* 	data;
};

enum type { BOOLEAN, CHARACTER, INTEGER, REAL};

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

//////////////////////////
// 	BOOLEAN  	//
//////////////////////////

void bool_allocVector(struct Vector* vector, int32_t size) {
	vector->size = size;
	vector->data = malloc(sizeof(int8_t) * size);
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

void bool_VectorOrExpr(struct Vector* out, struct Vector* lhs, int8_t rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] || rhs) & 1;
}

void bool_VectorXOrExpr(struct Vector* out, struct Vector* lhs, int8_t rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] ^ rhs) & 1; 
}

void bool_VectorAndExpr(struct Vector* out, struct Vector* lhs, int8_t rhs) {
	int8_t *out_data = (int8_t*) out->data;
	int8_t *lhs_data = (int8_t*) lhs->data;
	
	for (int i = 0; i < out->size; i++)
		out_data[i] = (lhs_data[i] && rhs) & 1;
}

int bool_VectorEq(struct Vector* lhs, struct Vector* rhs) {
	int8_t *lhs_data = (int8_t*) lhs->data;
	int8_t *rhs_data = (int8_t*) rhs->data;
	
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

int32_t getVectorSize(struct Vector* vector) {
	return vector->size;
}

void releaseVector(struct Vector* vector) {
	free(vector->data);
}
