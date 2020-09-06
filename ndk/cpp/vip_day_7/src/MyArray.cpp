//
// Created by Zero on 2019/10/22.
//

#include "MyArray.h"
#include <iostream>



MyArray::MyArray(int size){
    if(size < 0){
        size = 0;
    }
    m_size = size;
    m_space = new int[m_size];

}
MyArray::MyArray(const MyArray& array){
    m_size = array.m_size;
    m_space = new int[m_size];
    for (int i = 0; i < m_size; i++) {
        m_space[i] = array.m_space[i];
    }

}
MyArray::~MyArray(){
    if(m_space != nullptr){
        delete [] m_space;
        m_space = nullptr;
        m_size = -1;

    }
}

void MyArray::setData(int index,int value){
   m_space[index] = value;
}
int MyArray::getData(int index){
    return m_space[index];
}
int MyArray::size(){
    return m_size;
}


int& MyArray::operator[](int i){
    return m_space[i];
}

//重载=
MyArray& MyArray::operator=(MyArray &array){
    //1 释放原来的内存空间
    if (m_space != nullptr)
    {
        delete [] m_space;
        m_size = 0;
    }
    //2 根据a1大小 分配内存
    m_size = array.m_size;
    m_space = new int[m_size];
//    m_space = (int*)malloc(m_size);
    // new/delete new[]/delete[]  malloc/free C++ new/delete new[]/delete[]是关键字
    //malloc/free 是C库的函数
    //C++ class类型

    //3 copy数据
    for (int i=0; i<m_size; i++)
    {
        //m_space[i] = a1.m_space[i];
        m_space[i] = array[i];
    }

    return *this;
}

//重载 ==
bool MyArray::operator==(MyArray &array){
    if (this->m_size !=  array.m_size)
    {
        return false;
    }

    for (int i=0; i<m_size; i++)
    {
        if (this->m_space[i] != array[i])
        {
            return false;
        }
    }
    return true;
}


//重载 !=
bool MyArray::operator!=(MyArray &array){
    return !(*this == array);
}
