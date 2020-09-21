#include <iostream>
#include <string.h>

using namespace std;

/*
 * 构造函数
 * 1. 没有返回值
 * 2. 函数名称和类名相同 ClassName(){}
 * 析构函数
 * 1. 没有返回值
 * 2. 函数名称和类名相同 ~ClassName(){}
 */
class Test
{
public:
    Test()
    {
        //构造函数 初始化工作
        a = 10;
        p = (char *)malloc(100); //给p变量在堆区分配了一块内存 colloc
        strcpy(p, "steady");

        cout << "构造函数" << endl;
    }

    ~Test()
    {
        //析构函数 释放资源的工作
        //析构函数是当我们的对象被销毁之前，由编译器主动去调用的
        if (p != NULL)
        {
            free(p);
            p = NULL;
        }
        cout << "析构函数" << endl;
    }

    void print()
    {
        //成员函数
        cout << "a: " << a << " p: " << p << endl;
    }

private:
    int a; //类的成员变量
    char *p;
};

void example01()
{
    Test t1; // 创建了一个对象
    t1.print();
}

struct TestStruct
{
    int b;

private:
    int a;

public:
    TestStruct(int a_) : a(a_)
    {
        cout << "TestStruct(int) " << this << endl;
    }
    ~TestStruct()
    {
        cout << "~TestStruct() " << this << endl;
    }
};
    class Date1
    {
        int d, m, y;

    public:
        Date1(int, int, int);
        void add_year(int);
    };
    struct Date2
    {
    private:
        int d, m, y;

    public:
        Date2(int, int, int);
        void add_year();
    };
void example01_struct()
{
    TestStruct t(1);
    cout << "t.b = " << t.b << endl;
}

//构造函数分类
class Test2
{
public:
    void init(int a, int b)
    {
        m_a = a;
        m_b = b;
    }

    Test2()
    {
        m_a = 0;
        m_b = 0;
        cout << "Default constructor: " << m_a << " " << m_b << endl;
    }

    Test2(int a)
    {
        m_a = a;
        m_b = 0;
        cout << "Constructor taking a int: " << m_a << " " << m_b << endl;
    }

    Test2(int a, int b)
    {
        m_a = a;
        m_b = b;
        cout << "Constructor taking two int: " << m_a << " " << m_b << endl;
    }

    Test2(const Test2 &obj)
    {
        m_a = obj.m_a;
        m_b = obj.m_b;
        cout << "Copy constructor: " << m_a << " " << m_b << endl;
    }

    ~Test2()
    {
        cout << "Destructor: " << m_a << " " << m_b << endl;
    }

    void print()
    {
        cout << "print " << m_a << " " << m_b << endl;
    }

    int getA()
    {
        return m_a;
    }

private:
    int m_a;
    int m_b;
};

// 拷贝构造函数的调用时机
// 3.用实参去初始化一个函数的形参的时候会调用拷贝构造函数
void takingObject(Test2 t)
{
    cout << t.getA() << endl;
}

Test2 returnObject()
{
    Test2 t(4, 5);
    return t;
}

void example02_01()
{
    // Test2 t = (1, 2, 3);
    // Test2 t = 9;
    // Test2 t(1);
    // Test2 t{1};
    // Test2 t = {1};
    // Test2 t = Test2(1);
    // Test2 t1(1);
    // Test2 t2(2);
    // t1.print();    // 1
    // t1 = t2;       // 对象之间赋值，没有新建对象
    // t1.print();    // 2
    // Test2 t3 = t1; // 调用拷贝构造函数 对象初始化
    // Test2 t4(t1);  // 调用拷贝构造函数 对象初始化
    const Test2 t(1);
    takingObject(t); // 调用拷贝构造函数
}

// 有参数的构造函数调用
void example02()
{

    // C++编译器自动调用的构造函数
    // 1. 函数风格的参数表
    Test2 t1(1); //c++编译器自动的调用对应的构造函数
    Test2 t2(2, 3);
    // 2. =号法
    Test2 t3 = (4, 5, 6, 7, 8); // = C++对"=" 功能增强，调用了构造函数
    Test2 t4 = 9;               // 这里是不是把9赋值给t4;,不是的，这里调用了Test2的构造函数

    //3. 直接调用构造函数，手动调用的构造函数
    Test2 t5 = Test2(1, 2);

    t1 = t5; //把 t5 copy给 t1 //赋值操作

    //调用了拷贝构造函数
    Test2 t6 = t5;

    Test2 t7(t5);
    //对象的初始化和对象的赋值是两个不同的概念
}

