#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

#define MAKE_NAME(name, type) type ## _ ## name
#define NAME(name, type) MAKE_NAME(name, type)

struct Vector** vectors_matrix_literal;
int literal_index;

// ALLOC
void NAME(allocMatrix, TEMPLATE_NAME)(struct Matrix* matrix, int32_t rows, int32_t columns) {
	if (matrix->rows != 0 && matrix->columns != 0) {
		// TODO: Error
	} else {
		matrix->rows = rows;
		matrix->columns = columns;
		
		int size = rows*columns;
		matrix->data = xmalloc(sizeof(TEMPLATE_TYPE) * size);
	}
}

void NAME(startLiteralMatrixCreation, TEMPLATE_NAME)(int32_t rows) {
	literal_index = 0;
	vectors_matrix_literal = (struct Vector**) xmalloc(sizeof(struct Vector*) * rows);
}

void NAME(addLiteralMatrixCreation, TEMPLATE_NAME)(struct Vector* vector) {
	vectors_matrix_literal[literal_index] = vector;
	literal_index++;
}

void NAME(endLiteralMatrixCreation, TEMPLATE_NAME)(struct Matrix* matrix) {
	int rows = literal_index;	
	int columns = 0;

	for (int i = 0; i < rows; i++) {
		if (vectors_matrix_literal[i]->size > columns)
			columns = vectors_matrix_literal[i]->size;
	}

	matrix->rows = rows;
	matrix->columns = columns;
	
	int size = rows*columns;
	matrix->data = xmalloc(sizeof(TEMPLATE_TYPE) * size);
	TEMPLATE_TYPE* matrix_data = (TEMPLATE_TYPE*) matrix->data;
	
	int i;
	for (i = 0; i < literal_index; i++) {
		TEMPLATE_TYPE* vector_data = (TEMPLATE_TYPE*) vectors_matrix_literal[i]->data;
		int j;		
		for (j = 0; j < vectors_matrix_literal[i]->size; j++) {
			matrix_data[i*columns + j] = vector_data[j];
		}
		
		for (; j < columns; j++) {
			matrix_data[i*columns + j] = 0;
		}
	}	
}

// ASSIGNMENT
void NAME(assignMatrix, TEMPLATE_NAME)(struct Matrix* lhs, struct Matrix* rhs) {
	TEMPLATE_TYPE* lhs_data = (TEMPLATE_TYPE*)lhs->data;
	TEMPLATE_TYPE* rhs_data = (TEMPLATE_TYPE*)rhs->data;
	
	int lhs_size = lhs->rows*lhs->columns;
	int rhs_size = rhs->rows*rhs->columns;
	
	int i;
	for (i = 0; i < lhs_size && i < rhs_size; i++)
		lhs_data[i] = rhs_data[i];

	for (; i < lhs_size; i++)
		lhs_data[i] = 0;
}

int NAME(getElementMatrix, TEMPLATE_NAME)(TEMPLATE_TYPE* out, struct Matrix* matrix, int32_t r, int32_t c) {
	if (r > matrix->rows || r < 1)
		return 0;

	if (c > matrix->columns || c < 1)
		return 0;

	int index = r*matrix->columns + c;

	*out = ((TEMPLATE_TYPE*)matrix->data)[index - 1];
	
	return 1;
}

int NAME(setElementMatrix, TEMPLATE_NAME)( struct Matrix* matrix, int32_t r, int32_t c, TEMPLATE_TYPE value) {
	if (r > matrix->rows || r < 1)
		return 0;

	if (c > matrix->columns || c < 1)
		return 0;

	int index = r*matrix->columns + c;

	((TEMPLATE_TYPE*)matrix->data)[index - 1] = value;
	
	return 1;
}
