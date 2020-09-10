#include <iostream>
#include <string.h>

using namespace std;

class Test
{
public:
    int i;

    Test(int i)
    {
        this->i = i;
    }

    Test &operator+(Test &t)
    {
        cout << "重载+" << endl;
        i = i + t.i;
        return *this;
    }

    bool operator&&(Test &t)
    {
        cout << "重载&&" << endl;
        return i && t.i;
    }
};

void example01()
{
    int a, b, c;
    a = b = c = 1;

    //    && ||  重载  最好不要重载这两个运算符
    bool b1 = true;
    bool b2 = false;
    bool b3 = false;
    if (b2 && b1)
    { //1 b2为false 判断 b1 不会 短路原则
    }

    if (b1 || b2)
    {
    }

    Test t1 = 0;
    Test t2(1);

    if ((t1 + t2) && t1)
    {
    }
    cout << "=======" << endl;

    if (t1 && (t1 + t2))
    {
        //t1.operator && ((t1 + t2))
    }
}

// java 继承 extends
// C++ OOP
// class Child : Parent{}//默认是private
// class Child : [权限访问]private|protected|public Parent{}
// 访问控制权限 子类(派生类)可以访问父类(基类)中的所有的非私有成员

// 子类继承父类时使用的访问权限描述符，影响的是父类成员对子类客户端的可见性。
// public 对父类成员的可见性没有影响，父类成员的可见性取决于其自身的修饰符
// protected 将父类public成员的可见性降级为protected
// private 将父类public/protected成员的可见性降级为private
class Parent
{
public:
    int a;
    void print()
    {
        cout << "a: " << a << " b: " << b << " c: " << c << endl;
    }

protected:
    int b;

private:
    int c;
};

// 默认private继承
class DefaultChild : Parent
{
    void test()
    {
        this->b = 1;
        this->a = 2;
        //        this->c = 3;
    }
};

class PrivateChild : private Parent
{
    void test()
    {
        this->b = 1;
        this->a = 2;
        //        this->c = 3;
    }
};

struct Base
{
    int a;
    int b;
    int c;
};

struct B1 : Base // 默认的public
{
};

class ProtectedChild : protected Parent
{

public:
    void test()
    {
        this->b = 1;
        this->a = 2;
        //        this->c = 3;
    }
};
class PublicChild : public Parent
{

public:
    void test()
    {
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

// 派生类访问控制
// 1 protected   修饰的成员 是为了在家族中使用，为了继承
// 在实际的项目中 全部用 public
// 我们需要知道 面试
void example02()
{
    DefaultChild defaultChild;
    // defaultChild.a = 10; // 不可访问
    // defaultChild.b = 20; // 不可访问
    // defaultChild.c = 30; // 不可访问
    // 外部可见性 private继承： 父类 public protected private -> 不可见
    PrivateChild child1;
    // child1.a = 10; // 不可访问
    // child1.b = 20; // 不可访问
    // child1.c = 30; // 不可访问
    // 外部可见性 protected继承： 父类 public protected private -> 不可见
    ProtectedChild child2;
    // child2.a = 10; // 不可访问
    // child2.b = 20; // 不可访问
    // child2.c = 30; // 不可访问

    // 外部可见性 public继承： 父类public -> 可见 protected private -> 不可见
    PublicChild child3;
    child3.a = 10; //ok
    // child3.b = 20; // 不可访问
    // child3.c = 30; // 不可访问

    B1 b1;
    b1.a = 1;
    b1.b = 2;
    b1.c = 3;
}

// C++ 类型的兼容性问题
class parent
{

public:
    parent()
    {
        //        cout<<"构造 我是爸爸..."<<endl;
    }

    parent(const parent &p)
    {
        cout << "Copy constrctor parent(const parent&) :" << this << endl;
    }

    ~parent()
    {
        //        cout<<"析构 我是爸爸..."<<endl;
    }

    /* virtual*/ void print()
    {
        //虚函数可以实现成员函数的动态绑定
        cout << "print in parent: a = " << a << endl;
    }

private:
    int a;
};

class child : public parent
{
public:
    void print()
    {
        cout << "print in child: c = " << c << endl;
    }
    void print1()
    {
        cout << "print1 in child: c = " << c << endl;
    }

private:
    int c;
};

void testPrint(parent *base)
{
    base->print();
}

void testPrint(parent &base)
{
    base.print();
}

/**
 * 非虚成员函数是静态绑定的，也就是调用变量的声明类型里的成员函数
 */
void example03()
{
    parent p1;
    p1.print();

    child c1;
    c1.print();  //调用子类print
    c1.print1(); //调用子类print
    parent &p3 = c1;
    p3.print(); //调用父类print

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
    parent &p2 = c1; //ok 向上转型
    // child &c2 = p1;  //error 向下转型 在特定的语义环境下可以强转
}

class Base1
{
public:
    Base1(int a = 0, int b = 0)
    {
        this->a = a;
        this->b = b;
        cout << "Constructor Base1(int,int): " << this << endl;
    }

    ~Base1()
    {

        this->b = b;
        cout << "Destructor ~Base1(): " << this << endl;
    }

    void print()
    {
        cout << "parent " << a << " " << b << endl;
    }

private:
    int a;
    int b;
};

class A
{
public:
    A(int i)
    {
        this->i = i;
        cout << "Constructor A(int): " << this << endl;
    }
    ~A()
    {
        cout << "Destructor ~A(): " << this << endl;
    }

private:
    int i;
};

class B2 : public Base1
{
public:
    B2(int a = 0, int b = 0, int c = 0) : a(4), Base1(a, b)
    { 
        //父类先初始化 还是子类的成员变量先初始化 ？
        this->c = c;
        cout << "Constructor B2(int, int, int): " << this << " c = " << c << endl;
    }

    // 初始化列表的使用情况
    // 1. 成员变量是一个类类型，类还是 有参数的构造函数
    // 2. const 变量
    // 3. 初始化父类的成员变量

    //    B2(int a= 0,int b= 0,int c = 0){//初始化列表
    //        Base1(a,b);
    //        this->c = c;
    //        cout<<"子类构造: "<< c<<endl;
    //    }

    ~B2()
    {
        cout << "Destructor ~B2(): " << this << " c = " << c << endl;
    }

private:
    int c;
    A a;
};
void example04()
{
    B2 b2(1, 2, 3);
    b2.print();
}

int main(int argc, char const *argv[])
{
    // example01();
    // example03();
    example04();
    return 0;
}