void example02_02()
{
    // 使用构造函数，编译器可以确保成员变量会被初始化
    Test2 t1;
    t1.init(2, 3);

    Test2 tArr[3];
    tArr[0].init(1, 2);
    tArr[1].init(1, 2);
    tArr[2].init(1, 2);
}

void example03()
{
    Test2 t1(1, 2);
    Test2 t2 = t1; //1 =
    Test2 t3(t2);  //2 ()
    t2.print();

    takingObject(t2); // 当我们用t2 实参初始化形参t的时候，会自动调用拷贝构造函数
}

void example04()
{
    // returnObject();
    Test2 t1 = returnObject(); //初始化 t1, 创建一个匿名对象，(扶正)从匿名转成了有名字了 t1
    t1.print();                // 注意：这里只创建了1个对象，t1就是返回的对象。

    Test2 t2(1, 2);
    t2 = returnObject(); // 返回的对象在其值赋给t2后立即被销毁(在t2.print()之前被销毁)
    // 🤔：对象赋值操作是如何实现的？内存拷贝？
    t2.print();
}
// example04
// 有参构造函数 4 5
// print 4 5
// 析构函数 4 5
// ==========================
// 有参构造函数 1 2
// 有参构造函数 4 5
// 析构函数 4 5 returnObject的t被析构
// print 4 5
// 析构函数 4 5
static int g_id;
class Test3
{
public:
    Test3();
    Test3(const char *arg);
    Test3(const Test3 &t);
    ~Test3();
    Test3 &operator=(Test3 &t);
    void print();

private:
    int id = g_id++;
    char *p;
};
Test3::Test3()
{
    p = (char *)malloc(100);
    strcpy(p, "steady!");
    cout << "Test3(): id = " << id << endl;
}
Test3::Test3(const char *arg)
{
    p = (char *)malloc(100);
    strcpy(p, arg);
    cout << "Test3(const char*): id = " << id << endl;
}
Test3::Test3(const Test3 &t)
{
    p = (char *)malloc(100);
    strcpy(p, t.p);
    cout << "Test3(const Test3 &): id = " << id << endl;
    // p = t.p; Oops! t1和t2指向了同一块堆内存，t1被析构后，该堆内存被释放，但t2仍在使用该堆内存
}
Test3::~Test3()
{
    if (p != NULL)
        free(p);
    cout << "~Test3() :id = " << id << endl;
}
Test3 &Test3::operator=(Test3 &t)
{
    strcpy(p, t.p);
    return *this;
}
void Test3::print()
{
    cout << "id: " << id << " p: " << p << endl;
}
class Test4
{
private:
    int id = g_id++;
    int value = 0;

public:
    Test4();
    Test4(int value);
    void print();
};
Test4::Test4() {}
Test4::Test4(int value) : value(value)
{
    cout << "Test4(int): id = " << id << endl;
}
void Test4::print()
{
    cout << "id = " << id << " value = " << value << endl;
}

// 赋值运算符以及拷贝构造函数的默认实现是浅拷贝，
// 如果类内部分配了堆内存，则该类需要重写析构函数，拷贝构造函数，以及赋值运算符等。
// 构造函数的调用顺序：t1 -> t2 -> t3
// 析构函数的调用顺序：t1 <- t2 <- t3
void example05()
{
    Test3 t1("t1");
    t1.print();
    Test3 t2 = t1;
    t2.print();
    Test3 t3("t3");
    t3.print();
    t3 = t1;
    t3.print();
}

void example06()
{
    Test4 t1(1);
    t1.print();
    Test4 t2 = t1;
    t2.print();
    Test4 t3(2);
    t3.print();
    t3 = t1;
    t3.print();
}

int main()
{
    // example01();
    example01_struct();
    // example02_01();
    // example03();
    // example04();
    // example05();
    // example06();

    return 0;
}