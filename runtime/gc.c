#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#include "types.h"

enum Markers {
	VARIABLE_MEMORY_NODE,
	BLOCK_MEMORY_NODE,
	METHOD_MEMORY_NODE,
	LOOP_MEMORY_NODE
	};

/* header for each object */
typedef struct node* Node;
struct node {
	Node next;
	char mark;
	int type;
	int32_t obj_type;
	void *obj;
};

Node root = 0;

extern void int_releaseInterval(struct Interval* interval);
extern void releaseVector(struct Vector* vector);

void* xmalloc(size_t n) {
	void* result = malloc(n);
	if (!result) {
		fputs("gc: out of memory.\n",stderr);
		abort(); /* all hope is lost */
	}
	memset(result, 0, n);
	return result;
}

Node new_node() {
	Node n = (Node)xmalloc(sizeof(struct node));
	n->next = 0;
	n->mark = 0;
	n->type = 0;
	n->obj_type = 0;
	n->obj = 0;
	return n;
}

void gc_add_object(void* object, int32_t type) {
	Node n = new_node();
	
	n->type = VARIABLE_MEMORY_NODE;
	n->obj_type = type;
	n->obj = object;

	n->next = root;
	root = n;
}

void gc_add_method() {
	Node n = new_node();
	
	n->type = METHOD_MEMORY_NODE;

	n->next = root;
	root = n;
}

void gc_release_object(void* object, int32_t type) {
	switch (type) {
	case INTERVAL:
		int_releaseInterval(object);
		break;
	case VECTOR:
		releaseVector(object);
		break;
	case MATRIX:
		break;
	}
}

void gc_free_method() {
	Node n = root;
	int methodFound = 0;
	while (n != 0 && !methodFound) {
		switch (n->type) {
			case VARIABLE_MEMORY_NODE:
				gc_release_object(n->obj, n->obj_type);
				break;
			case METHOD_MEMORY_NODE:
				methodFound = 1;
			break;
		};
		n = n->next;
	}

	root = n;
}

void gc_add_block() {
	Node n = new_node();
	
	n->type = BLOCK_MEMORY_NODE;

	n->next = root;
	root = n;
}

void gc_add_loop() {
	Node n = new_node();
	
	n->type = LOOP_MEMORY_NODE;

	n->next = root;
	root = n;
}

void gc_free_loop() {
	Node n = root;
	int loopFound = 0;
	while (n != 0 && !loopFound) {
		switch (n->type) {
			case VARIABLE_MEMORY_NODE:
				gc_release_object(n->obj, n->obj_type);
				break;
			case LOOP_MEMORY_NODE:
				loopFound = 1;
			break;
		};
		n = n->next;
	}

	root = n;
}

void gc_print() {
	Node n = root;
	printf("\nCurrent GC Stack:\n\n");
	while (n != 0) {
		switch (n->type) {
		case VARIABLE_MEMORY_NODE:
			printf("VARIABLE:\t");

			switch (n->obj_type) {
			case INTERVAL:
				printf("interval\n");
				break;
			case VECTOR:
				printf("vector\n");
				break;
			case MATRIX:
				printf("matrix\n");
				break;
			}

			break;
		case BLOCK_MEMORY_NODE:
			printf("BLOCK\n");
			break;
		case METHOD_MEMORY_NODE:
			printf("METHOD\n");
			break;
		case LOOP_MEMORY_NODE:
			printf("LOOP\n");
			break;
		}
		
		n = n->next;
	}
}

