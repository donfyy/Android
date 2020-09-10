#include <iostream>
#include <string.h>

#include "./exercise/String.cpp"

using namespace std;

void example01()
{
    MyString s1;
    MyString s2("s2");
    MyString s2_2 = "";
    MyString s3 = s2;
    MyString s4 = "s4444444444";

    s4 = s2;
    s4 = "s2222";
    s4[1] = '4';
    printf("%c\n", s4[1]);

    cout << s4 << endl;
}

class A
{
public:
    int a = 3;
    int b = 4;

    void get()
    {
        cout << "b: " << b << endl;
    }

    void print()
    {
        cout << "A print:b " << b << endl;
    }
};

class B : public A
{
public:
    int b = 1;
    int c = 2;

    void getChild()
    {
        cout << "b: " << b << endl;
    }

    void print()
    {
        cout << "B print:b " << b << endl;
    }
};

void example02()
{
    B b1;
    b1.print();
    // 同名方法
    b1.A::print();
    b1.B::print(); //默认的 b1.print();

    // 同名成员变量
    b1.b = 10;
    // 修改A类(父类)b变量的值
    b1.A::b = 20;
    b1.B::b = 30;
}

// C++的多继承
// java extends 单基础 implements接口实现多个
class Base1
{
public:
    Base1(int b1) : b1(b1)
    { //最好的方式是使用初始化列表
        //        this->b1 = b1;
    }

    void printB1()
    {
        cout << "b1: " << b1 << endl;
    }

private:
    int b1;
};

class Base2
{
public:
    Base2(int b2) : b2(b2)
    { //最好的方式是使用初始化列表
    }

    void printB2()
    {
        cout << "b2: " << b2 << endl;
    }

private:
    int b2;
};

class child : public Base1, public Base2
{ //C++多继承语法
public:
    child(int b1, int b2, int c) : Base1(b1), Base2(b2), c(c)
    {
        //        Base1(b1);//这是错误的
    }

    void printC()
    {
        cout << "c: " << c << endl;
    }

private:
    int c;
};

void example03()
{
    child c1(1, 2, 3);
    c1.printB1();
    c1.printB2();
    c1.printC();
}

class B_
{
public:
    int b;
};

class Base1_ : virtual public B_
{
};

class Base2_ : virtual public B_
{
};

class C : public Base1_, public Base2_
{
};

void example04()
{

    C c;
    c.b = 100; // 存在继承的二义性
    cout << " Base1_::b = " << c.Base1_::b << endl;
    cout << " Base2_::b = " << c.Base2_::b << endl;
    // 解决方案 虚继承
    c.Base1_::b = 20;
    c.Base2_::b = 30;
    cout << " Base1_::b = " << c.Base1_::b << endl;
    cout << " Base2_::b = " << c.Base2_::b << endl;
}

class Test
{
public:
    Test()
    {
        cout << "Test(): " << this << endl;
    }

    ~Test()
    {
        cout << "~Test(): " << this << endl;
    }
};

class Test1 : public Test
{
public:
    Test1()
    {
        cout << "Test1(): " << this << endl;
    }
    ~Test1()
    {
        cout << "~Test1(): " << this << endl;
    }
};

class Test2 : public Test1
{
public:
    Test2()
    {
        cout << "Test2(): " << this << endl;
    }
    ~Test2() // 加不加virtual父类的析构函数都会被调用
    {
        cout << "~Test2(): " << this << endl;
    }
};

void f(Test2 *t)
{
    delete t;
}

void example05()
{
    // Test2 t;
    Test2 *t = new Test2();
    f(t);
    // delete t;
}

class Parent
{
public:
    Parent(int a = 0) : a(a) {}

    virtual void print()
    {
        cout << "parent 1" << endl;
    }

    void print2()
    {
        cout << "parent print2 2" << endl;
    }

private:
    int a = 0;
};

// 虚继承是为了解决有相同成员的类？
class D : public Parent
{
public:
    D(int a = 0, int b = 0) : Parent(a), b(b) {}

    void print()
    { //只有虚函数才能产生多态
        cout << "child 1" << endl;
    }

    void print2()
    {
        cout << "child print2 2" << endl;
    }

private:
    int b = 0;
};

void objTest1(Parent *parent)
{
    parent->print();
    parent->print2();
}

void example06()
{
    Parent p1;
    D child1;
    objTest1(&p1);
    objTest1(&child1);
}

// C++的抽象类
// java abstract
class Shape
{ //抽象类,这个抽象类是无法实例化的
public:
    virtual void area() = 0; // =0 关键字 纯虚函数
    void print()
    {
        cout << "Shape" << endl;
    }

