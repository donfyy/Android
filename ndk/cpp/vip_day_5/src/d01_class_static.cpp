//
// Created by Zero on 2019/10/10.
//

#include <iostream>
#include <string.h>

using namespace std;//使用命名空间std标准的命名空间


//static
class Test{
public:
    void printC(){//成员函数 是不是可以使用静态变量 或者静态函数
        cout <<"c: " <<c<<endl;
        getC();
    }

    void addC(){
        c = c + 1;
    }

    static int getC(){//静态成员函数
        //静态成员函数 不能调用普通成员函数 或者普通成员变量
        cout << "c: " <<c<<endl;
//        cout << "a: " << a<<endl;
        return c;
    }


private:
    int a;
    int b;//成员变量是 是每个对象 都有一份
    static int c;//静态成员变量 类的多个对象共享
};


//1.
int Test::c = 1;


int main01(){
    //TODO: static
    cout<<"static"<<endl;
   //静态成员变量然后初始化
   Test t1;

   //静态成员函数调用
   //1.
   t1.getC();
//  Test::c = 10;
//    int Test::c = 1;//这里不能这么写
   //2
   Test::getC();

    return 0;
}



class Test1{

public:

    friend class Test2;//友元类 Test2是Test1的好朋友，
    //在Test2中可以访问Test1类的私有的成员变量，私有函数(private/protected)

    Test1(){

    }
    Test1(int a,int b){
        this->a = a;
        this->b = b;
    }


    void add(){
        a = a +b;
        cout << "a: " << a <<endl;
    }

    int c;

private:
    friend void add1(Test1 t);//友元函数的声明方式,友元函数
    friend void print1();
    int a;
    int b;
    static int d;
    void print(){
        cout <<"a: " << a<<" ,b: " <<b <<" ,c: "<<c<<endl;
    }
};

class Test2{

public:
    void setA(int a){//
        t1.c = a;
        t1.a= a;
    }

    void print(){
        cout <<"t1.a: " << t1.a<<endl;
        t1.print();
    }

private:
    Test1 t1;

};

Test1 gTest;

//在类的外部类里面的私有的成员变量
//1. 友元函数是没有this指针的
//2. 你要访问的是类的非静态成员，需要对象做参数
//2. 你要访问的是类的静态成员，则不需要对象做参数
//3. 如果做参数的是全局对象，则不需要对象做参数

//为什么要设计友元函数 友元类
//1.  java反射 直接修改类的私有属性
//2.  开了一个后门
void add1(Test1 t){
    t.a = t.a +t.b;
    cout << "a: " << t.a  <<t.c<<endl;
}

void print1(){
//  cout <<"gTest.a: " << gTest.a << Test1::d<<endl;
}


//类的友元函数
//1. 定义在类的外部
//2. 有权访问类的所有private/protected的成员
int main02(){
    //TODO: 友元函数 友元类
    cout<<"友元函数 友元类"<<endl;// << >> 原本的一样：位移  流的输入输出

    Test1 t1;
//     Test1::d = 10;
    Test2 t2;
    t2.setA(2);
    t2.print();


    return 0;
}

// 运算符重载
// 可以使得用户自定义的数据，使用起来更简单
/*
 * java  String  "abc" + "def"
 * c/c++ strcat
 */

class Complex{
public:

    Complex(int a=0,int i=0){
        this->a = a;
        this->i = i;
    }

    void print(){
        cout <<"Complex: ("<<a<<"+"<<i<<")"<<endl;
    }
private:
    friend Complex myAdd(Complex c1, Complex c2);
    friend Complex operator+(Complex c1,Complex c2);
    int a;
    int i;
};

Complex myAdd(Complex c1, Complex c2){
    Complex tmp(c1.a+c2.a,c1.i +c2.i);
    return tmp;
}

//void operator-(Complex i){
//
//}


Complex operator+(Complex c1,Complex c2){//看成是一个函数
    Complex tmp(c1.a+c2.a,c1.i +c2.i);
    return tmp;
}

int main(){
    //TODO: 运算符重载
    cout<<"运算符重载"<<endl;

    int a = 0;
    int b = 0;
    int c;
    c = a + b;//基础数据类型 如何运算  C++编译器是不是已经定义好了的

    //"abc " + "def"
    //复数 实数 + 虚数 a + i  加法 (a1+i1) +  (a2+i2) = (a1+a2) + (i1+i2)
    Complex c1(1,2);
    c1.print();
    Complex c2(3,4);

    Complex sum;// sum c1 + c2;C++编译器如何支持操作符重载的？
//   sum = myAdd(c1,c2);
//   sum.print();
    sum = c1 + c2;// 函数调用
    sum.print();

    //是不是所有的运算符都可以重载
    //不是
    /*不可以重载的运算符
     *  1.  . 成员访问运算符
     *  2.  .*, ->* 成员指针访问运算符
     *  3.  ::  域运算符
     *  4.  sizeof 长度运算符
     *  5.  ?: 三目运算符
     *  6.  # 预处理符
     */
     // new/delet new[] delete[] 可以的
    return 0;
}



