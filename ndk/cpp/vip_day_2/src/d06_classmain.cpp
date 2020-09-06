//
// Created by Zero on 2019/10/13.
//

#include <iostream>

#include "../includes/MyTeacher.h"
using namespace std;

// :: 作用域限定运算符

int a;

int main(){
    //TODO: 用类
    cout<<"用类"<<endl;

    MyTeacher t1;
    t1.setAge(33);

    cout <<"t1.age: " <<t1.getAge()<<endl;

    float a;
    a = 3.14;

    ::a = 5;//实现在局部变量a的作用域范围内对全局变量a的访问

    cout<<"local a: " << a << " global a: " << ::a <<endl;

    return 0;
}