    virtual void func()
    {
    }
};

class Circle : public Shape
{
public:
    Circle(int r) : r(r) {}

    virtual void area()
    {
        cout << "Circle area: " << 3.14 * r * r << endl;
    }

    void func()
    {
    }

private:
    int r;
};

// 重写 重载 重定义
// 重写 发生在两个类 虚函数重写 多态
// 1 虚函数 重写
// 2 非虚函数 重定义
// 重载
// 必须是发生在同一个类之间
class Tri : public Shape
{
public:
    Tri(int a, int h) : a(a), h(h) {}

    virtual void area()
    {
        cout << "Tri area: " << a * h / 2 << endl;
    }

    void print()
    {
        cout << "Tri" << endl;
    }

    void print(int a)
    {
        cout << "a: " << a << endl;
    }

private:
    int a;
    int h;
};

void getArea(Shape *shape)
{
    shape->area();
    shape->print();
}

void example07()
{

    // 面向接口 抽象编程
    // Shape * base = new Shape();
    Shape *base = nullptr;
    Circle c1(10);

    Tri tr(3, 6);
    base = &c1;
    base->area(); //多态

    getArea(&c1);
    getArea(&tr);

    tr.print();
}

// C++ 类型转化
// (T)expre C 风格
// T(expre) 函数风格
/*
 * static_cast<T>(expre)
 * dynamic_cast<T>(expre)
 * const_cast<T>(expre)
 * reinterpret_cast<T>(expre)
 */

// C++的异常
class F
{
public:
    ~F()
    {
        cout << "~F" << endl;
        throw 1; //析构函数不要抛出异常
        cout << "~F end" << endl;
    }
};

void f1() throw(int)
{
    cout << "f1 start" << endl;
    int a;
    throw 2;
    cout << "f1 end" << endl;
}

void f2() throw(int)
{
    cout << "f2 start" << endl;
    F f;
    f1();
    cout << "f2 end" << endl;
}

void f3()
{
    cout << "f3 start" << endl;
    try
    {
        f2();
    }
    catch (int i)
    {
        cout << "exception: " << i << endl;
    }

    cout << "f3 end" << endl;
}

void example08()
{
    f3();
}

void example09()
{
    // 转化指针的，把指针转化为任何类型的指针
    int a = 0x7644; //ASCII v
    printf("a = %d, addr = %p\n", a, &a);
    int *ap = &a;
    char *c = reinterpret_cast<char *>(ap); // 指向了低位字节
    printf("c = %c, addr = %p\n", *c, c);
}

void func(int *i)
{
    *i = 11;
    cout << "i = " << *i << endl; // 11
}
void example10()
{
    const int a = 10;
    // func(&a);//error
    cout << "a = " << a << endl; // 10
    func(const_cast<int *>(&a)); // OK, 生成了a的副本
    cout << "a = " << a << endl; // 10
}

class B1
{
public:
    void func(){};
    virtual void func1(){};
};

class D1 : public B1
{
};

void example11()
{
    D1 *d = new D1();
    B1 *b1 = new B1();
    // 向上
    B1 *b12 = dynamic_cast<B1 *>(d);
    cout << "b12: " << b12 << endl;
    // 向下 基类必须是有虚函数
    D1 *d1 = dynamic_cast<D1 *>(b1); // 如果B1不是多态会报错，不能乡下转型
    cout << "d1: " << d1 << endl; // NULL
    D1 *d2 = dynamic_cast<D1 *>(b12);
    cout << "d2: " << d2 << endl; // Not NULL
}

void example12()
{
    double d = 3.14;
    int a = (int)d;
    int b = int(d);

    int b1 = static_cast<int>(d); //C++

    int a1 = 3;
    const int b2 = (const int)a1;
    const int c1 = static_cast<const int>(a1);
}

//C++模板
template <typename T> //模板函数
void swap1(T &a, T &b)
{
    T temp(a);
    a = b;
    b = temp;
}

class Printer
{
public:
    template <typename T>
    void print(const T &t)
    {
        cout << t << endl;
    }
};

void example13()
{ 
    int i = 1;
    int j = 2;
    swap1(i, j);
    cout << "i: " << i << " j: " << j << endl;
    double n = 1.0;
    double m = 2.0;
    swap1(n, m);
    Printer p;
    p.print(i);
    p.print(n);
}

int main(int argc, char const *argv[])
{
    // example01();
    // example02();
    // example03();
    // example04();
    // example05();
    // example06();
    // example07();
    // example08();
    // example09();
    // example10();
    // example11();
    // example12();
    example13();
    return 0;
}
