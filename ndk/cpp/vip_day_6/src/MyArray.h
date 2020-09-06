//
// Created by Zero on 2019/10/22.
//

#ifndef C_PLUS_STUDY_MYARRAY_H
#define C_PLUS_STUDY_MYARRAY_H


class MyArray {
public:
    MyArray(int size);
    MyArray(const MyArray array);
    ~MyArray();

    void setData(int index,int value);
    int getData(int index);
    int size();

private:
    int size;
    int * m_space;
};

//int[]
//int[i]
//int[i] =
//==

//作业
//重载 [] = == !=


#endif //C_PLUS_STUDY_MYARRAY_H
