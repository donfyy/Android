#include <cstring>
#include "String.h"

ostream &operator<<(ostream &out, MyString &s)
{
    out << s.m_p;
    return out;
}

istream &operator>>(istream &in, MyString &s)
{
    in >> s.m_p;
    return in;
}

MyString::MyString(int len)
{
    if (len < 0)
        len = 0;
    if (len == 0)
    {
        m_len = 0;
        m_p = new char[m_len + 1];
        strcpy(m_p, "");
    }
    else
    {
        m_len = len;
        m_p = new char[m_len + 1];
        memset(m_p, 0, m_len);
    }
}

MyString::MyString(const char *p)
{
    if (p == NULL)
    {
        m_len = 0;
        m_p = new char[m_len + 1];
        strcpy(m_p, "");
    }
    else
    {
        m_len = strlen(p);
        m_p = new char[m_len + 1];
        strcpy(m_p, p);
    }
}

MyString::MyString(const MyString &s)
{
    m_len = s.m_len;
    m_p = new char[m_len + 1];

    strcpy(m_p, s.m_p);
}

MyString::~MyString()
{
    if (m_p != NULL)
    {
        delete[] m_p;
        m_p = NULL;
        m_len = -1;
    }
}

MyString &MyString::operator=(const char *p)
{
    if (m_p != NULL)
    {
        delete[] m_p;
    }

    if (p == NULL)
    {
        m_len = 0;
        m_p = new char[m_len + 1];
        strcpy(m_p, "");
    }
    else
    {
        m_len = strlen(p);
        m_p = new char[m_len + 1];
        strcpy(m_p, p);
    }
    return *this;
}

MyString &MyString::operator=(const MyString &s)
{
    if (m_p != NULL)
    {
        delete[] m_p;
    }
    m_len = s.m_len;
    m_p = new char[m_len + 1];
    strcpy(m_p, s.m_p);
    return *this;
}

char &MyString::operator[](int index)
{
    return m_p[index];
}

bool MyString::operator==(const char *p) const
{
    if (p == NULL)
    {
        return m_len < 1;
    }
    else
    {
        return m_len == strlen(p) && !strcmp(m_p, p);
    }
}

bool MyString::operator!=(const char *p) const
{
    return !(*this == p);
}

bool MyString::operator==(const MyString &s) const
{
    return m_len != s.m_len  || !strcmp(m_p, s.m_p);
}

bool MyString::operator!=(const MyString &s) const
{
    return !(*this == s);
}

int MyString::operator<(const char *p)
{
    return strcmp(this->m_p, p);
}

int MyString::operator>(const char *p)
{
    return strcmp(p, this->m_p);
}

int MyString::operator<(const MyString &s)
{
    return strcmp(this->m_p, s.m_p);
}

int MyString::operator>(const MyString &s)
{
    return strcmp(s.m_p, m_p);
}
char *MyString::c_str() {
    return m_p;
}
int MyString::length() {
    return m_len;
}