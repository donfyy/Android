#include <iostream>
#include <string.h>

using namespace std;

class Complex
{
public:
    Complex(int a = 0, int i = 0);
    Complex(Complex &c);
    ~Complex();
    Complex operator-(Complex &operand2);

private:
    friend Complex operator+(Complex c1, Complex c2);
    friend Complex &operator++(Complex &c);
    friend Complex operator++(Complex &c, int);
    friend Complex &operator--(Complex &c);
    friend Complex operator--(Complex &c, int);
    friend ostream &operator<<(ostream &out, Complex &c);
    int a;
    int i;
};
Complex::Complex(int a, int i)
{
    this->a = a;
    this->i = i;
    cout << "Constructor(int, int): " << this << " a: " << this->a << " i: " << this->i << endl;
}
Complex::Complex(Complex &c)
{
    this->a = c.a;
    this->i = c.i;
    cout << "Copy constructor(Complex&): " << this << " a: " << this->a << " i: " << this->i << endl;
}
Complex::~Complex()
{
    cout << "Destructor(int, int): " << this << " a: " << this->a << " i: " << this->i << endl;
}
Complex Complex::operator-(Complex &operand2)
{
    return Complex(this->a - operand2.a, this->i - operand2.i);
}

//重载为友元函数 二元运算符
Complex operator+(Complex c1, Complex c2)
{ //看成是一个函数
    Complex tmp(c1.a + c2.a, c1.i + c2.i);
    return tmp;
}

// 前置++(++c) 重载为友元函数
Complex &operator++(Complex &c)
{
    c.a++;
    c.i++;
    return c; //返回对象自身
}
// 后置++(c++)
Complex operator++(Complex &c, int /*占位参数*/)
{
    Complex t(c);
    c.a++;
    c.i++;
    return t;
}

Complex &operator--(Complex &c)
{
    c.a--;
    c.i--;
    return c;
}
Complex operator--(Complex &c, int)
{
    Complex t(c);
    c.a--;
    c.i--;
    return t;
}

//重载为友元函数 out. << Complex 不能加成员函数
//当我们无法修改左操作数的类时，只能使用友元函数
ostream &operator<<(ostream &out, Complex &c)
{
    out << "Complex: (" << c.a << "+" << c.i << "i): " << &c << endl;
    return out;
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
void example01()
{
    // 是不是所有的运算符都可以重载
    // 不是
    /* 不可以重载的运算符
     *  1.  . 成员访问运算符
     *  2.  .*, ->* 成员指针访问运算符
     *  3.  ::  域运算符
     *  4.  sizeof 长度运算符
     *  5.  ?: 三目运算符
     *  6.  # 预处理符
     */
    // new/delet new[] delete[] 可以的

    // 重载运算符的步骤
    // sum c1 + c2
    // c1.operator +(c2);1. 把操作符重载 认为是一个函数调用 -> operator +
    // 推断出函数原型
    // 2. 分析函数参数 根据左右操作数的个数 -> operator + (ClassName &classname)
    // 3. 分析函数的返回值
    // 友元 Complex operator+(Complex &c);

    // Complex c3(3, 4), c4(1, 2);
    // Complex result = (c3 - c4); // 赋值

    // 一元运算符重载
    // 1. c +1
    // 2. 返回c本身
    // Complex& operator ++(Complex &c);
    Complex c(3, 3);
    ++c;
    cout << c; // 4
    Complex c1 = c++;
    cout << c1; // 4
    cout << c;  // 5
    --c;
    cout << c; // 4
    Complex c2 = c--;
    cout << c2; // 4
    cout << c;  // 3
}

class Name
{
public:
    Name(char *pName);
    Name(const Name &name);
    ~Name();
    Name &operator=(Name &obj);
    friend ostream &operator<<(ostream &out, Name &n);
    void *operator new(size_t size);

protected:
    char *pName; //char pName[]
    int size;
};
Name::Name(char *pName)
{
    size = strlen(pName);
    this->pName = (char *)malloc(size + 1);
    strcpy(this->pName, pName);
    cout << "Constructor(char*): " << this << " name: " << this->pName << endl;
}
Name::Name(const Name &name)
{
    size = name.size;
    this->pName = (char *)malloc(size + 1);
    strcpy(this->pName, name.pName);
    cout << "Copy constructor(Name&): " << this << " name: " << this->pName << endl;
}
Name::~Name()
{
    cout << "Destructor(): " << this << " name: " << this->pName << endl;
    if (pName != NULL)
    {
        free(pName);
        pName = NULL;
        size = 0;
    }
}
Name &Name::operator=(Name &obj)
{
    if (this->pName != NULL)
    {
        delete[] pName;
        size = 0;
    }
    size = obj.size;
    pName = new char[size + 1];
    strcpy(pName, obj.pName);
    return *this;
}

ostream &operator<<(ostream &out, Name &n)
{
    out << "Name (" << n.pName << ") : " << &n << endl;
    return out;
}

void *Name::operator new(size_t size)
{
    void *p = malloc(size);
    cout << "new (" << size << "): " << p << endl;
    return p;
}

void example02()
{
    // 默认copy构造函数 浅拷贝
    Name alice("Alice");
    Name alice2 = alice;
    Name bob("Bob");
    alice2 = bob;
    cout << alice2;

    Name *alice3 = new Name("3");
    cout << *alice3;
    delete alice3;
}

namespace test1
{

    class A
    {
        int a;
        friend void ff(A&);
    private:
    int b;
        /* data */
    public:
        A(/* args */);
        ~A();
    };

    A ::A(/* args */)
    {
    }

    A ::~A()
    {
    }

    void ff(A& a) {
        cout << "ff: A::a = " << a.a << endl;
    }
} // namespace test1
void example03() {
    test1::A a;
    test1::ff(a);
}
int main(int argc, char const *argv[])
{
    // example01();
    // example02();
    example03();
    return 0;
}
