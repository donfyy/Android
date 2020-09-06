//
// Created by Zero on 2019/10/22.
//

#ifndef C_PLUS_STUDY_MYARRAY_H
#define C_PLUS_STUDY_MYARRAY_H


class MyArray {
public:
    MyArray(int size);
    MyArray(const MyArray& array);
    ~MyArray();

    void setData(int index,int value);
    int getData(int index);
    int size();

    int& operator[](int i);
    //MyArray a(10);
    //int b = a[1];
    //a[2] = 3; //返回值当左值 要么是指针，要么是引用
// 认为[]是一个函数调用   1. operator[]();
// 分析出参数列表         2. operator[](int i);
// 推断出 返回类型        3. int& operator[](int i);

    //重载=
    MyArray& operator=(MyArray &array);
    //MyArray a(10)，a1;
    // a1 = a;
//    operator=()
//    operator=(const MyArray& array)//MyArray MyArray&
//    MyArray& operator=(const MyArray& array)

    //重载 ==
    bool operator==(MyArray &array);
    //MyArray a(10)，a1;
    // bool b = (a1 == a);
//    operator==()
//    bool operator==(MyArray &array)

    //重载 !=
    bool operator!=(MyArray &array);

private:
    int m_size;
    int * m_space;
};

//int[]
//int[i]
//int[i] =
//==

//作业
//重载 [] = == !=


#endif //C_PLUS_STUDY_MYARRAY_H
