#include "LinkedList.hpp"
#include <iostream>
using namespace std;

int main() {
    LinkedList list;

    list.addLast(10);
    list.addLast(20);
    list.addLast(30);
    cout << "Initial list: ";
    list.print(); // 10 -> 20 -> 30

    list.addFirst(5);
    cout << "After addFirst(5): ";
    list.print(); // 5 -> 10 -> 20 -> 30

    list.insertAt(3, 15);
    cout << "After insertAt(3,15): ";
    list.print(); // 5 -> 10 -> 15 -> 20 -> 30

    cout << "Removed first: " << list.removeFirst() << endl;
    cout << "After removing first: ";
    list.print(); // 10 -> 15 -> 20 -> 30

    cout << "Removed last: " << list.removeLast() << endl;
    cout << "After removing last: ";
    list.print(); // 10 -> 15 -> 20

    cout << "Searching for 15: found at position " << list.search(15) << endl;

    list.reverse();
    cout << "After reverse: ";
    list.print(); // 20 -> 15 -> 10

    cout << "Current size: " << list.getSize() << endl;

    return 0;
}
