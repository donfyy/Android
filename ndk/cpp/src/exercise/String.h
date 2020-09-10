#include <iostream>
using namespace std;

#ifndef MYSTRING_H
#define MYSTRING_H

class MyString
{
    friend ostream &operator<<(ostream &out, MyString &s);
    friend istream &operator>>(istream &in, MyString &s);

public:
    MyString(int len = 0);
    MyString(const char *p);
    MyString(const MyString &s);
    ~MyString();
    MyString &operator=(const char *p);
    MyString &operator=(const MyString &s);
    char &operator[](int index);
    bool operator==(const char *p) const;
    bool operator==(const MyString &s) const;
    bool operator!=(const char *p) const;
    bool operator!=(const MyString &s) const;

public:
    int operator<(const char *p);
    int operator>(const char *p);
    int operator<(const MyString &s);
    int operator>(const MyString &s);

public:
    char *c_str();
    int length();

private:
    int m_len;
    char *m_p;
};

#endif
