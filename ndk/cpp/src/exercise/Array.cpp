#include "Array.h"
#include <iostream>

using namespace std;

// TODO: replace malloc and free with keywords new and delete

Array::Array(int size) : m_size(size)
{
    m_space = (int *)malloc(size * sizeof(int));
    cout << "Constructor(int): size:" << size << " :" << this << endl;
}

Array::Array(const Array &arr)
{
    m_size = arr.m_size;
    m_space = (int *)malloc(m_size * sizeof(int));
    for (int i = 0; i < m_size; i++)
    {
        m_space[i] = arr.m_space[i];
    }
    cout << "Copy Constructor(const Array&): size:" << m_size << " :" << this << endl;
}

Array::~Array()
{
    cout << "Destructor(): size:" << m_size << " :" << this << endl;
    m_size = 0;
    if (m_space)
    {
        free(m_space);
        m_space = NULL;
    }
}

Array &Array::operator=(Array &arr)
{
    if (m_space)
    {
        free(m_space);
    }
    m_size = arr.m_size;
    if (m_size > 0)
    {
        m_space = (int *)malloc(m_size * sizeof(int));
        for (int i = 0; i < m_size; i++)
        {
            m_space[i] = arr.m_space[i];
        }
    }

    return *this;
}

ostream &operator<<(ostream &out, Array &arr)
{
    cout << "Array: " << &arr << " [ ";
    if (arr.size() > 0)
    {
        for (int i = 0; i < arr.size(); i++)
        {
            cout << arr.getData(i);
            if (i != arr.size() - 1)
            {
                cout << ", ";
            }
        }
    }
    cout << " ] size:" << arr.size() << endl;
    return out;
}

int &Array::operator[](int i)
{
    if (i < 0 || i >= m_size)
    {
        cout << "Invalid idx:" << i << endl;
        i = 0;
    }

    return m_space[i];
}

bool Array::operator==(Array &other)
{
    if (other.size() != size())
    {
        return false;
    }
    for (int i = 0; i < m_size; i++)
    {
        if (m_space[i] != other.m_space[i])
        {
            return false;
        }
    }
    return true;
}

bool Array::operator!=(Array &other)
{
    return !(*this==other);
}

void Array::setData(int i, int value)
{
    if (i < 0 || i >= m_size)
    {
        cout << "Invalid idx:" << i << endl;
        return;
    }
    m_space[i] = value;
}

int Array::getData(int i)
{
    if (i < 0 || i >= m_size)
    {
        cout << "Invalid idx:" << i << endl;
        return -1;
    }
    return m_space[i];
}

int Array::size()
{
    return m_size;
}

int main(int argc, char const *argv[])
{
    Array arr1(3);
    arr1.setData(0, 1);
    arr1.setData(1, 2);
    arr1.setData(2, 3);
    cout << arr1;
    cout << arr1.getData(0) << endl;
    cout << arr1.getData(1) << endl;
    cout << arr1.getData(2) << endl;
    arr1.setData(-1, -1);
    cout << arr1.getData(-1) << endl;

    Array arr2 = arr1;
    cout << arr2;
    cout << "arr1 == arr2 : " << (arr1 == arr2) << endl;
    cout << "arr1 != arr2 : " << (arr1 != arr2) << endl;

    arr1.setData(0, 8);
    cout << arr1;
    arr2 = arr1;
    cout << arr2;

    arr1[0] = 7;
    cout << arr1;
    cout << arr1[0] << endl;
    cout << "arr1 == arr2 : " << (arr1 == arr2) << endl;
    cout << "arr1 != arr2 : " << (arr1 != arr2) << endl;
    return 0;
}
