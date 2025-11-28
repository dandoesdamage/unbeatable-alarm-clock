#include "LinkedList.hpp"

// Constructor
LinkedList::LinkedList() {
    head = nullptr;
    tail = nullptr;
    size = 0;
}

// Destructor
LinkedList::~LinkedList() {
    node* curr = head;
    while (curr) {
        node* temp = curr;
        curr = curr->next;
        delete temp;
    }
}

// Add at beginning
void LinkedList::addFirst(int elem) {
    node* n = new node;
    n->elem = elem;
    n->next = head;
    head = n;
    if (!tail) tail = n;
    size++;
}

// Add at end
void LinkedList::addLast(int elem) {
    node* n = new node;
    n->elem = elem;
    n->next = nullptr;
    if (tail)
        tail->next = n;
    else
        head = n;
    tail = n;
    size++;
}

// Insert at position (1-based)
void LinkedList::insertAt(int pos, int elem) {
    if (pos < 1 || pos > size + 1)
        throw logic_error("Invalid position: " + to_string(pos));

    if (pos == 1) { addFirst(elem); return; }
    if (pos == size + 1) { addLast(elem); return; }

    node* n = new node;
    n->elem = elem;

    node* curr = head;
    for (int i = 1; i < pos - 1; i++)
        curr = curr->next;

    n->next = curr->next;
    curr->next = n;
    size++;
}

// Remove first
int LinkedList::removeFirst() {
    if (!head)
        throw logic_error("List is empty.");

    int val = head->elem;
    node* temp = head;
    head = head->next;
    delete temp;

    if (!head) tail = nullptr;
    size--;
    return val;
}

// Remove last
int LinkedList::removeLast() {
    if (!head)
        throw logic_error("List is empty.");

    int val = tail->elem;

    if (head == tail) {
        delete head;
        head = tail = nullptr;
    } else {
        node* curr = head;
        while (curr->next != tail)
            curr = curr->next;
        delete tail;
        tail = curr;
        tail->next = nullptr;
    }
    size--;
    return val;
}

// Remove at position
int LinkedList::removeAt(int pos) {
    if (pos < 1 || pos > size)
        throw logic_error("Invalid position: " + to_string(pos));

    if (pos == 1) return removeFirst();
    if (pos == size) return removeLast();

    node* curr = head;
    for (int i = 1; i < pos - 1; i++)
        curr = curr->next;

    node* target = curr->next;
    int val = target->elem;
    curr->next = target->next;
    delete target;
    size--;
    return val;
}

// Remove by value
void LinkedList::remove(int elem) {
    if (!head) return;
    if (head->elem == elem) {
        removeFirst();
        return;
    }

    node* curr = head;
    while (curr->next && curr->next->elem != elem)
        curr = curr->next;

    if (curr->next) {
        node* target = curr->next;
        curr->next = target->next;
        if (target == tail) tail = curr;
        delete target;
        size--;
    }
}

// Get value by position
int LinkedList::get(int pos) {
    if (pos < 1 || pos > size)
        throw logic_error("Invalid position: " + to_string(pos));

    node* curr = head;
    for (int i = 1; i < pos; i++)
        curr = curr->next;
    return curr->elem;
}

// Search for a value
int LinkedList::search(int elem) {
    int pos = 1;
    for (node* curr = head; curr; curr = curr->next, pos++) {
        if (curr->elem == elem)
            return pos;
    }
    return -1;
}

// Get current size
int LinkedList::getSize() {
    return size;
}

// Print list
void LinkedList::print() {
    if (!head) {
        cout << "Empty" << endl;
        return;
    }
    for (node* curr = head; curr; curr = curr->next) {
        cout << curr->elem;
        if (curr->next)
            cout << " -> ";
    }
    cout << endl;
}

// Reverse the list
void LinkedList::reverse() {
    node* prev = nullptr;
    node* curr = head;
    tail = head;

    while (curr) {
        node* nextNode = curr->next;
        curr->next = prev;
        prev = curr;
        curr = nextNode;
    }
    head = prev;
}
