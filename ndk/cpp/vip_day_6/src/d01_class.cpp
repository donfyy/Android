//
// Created by Zero on 2019/10/10.
//

#include <iostream>
#include <string.h>

using namespace std;//使用命名空间std标准的命名空间

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
        cout << "有参构造函数"<<a<<" "<<i<<endl;
    }

    ~Complex(){
        cout << "析构函数"<<a<<" "<<i<<endl;
    }

    void print(){
        cout <<"Complex: ("<<a<<"+"<<i<<"i)"<<endl;
    }

    //所以的运算符 C++编译器是不是已经给我们实现了一套啦
    //重载为成员函数 二元运算符
    Complex operator-(Complex &c){
        Complex tmp(this->a - c.a,this->i - c.i);
        return tmp;
    }
//    前置-- 重载为成员函数
    Complex& operator --(){//没有用占位参数的 前置
        this->a--;
        this->i--;
        return *this;
    }

    //后置--
    Complex operator --(int){//有占位参数的  就是后置的
        Complex tmp = *this;
//    return c;
        this->a--;
        this->i--;
        return tmp;
    }

private:
    friend Complex myAdd(Complex c1, Complex c2);
    friend Complex operator+(Complex c1,Complex c2);
    friend Complex& operator ++(Complex &c);
    friend Complex operator ++(Complex &c,int);
    friend ostream& operator<<(ostream &out,Complex &c);
    int a;
    int i;
};

Complex myAdd(Complex c1, Complex c2){
    Complex tmp(c1.a+c2.a,c1.i +c2.i);
    return tmp;
}


//重载为友元函数 二元运算符
Complex operator+(Complex c1,Complex c2){//看成是一个函数
    Complex tmp(c1.a+c2.a,c1.i +c2.i);
    return tmp;
}

//前置++ 重载为友元函数
Complex& operator ++(Complex &c){
    c.a++;
    c.i++;
    return c;//返回本身
}

//后置++
Complex operator ++(Complex &c,int){//占位参数
    // 后置
    // 1. 返回Complex变量本身
    // 2. +1 -1
    Complex tmp = c; //返回局部(函数栈)的 引用 指针 都是不可以的，可能报错
//    return c;
    c.a++;
    c.i++;
    return tmp;//返回一个元素
}

//运算符重载分类
/*
 * //按照实现方式
 * 1. 运算符重载为成员函数
 * 2. 运算符重载为全局函数(友元)
 *
 * //运算符 需要的操作数
 * 1. 一元运算符 ++ -- 前置/后置
 * 2. 二元运算符 数学运算符(+ - * /)
 *  ObjectL op ObjectR
 *  重载为成员函数
 *  ObjectL.operator op(ObjectR)
 *  重载为友元函数
 *  operator op(ObjectL,ObjectR)
 */

void objTest(){
    Complex c3(3,4), c4(1,2);

    Complex result;//初始化
    result = (c3 - c4);//赋值
}

//重载为友元函数 out. << Complex 不能加成员函数
//当我们无法修改左操作数的类时，只能使用友元函数
/*
void operator<<(ostream &out,Complex &c){
    out <<"Complex: ("<<c.a<<"+"<<c.i<<"i)"<<endl;
}
 */

ostream& operator<<(ostream &out,Complex &c){
    out <<"Complex: ("<<c.a<<"+"<<c.i<<"i)"<<endl;
    return out;
}

class Name{

public:
    Name(char *pName){
        size = strlen(pName);
        this->pName = (char*)malloc(size +1);
        strcpy(this->pName,pName);
    }
    Name(const Name& name){
        size = name.size;
        this->pName = (char*)malloc(size +1);
        strcpy(this->pName,name.pName);
    }
    ~Name(){
        if(pName != NULL){
            free(pName);
            pName = NULL;
            size = 0;
        }
    }

    Name& operator= (Name& obj){
       if(this->pName != NULL){
           delete[] pName;
           size = 0;
       }
       size = obj.size;
       pName = new char[size +1];
       strcpy(pName,obj.pName);
       return *this;
    }
protected:
    char *pName;//char pName[]
    int size;
};



void objTest2(){
    Name obj1("asdfsdfw");
    Name obj2 = obj1;//默认copy构造函数 浅拷贝

    // Name& operator = (Name& obj)
}

class Test{
public:
    Test(){
        cout <<"无参构造"<<endl;
    }

    Test(int i){
        cout <<"有参构造"<<i<<endl;
    }

    void* operator new(size_t size){
        cout <<"new"<<endl;
        void *p = malloc(size);
        return p;
    }
};

int main(){
    //TODO: new运算符重载
    cout<<"new运算符重载"<<endl;


    Test *t = new Test;

    return 0;
}

int main04 (){
    //TODO: 运算符重载加深
    cout<<"运算符重载加深"<<endl;

    objTest2();

    cout<<"main结束"<<endl;

    return 0;
}

int main03(){
    //TODO: 运算符重载加深
    cout<<"运算符重载加深"<<endl;

    //1. 友元函数
    //当我们无法修改左操作数的类时，只能使用友元函数
    //2. 成员函数
    // = [] () -> 操作符 只能通过成员函数进行重载

    Complex c1(1,2);
    cout<< c1 << "sdfdfasdf " <<endl;
//    cout <<"Compex: " << c1 <<endl;
//    cout<<("Compex:");
//    cout<<(Complex& c);
//     void operator<<(ostream,Complex);

    return 0;
}


int main02(){
    //TODO: 后置++ --
    cout<<"后置++ --"<<endl;

    //前置
    Complex c1(3,3);
    c1.print();
    ++c1;
    c1.print();
    --c1;
    c1.print();
    //    ++c5;//我们需要去重载++ 支持Complex
    //1. c5 +1
    //2. 返回c5本身
    // 后置
    // 1. 返回Complex变量本身
    // 2. +1 -1
//    Complex& operator ++(Complex &c);
    //后置
    Complex c2(6,6);
    c2.print();
    c2++; // -> 首先推断出友元 Complex operator ++(Complex &c)
    c2.print();
    c2--;
    c2.print();

    return 0;
}

int main01(){
    //TODO: 运算符重载
    cout<<"运算符重载"<<endl;
//
//    int a = 0;
//    int b = 0;
//    int c;
//    c = a + b;//基础数据类型 如何运算  C++编译器是不是已经定义好了的
//
//    //"abc " + "def"
//    //复数 实数 + 虚数 a + i  加法 (a1+i1) +  (a2+i2) = (a1+a2) + (i1+i2)
//    Complex c1(1,2);
//    c1.print();
//    Complex c2(3,4);
//
//    Complex sum;// sum c1 + c2;C++编译器如何支持操作符重载的？
////   sum = myAdd(c1,c2);
////   sum.print();
//    sum = c1 + c2;// 函数调用
//    sum.print();

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


     //重载运算符的步骤
//    sum c1 + c2
//      c1.operator +(c2);1. 把操作符重载 认为是一个函数调用 -> operator +
//      推断出函数原型
// 2. 分析函数参数 根据左右操作数的个数 -> operator + (ClassName &classname)
// 3. 分析函数的返回值
//   友元 Complex operator+(Complex &c);


    objTest();
    cout<<"main结束"<<endl;

    //一元运算符重载
    int a =10;
    int b = 2;
    int c = a+b;
    a++;
    ++b;

    Complex c5(3,3);
//    ++c5;//我们需要去重载++ 支持Complex
    //1. c5 +1
    //2. 返回c5本身
//    Complex& operator ++(Complex &c);
    ++c5;
    c5.print();
    --c5;
    c5.print();

    return 0;
}



