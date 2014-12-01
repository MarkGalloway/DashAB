#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

#define MAKE_NAME(name, type) type ## _ ## name
#define NAME(name, type) MAKE_NAME(name, type)

extern void NAME(allocVector, TEMPLATE_NAME)(struct Vector*, int32_t);

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

// INDEXING
int NAME(getElementMatrix, TEMPLATE_NAME)(TEMPLATE_TYPE* out, struct Matrix* matrix, int32_t r, int32_t c) {
	if (r > matrix->rows || r < 1)
		return 0;

	if (c > matrix->columns || c < 1)
		return 0;

	int index = (r-1)*matrix->columns + (c-1);

	*out = ((TEMPLATE_TYPE*)matrix->data)[index];
	
	return 1;
}

int NAME(rowIndexMatrix, TEMPLATE_NAME)(struct Vector* out, struct Matrix* matrix, int32_t r, struct Vector* column) {
	if (r > matrix->rows || r < 1)
		return 0;

	NAME(allocVector, TEMPLATE_NAME)(out, column->size);
	
	for (int i = 0; i < column->size; i++) {
		int c = ((int32_t*)column->data)[i];

		if (c > matrix->columns || c < 1)
			return 0;
		
		int index = (r-1)*matrix->columns + (c-1);		
		((TEMPLATE_TYPE*)out->data)[i] = ((TEMPLATE_TYPE*)matrix->data)[index];
	}
	
	return 1;
}

int NAME(columnIndexMatrix, TEMPLATE_NAME)(struct Vector* out, struct Matrix* matrix, struct Vector* row, int32_t c) {
	if (c > matrix->columns || c < 1)
		return 0;

	NAME(allocVector, TEMPLATE_NAME)(out, row->size);
	
	for (int i = 0; i < row->size; i++) {
		int r = ((int32_t*)row->data)[i];

		if (r > matrix->rows || r < 1)
			return 0;

		int index = (r-1)*matrix->columns + (c-1);		
		((TEMPLATE_TYPE*)out->data)[i] = ((TEMPLATE_TYPE*)matrix->data)[index];
	}
	
	return 1;
}

int NAME(indexMatrix, TEMPLATE_NAME)(struct Matrix* out, struct Matrix* matrix, struct Vector* row, struct Vector* column) {
	NAME(allocMatrix, TEMPLATE_NAME)(out, row->size, row->size);
	
	for (int i = 0; i < row->size; i++) {
		int r = ((int32_t*)row->data)[i];

		if (r > matrix->rows || r < 1)
			return 0;

		for (int j = 0; j < column->size; j++) {
			int c = ((int32_t*)column->data)[j];

			if (c > matrix->columns || c < 1)
				return 0;

			int index = (r-1)*matrix->columns + (c-1);		
			((TEMPLATE_TYPE*)out->data)[i*row->size + j] = ((TEMPLATE_TYPE*)matrix->data)[index];
		}
	}
	
	return 1;
}

// ASSIGNMENT
void NAME(assignMatrix, TEMPLATE_NAME)(struct Matrix* lhs, struct Matrix* rhs) {
	TEMPLATE_TYPE* lhs_data = (TEMPLATE_TYPE*)lhs->data;
	TEMPLATE_TYPE* rhs_data = (TEMPLATE_TYPE*)rhs->data;
	
	int i;
	for (i = 0; i < lhs->rows && i < rhs->rows; i++) {
		int j;		
		for (j = 0; j < lhs->columns && j < rhs->columns; j++) {
			lhs_data[i*lhs->columns + j] = rhs_data[i*rhs->columns + j];
		}
		
		for (; j < lhs->columns; j++) {
			lhs_data[i*lhs->columns + j] = 0;
		}
	}

	for (; i < lhs->rows; i++) {
		int j;		
		for (j = 0; j < lhs->columns; j++) {
			lhs_data[i*lhs->columns + j] = 0;
		}
	}
}

void NAME(assignMatrixScalar, TEMPLATE_NAME)(struct Matrix* lhs, TEMPLATE_TYPE rhs) {
	TEMPLATE_TYPE* lhs_data = (TEMPLATE_TYPE*)lhs->data;
	int lhs_size = lhs->rows*lhs->columns;
	
	int i;
	for (i = 0; i < lhs_size; i++)
		lhs_data[i] = rhs;
}

int NAME(setElementMatrix, TEMPLATE_NAME)( struct Matrix* matrix, int32_t r, int32_t c, TEMPLATE_TYPE value) {
	if (r > matrix->rows || r < 1)
		return 0;

	if (c > matrix->columns || c < 1)
		return 0;

	int index = (r-1)*matrix->columns + (c-1);

	((TEMPLATE_TYPE*)matrix->data)[index] = value;
	
	return 1;
}

int NAME(rowAssignMatrix, TEMPLATE_NAME)(struct Matrix* matrix, int32_t r, struct Vector* column, struct Vector* value) {
	
	if (column->size != value->size)
		return 0;

	if (r > matrix->rows || r < 1)
		return 0;
	
	for (int i = 0; i < column->size; i++) {
		int c = ((int32_t*)column->data)[i];

		if (c > matrix->columns || c < 1)
			return 0;
		
		int index = (r-1)*matrix->columns + (c-1);		
		((TEMPLATE_TYPE*)matrix->data)[index] = ((TEMPLATE_TYPE*)value->data)[i];
	}
	
	return 1;
}

int NAME(columnAssignMatrix, TEMPLATE_NAME)(struct Matrix* matrix, struct Vector* row, int32_t c, struct Vector* value) {
	
	if (row->size != value->size)
		return 0;

	if (c > matrix->columns || c < 1)
		return 0;
	
	for (int i = 0; i < row->size; i++) {
		int r = ((int32_t*)row->data)[i];

		if (r > matrix->rows || r < 1)
			return 0;
		
		int index = (r-1)*matrix->columns + (c-1);		
		((TEMPLATE_TYPE*)matrix->data)[index] = ((TEMPLATE_TYPE*)value->data)[i];
	}
	
	return 1;
}

int NAME(matrixAssignMatrix, TEMPLATE_NAME)(struct Matrix* matrix, struct Vector* row, struct Vector* column, struct Matrix* value) {
	
	if (row->size != value->rows)
		return 0;

	if (column->size != value->columns)
		return 0;
	
	for (int i = 0; i < row->size; i++) {
		int r = ((int32_t*)row->data)[i];

		if (r > matrix->rows || r < 1)
			return 0;

		for (int j = 0; j < column->size; j++) {
			int c = ((int32_t*)column->data)[j];
			
			if (c > matrix->columns || c < 1)
				return 0;
		
			int index = (r-1)*matrix->columns + (c-1);		
			((TEMPLATE_TYPE*)matrix->data)[index] = ((TEMPLATE_TYPE*)value->data)[i*column->size + j];
		}
	}
	
	return 1;
}

int NAME(scalarAssignMatrix, TEMPLATE_NAME)(struct Matrix* matrix, struct Vector* row, struct Vector* column, TEMPLATE_TYPE value) {
	for (int i = 0; i < row->size; i++) {
		int r = ((int32_t*)row->data)[i];

		if (r > matrix->rows || r < 1)
			return 0;

		for (int j = 0; j < column->size; j++) {
			int c = ((int32_t*)column->data)[j];
			
			if (c > matrix->columns || c < 1)
				return 0;
		
			int index = (r-1)*matrix->columns + (c-1);		
			((TEMPLATE_TYPE*)matrix->data)[index] = value;
		}
	}
	
	return 1;
}

