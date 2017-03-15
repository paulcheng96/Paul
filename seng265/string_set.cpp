#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "string_set.h"

using namespace std;

string_set::string_set() {
	for (int i = 0; i < HASH_TABLE_SIZE; i++){
            hash_table[i] = NULL;
        }
}

void string_set::add(const char *s) {
	// allocate new node
	if (contains(s)==1)
		throw duplicate_exception();
	node *p = new node;
	if (p == NULL)
		throw memory_exception();
	
	// allocate string and copy s
	p->s = new char[strlen(s)+1];
	strcpy(p->s, s);
	// link in new node
	iterator_index = hash_function(s);	
	p->next = hash_table[iterator_index];
	hash_table[iterator_index] = p;
	
	reset();
}


void string_set::remove(const char *s) {
	iterator_index = hash_function(s);
	node *curr;
	node *prev;
	curr =hash_table[iterator_index];
	if(contains(s)==0)
		throw not_found_exception(); 
	while (curr->next!=NULL && strcmp(curr->s,s)!=0)  
    {  
        prev = curr;  
        curr = curr->next;  
    }  
    if (strcmp(curr->s,s)==0)  
    {  
        if (curr == hash_table[iterator_index])  
        {  
            hash_table[iterator_index] = curr->next;  
        }   
        else  
        {  
            prev->next = curr->next;  
        }  
        delete curr;  
    }   
		 
	reset();
}

int string_set::contains(const char *s) {
	iterator_index = hash_function(s);
	node *curr;
	curr = hash_table[iterator_index];
	while(curr!=NULL){
		
		if(strcmp(curr->s,s)==0)
			return 1;
		curr = curr->next;
	}
	return 0;
	
	reset();
}

void string_set::reset() {	
	for(int i = 0; i < HASH_TABLE_SIZE; i++){
		if (hash_table[i] != NULL){
			iterator_node = hash_table[i];
			iterator_index = i;
			break;
		}		
	}
}



const char *string_set::next() {
	node *curr;
	curr = iterator_node;
	
	if (iterator_node ==NULL) {
		return NULL;
	}
	if (iterator_node->next !=NULL) {
		iterator_node = iterator_node->next;
		return curr->s;
	}
	else{
		for(int i = iterator_index+1; i < HASH_TABLE_SIZE; i++){
			while (hash_table[i] != NULL){
				iterator_node = hash_table[i];
				iterator_index = i;
				return curr->s;
			}
			iterator_node = NULL;
			return curr->s;
						
		}		
	}	
	return NULL;
				
}

string_set::~string_set() {
	for (int i = 0; i < HASH_TABLE_SIZE; i++){
			while (hash_table[i] != NULL) {
			// save pointer to node D: to be freed
			node* p = hash_table[i];
			// move on to next node
			hash_table[i] = hash_table[i]->next;
			// free node D 
			delete p;
			}
}
}

int string_set::hash_function(const char *s) {
	iterator_index=0;
    while(*s) {
       iterator_index = iterator_index + (int) (*s); // convert char to int, and sum
       s++;
	}
	return iterator_index%HASH_TABLE_SIZE;
}

