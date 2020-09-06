//
// Created by Zero on 2019/10/10.
//

#include <iostream>

using namespace std;//使用命名空间std标准的命名空间


//引用的难点

int getA1(){
    int a;
    a = 10;
    return a;
}

int& getA2(){
    int a;
    a = 20;
    return a;
}

int* getA3(){
    int a;
    a = 30;
    return &a;
}



int main01(){


    int a1 = getA1();
    cout << "a1: " << a1<<endl;
    //当我们把栈上的引用 指针 返回出来的时候 是有问题的，因为栈的变量 方法调用完之后会被系统自动回收
//
//    int a2 = getA2();
//    cout<< "a2: " << a2 <<endl;

    int* a3 = getA3();
    cout<< "a3: " << a3 <<endl;

    return 0;
}

int getG1(){//返回的是数值
    static int a = 10;
    a++;
    return a;
}

int& getG2(){//返回的是变量本身
    static int b = 20;
    b++;
    cout <<"b: " << b <<endl;
    printf("&b:%p\n",&b);
    return b;
}

// 指针 引用  const
int main02(){
    //TODO: static
    cout<<"static"<<endl;

    int g1 = getG1();
    cout<< "g1: " << g1 <<endl;
    //函数返回值当左值
//    getG1() = 100;

   int g2 = getG2();//当右值
    printf("&g2:%p\n",&g2);
   cout<< "g2: " << g2 << endl;
   getG2() = 200;
   getG2();

    return 0;
}