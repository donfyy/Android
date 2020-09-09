#ifndef ARRAY_H
#define ARRAY_H

#include <iostream>

using namespace std;

class Array
{
public:
    Array(int size);
    Array(const Array &array);
    ~Array();
    Array &operator=(Array &arr);
    friend ostream &operator<<(ostream &, Array &);
    int& operator[](int);
    bool operator==(Array&);
    bool operator!=(Array&);

    void setData(int index, int value);
    int getData(int index);
    int size();

private:
    int m_size;
    int *m_space;
};

//int[]
//int[i]
//int[i] =
//==

//作业
//重载 [] = == !=

#endif
