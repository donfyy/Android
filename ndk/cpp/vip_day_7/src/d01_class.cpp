#include <iostream>
#include <string.h>
#include "MyArray.h"

//作业1  String类  自己实现C++的String类  java String可以怎么用，C++的String也应该有同样的功能 运算符重载

using namespace std;//使用命名空间std标准的命名空间

int main01() {
    //TODO: 继承
    cout << "继承" << endl;

    MyArray a1(10);

    for (int i = 0; i < a1.size(); i++) {
//        a1.setData(i, i);
        a1[i] = i;

    }

    cout << "打印数组a1: ";
    for (int i = 0; i < a1.size(); i++) {
        cout << a1[i] << " ";
    }
    cout << endl;

    MyArray a2 = a1;
    cout << "打印数组a2: ";
    for (int i = 0; i < a2.size(); i++) {
        cout << a2.getData(i) << " ";
    }
    cout << endl;

    //3
    MyArray a3(5);
    {
        a3 = a1;
        a3 = a2 = a1;

        cout << "打印数组a3: ";
        for (int i = 0; i < a3.size(); i++) {
            cout << a3[i] << " ";
        }
    }

    if (a3 == a1) {
        cout << "相等" << endl;
    } else {
        cout << "不相等" << endl;
    }

    if (a3 != a1) {
        cout << "不相等" << endl;
    } else {
        cout << "相等" << endl;
    }
    return 0;
}

class Test {
public:
    int i;

    Test(int i) {
        this->i = i;
    }

    Test &operator+(Test &t) {
        cout << "重载+" << endl;
        i = i + t.i;
        return *this;
    }

    bool operator&&(Test &t) {
        cout << "重载&&" << endl;
        return i && t.i;
    }
};

int main02() {
    //TODO:
    cout << "" << endl;


    int a, b, c;
    a = b = c = 1;

//    && ||  重载  最好不要重载这两个运算符
//
    bool b1 = true;
    bool b2 = false;
    bool b3 = false;
    if (b2 && b1) {//1 b2为false 判断 b1 不会 短路原则

    }

    if (b1 || b2) {

    }

    Test t1 = 0;
    Test t2(1);

    if ((t1 + t2) && t1) {

    }
    cout << "=======" << endl;

    if (t1 && (t1 + t2)) {
        //t1.operator && ((t1 + t2))
    }

    return 0;
}

//java 继承 extends
//C++ OOP
// class Child : Parent{}//默认是private
// class Child : [权限访问]private|protected|public Parent{}
//访问控制权限 子类(派生类)可以访问父类(基类)中的所有的非私有成员

//作业2 成员 public protected private  继承关系 public protected private 做成一个表格

//         成员修饰          public  protected  private
//继承关系        public
//               protected
//               private
class Parent {
public:
    int a;// 名字
    void print() {
        cout << "a: " << a << " b: " << b << " c: " << c << endl;
    }

protected:
    int b;//银行密码
private:
    int c;//老婆
};

//默认继承
class Child : Parent {//默认private
    void test() {
        this->b = 1;
        this->a = 2;
//        this->c = 3;
    }
};

//私有继承
class Child1 : private Parent {
    void test() {
        this->b = 1;
        this->a = 2;
//        this->c = 3;
    }
};

struct Base {
    int a;
    int b;
    int c;
};

struct B1 :  Base {//默认的public

};

//保护继承
class Child2 : protected Parent {

public:
    void test() {
        this->b = 1;
        this->a = 2;
//        this->c = 3;
    }
};

//公有继承
class Child3 : public Parent {

public:
    void test() {
        this->b = 1;
        this->a = 2;
//        this->c = 3;
    }
};



/*
 * public 修饰的成员变量 函数  在类的内部，类的外部都能使用
 * protected 修饰的成员变量 函数  在类的内部使用，在继承的子类中可用，其他 类的外部都不能使用
 * private 修饰的成员变量 函数  在类的内部使用，其他 情况下都不能使用
 */

