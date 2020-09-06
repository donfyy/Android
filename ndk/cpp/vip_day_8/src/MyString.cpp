//
// Created by Zero on 2019/10/27.
//

#include <cstring>
#include "MyString.h"

ostream &operator<<(ostream &out, MyString &s) {
    out << s.m_p;
    return out;
}

istream &operator>>(istream &in, MyString &s) {
    cin >> s.m_p;
    return in;
}

MyString::MyString(int len) {
    if (len == 0) {
        m_len = 0;
        m_p = new char[m_len + 1];

        strcpy(m_p, "");
    } else {
        m_len = len;
        m_p = new char[m_len + 1];
        memset(m_p, 0, m_len);
    }

}

MyString::MyString(const char *p) {
    if (p == NULL) {
        m_len = 0;
        m_p = new char[m_len + 1];
        strcpy(m_p, "");
    } else {
        m_len = strlen(p);
        m_p = new char[m_len + 1];
        strcpy(m_p, p);
    }
}

MyString::MyString(const MyString &s) {
    m_len = s.m_len;
    m_p = new char[m_len + 1];

    strcpy(m_p, s.m_p);
}

MyString::~MyString() {
    if (m_p != NULL) {
        delete[] m_p;
        m_p = NULL;
        m_len = 0;
    }
}

MyString &MyString::operator=(const char *p) {
    //1 旧内存释放掉
    if (m_p != NULL) {
        delete[] m_p;
        m_len = 0;
    }
    //2 根据p分配内存

    if (p == NULL) {
        m_len = 0;
        m_p = new char[m_len + 1];
        strcpy(m_p, "");
    } else {
        m_len = strlen(p);
        m_p = new char[m_len + 1];
        strcpy(m_p, p);
    }
    return *this;
}

MyString &MyString::operator=(const MyString &s) {
    //1 旧内存释放掉
    if (m_p != NULL) {
        delete[] m_p;
        m_len = 0;
    }
    //2 根据s分配内存
    m_len = s.m_len;
    m_p = new char[m_len + 1];
    strcpy(m_p, s.m_p);

    return *this;
}

char &MyString::operator[](int index) {
    return m_p[index];
}

bool MyString::operator==(const char *p) const {
    if (p == NULL) {
        if (m_len == 0) {
            return true;
        } else {
            return false;
        }
    } else {
        if (m_len == strlen(p)) {
            return !strcmp(m_p, p);
        } else {
            return false;
        }
    }
}

bool MyString::operator!=(const char *p) const {
    return !(*this == p);
}


bool MyString::operator==(const MyString &s) const {
    if (m_len != s.m_len) {
        return false;
    }
    return !strcmp(m_p, s.m_p);
}

bool MyString::operator!=(const MyString &s) const {
    return !(*this == s);
}


int MyString::operator<(const char *p) {
    return strcmp(this->m_p, p);
}

int MyString::operator>(const char *p) {
    return strcmp(p, this->m_p);
}

int MyString::operator<(const MyString &s) {
    return strcmp(this->m_p, s.m_p);
}

int MyString::operator>(const MyString &s) {
    return strcmp(s.m_p, m_p);
}