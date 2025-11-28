#ifndef LINKEDLIST_HPP
#define LINKEDLIST_HPP

#include "node.hpp"
#include <iostream>
#include <stdexcept>
using namespace std;

class LinkedList {
private:
    node* head;
    node* tail;
    int size;

public:
    LinkedList();          // Constructor
    ~LinkedList();         // Destructor

    void addFirst(int elem);
    void addLast(int elem);
    void insertAt(int pos, int elem);

    int removeFirst();
    int removeLast();
    int removeAt(int pos);
    void remove(int elem);

    int get(int pos);
    int search(int elem);
    int getSize();

    void print();
    void reverse();
};

#endif
