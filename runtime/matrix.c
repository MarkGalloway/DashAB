#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <math.h>

#include "types.h"

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
#include "matrix.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void bool_printMatrix(struct Matrix* matrix) {
	int8_t *matrix_data = (int8_t*) matrix->data;

	for (int i = 0; i < matrix->rows; i++) {
		for (int j = 0; j < matrix->columns; j++) {
			printf("%c", matrix_data[i*matrix->columns + j]);
		}
	}
}


//////////////////////////
// 	CHARACTER  	//
//////////////////////////


#define TEMPLATE_NAME char
#define TEMPLATE_TYPE int8_t
#include "matrix.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME


void char_printMatrix(struct Matrix* matrix) {
	int8_t *matrix_data = (int8_t*) matrix->data;

	for (int i = 0; i < matrix->rows; i++) {
		for (int j = 0; j < matrix->columns; j++) {
			if (matrix_data[i*matrix->columns + j] == 0)
				printf("F");
			else
				printf("T");
		}
	}
}

//////////////////////////
// 	INTEGER  	//
//////////////////////////


#define TEMPLATE_NAME int
#define TEMPLATE_TYPE int32_t
#include "matrix.h"
#include "matrix_arithmetic.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void int_MatrixToReal(struct Matrix* out, struct Matrix* matrix) {
	float *out_data = (float*) out->data;
	int32_t *matrix_data = (int32_t*) matrix->data;

	for (int i = 0; i < matrix->rows*matrix->columns; i++)
		out_data[i] = (float)matrix_data[i];
}

void int_printMatrix(struct Matrix* matrix) {
	int32_t *matrix_data = (int32_t*) matrix->data;

	for (int i = 0; i < matrix->rows; i++) {
		for (int j = 0; j < matrix->columns; j++) {
			printf("%d", matrix_data[i*matrix->columns + j]);
		}
	}
}

//////////////////////////
// 	REAL	  	//
//////////////////////////


#define TEMPLATE_NAME real
#define TEMPLATE_TYPE float
#include "matrix.h"
#include "matrix_arithmetic.h"
#undef TEMPLATE_TYPE
#undef TEMPLATE_NAME

void real_printMatrix(struct Matrix* matrix) {
	float *matrix_data = (float*) matrix->data;

	for (int i = 0; i < matrix->rows; i++) {
		for (int j = 0; j < matrix->columns; j++) {
			printf("%g", matrix_data[i*matrix->columns + j]);
		}
	}
}

int32_t getMatrixRows(struct Matrix* matrix) {
	return matrix->rows;
}

int32_t getMatrixColumns(struct Matrix* matrix) {
	return matrix->columns;
}

struct Matrix* allocMatrix() {
	struct Matrix* matrix = (struct Matrix*) xmalloc(sizeof(struct Matrix));
	matrix->rows = 0;
	matrix->columns = 0;
	matrix->data = 0;

	gc_add_object(matrix, MATRIX);
	
	return matrix;
}

void printMatrixIndexingOutOfBounds() {
	printf("RuntimeError: Matrix indexing out of bounds.");
}