//派生类访问控制
//1 protected   修饰的成员 是为了在家族中使用，为了继承
//在实际的项目中 全部用 public
// 我们需要知道 面试
int main03() {
    //TODO: 继承
    cout << "继承" << endl;

    Child child;
//    child.a = 10; //error
//   child.b = 20;  //error
//   child.c = 30;  //error
// 外部可见性 private继承： 父类public protected private -> 不可见
    Child1 child1;
//    child1.a = 10; //error
//   child1.b = 20;  //error
//   child1.c = 30;  //error

// 外部可见性 protected继承： 父类public protected private -> 不可见
    Child2 child2;
//    child2.a = 10; //error
//   child2.b = 20;  //error
//   child2.c = 30;  //error

// 外部可见性 public继承： 父类public -> 可见 protected private -> 不可见
    Child3 child3;
    child3.a = 10; //ok
//   child3.b = 20; //error
//   child3.c = 30; //error

    B1 b1;
    b1.a = 1;
    b1.b = 2;
    b1.c = 3;

    return 0;
}


//C++ 类型的兼容性问题

class parent{

public:

    parent(){
//        cout<<"构造 我是爸爸..."<<endl;
    }

    parent(const parent& p){
//        cout<<"copy构造 我是爸爸..."<<endl;
    }

    ~parent(){
//        cout<<"析构 我是爸爸..."<<endl;
    }

    virtual void print(){ //虚函数 多态 抛个砖块....
        cout<<"我是爸爸: "<<a <<endl;
    }
private:
    int a;
};

class child: public parent{
public:
    void print(){
        cout<<"我是儿子： " <<c<<endl;
    }
    void print1(){
        cout<<"我是儿子： " <<c<<endl;
    }
private:
    int c;
};


void testPrint(parent *base){
    base->print();
}

void testPrint(parent& base){
    base.print();
}

int main04(){
    //TODO: 兼容性问题
    cout<<"兼容性问题"<<endl;

    parent p1;
//    p1.print();

    child c1;
//    c1.print();//调用父类
//    c1.print1();//调用子类

    //兼容性问题
    //使用基类指针(引用) 指向子类对象
    //java面向接口编程
    parent *p = nullptr;
    p = &c1;
//    p->print();//print()是调用那个 C++编译器调用时候，是根据指针的类型来判断
// java 呈现成多态

    //指针作为函数参数
    testPrint(&p1);
    testPrint(&c1);

    //引用作为函数参数
    testPrint(p1);
    testPrint(c1);

    // 用子类对象 初始化父类对象
    parent p2 =  c1;//ok 向上转型
//    child c2 = p1;//error 向下转型 在特定的语义环境下可以强转


    return 0;
}

//继承的构造与析构
class Base1{
public:
    Base1(int a = 0,int b = 0){
        this->a = a;
        this->b = b;
        cout<<"父类构造函数"<<endl;
    }

    ~Base1(){

        this->b = b;
        cout<<"父类析构函数"<<endl;
    }

    void print(){
        cout <<"父类: " << a<<" " << b<<endl;
    }
private:
    int a;
    int b;
};

class A{
public:
    A(int i){
        this->i = i;
        cout<<"A构造函数"<<endl;
    }
    ~A(){
        cout<<"A析构函数"<<endl;
    }
private:
    int i;
};

class B2 :public Base1{
public:
    B2(int a= 0,int b= 0,int c = 0):a(4),Base1(a,b){//初始化列表
        //父类先初始化 还是子类的成员变量先初始化 ？
        this->c = c;
        cout<<"子类构造: "<< c<<endl;
    }

//初始化列表的使用情况
// 1. 成员变量是一个类类型，类还是 有参数的构造函数
// 2. const 变量
// 3. 初始化父类的成员变量

//    B2(int a= 0,int b= 0,int c = 0){//初始化列表
//        Base1(a,b);
//        this->c = c;
//        cout<<"子类构造: "<< c<<endl;
//    }

    ~B2(){
        cout<<"子类析构: "<< c<<endl;
    }
private:
    int c;
    A a;
};

void testObj(){
    B2 b2(1,2,3);
    b2.print();
}

int main(){
    //TODO: 继承中的构造析构问题
    cout<<"继承中的构造析构问题"<<endl;


    testObj();

    return 0;
}









