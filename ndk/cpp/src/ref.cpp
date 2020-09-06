#include <iostream>
using namespace std;

struct Teacher
{
    char name[20];
    int age;
};

void example01()
{
    cout << "引用的基本用法" << endl;

    int a = 10;
    // type & name = var;
    int &b = a; //b就是一个引用,请不要用C的语法是思考
    //就是给a变量取了一个别名
    printf("b:%d\n", b); // 10
    printf("a:%d\n", a); // 10

    b = 100;
    printf("b:%d\n", b); // 100
    printf("a:%d\n", a); // 100
    // type * const 指针常量
    // int &c;//会报错的，普通的引用必须要依附某个变量，必须初始化
    int c = 20;
    b = c; // 这里是给b所指的的对象a赋值，并不是让b引用c
    // 一个引用的值在初始化之后就不能改变了，它总是引用初始化时所指的那个对象，因此引用必须初始化。
    c = 30;
    printf("b:%d\n", b); // 20
    printf("a:%d\n", a); // 20
    printf("c:%d\n", c); // 30
}

void myswap(int a, int b)
{ //值传递
    int tmp = a;
    a = b;
    b = tmp;
} //完成不了我们的功能
void myswap1(int *a, int *b)
{
    int tmp = *a;
    *a = *b;
    *b = tmp;
}
void myswap2(int &a, int &b)
{
    int tmp = a;
    a = b;
    b = tmp;
}

void example02()
{
    cout << "基本类型的引用" << endl;
    int x, y;
    x = 10;
    y = 20;
    myswap(x, y);
    printf("x:%d, y:%d \n", x, y);

    myswap1(&x, &y);
    printf("x:%d, y:%d \n", x, y);

    int num1, num2;
    num1 = 1;
    num2 = 2;

    myswap2(num1, num2); //a 就是num1的别名，b就是num2的别名
    printf("num1:%d, num2:%d \n", num1, num2);
}

void printT1(const Teacher *pT)
{
    cout << pT->age << endl;
}

void printT2(Teacher &pT)
{
    cout << pT.age << endl;
    //pT是一个别名
    pT.age = 33;
}

void printT3(Teacher pT)
{ //值传递
    cout << pT.age << endl;
    //pT是一个别名
    pT.age = 36;
}

int main13()
{
    //TODO: 复杂类型引用的使用
    cout << "复杂类型引用的使用" << endl;
    Teacher t1;
    t1.age = 35;
    printT1(&t1);
    printT2(t1); //pT 是t1的别名
    printT3(t1); //pT是形参， t1 copy一份数据给pT //pT = t1;
    printf("t1.age:%d\n", t1.age);

    return 0;
}

void modifyA(int &a)
{
    //引用
    a = 11;
}

void modifyA1(int *const a)
{ //指针常量
    *a = 12;
}

struct Teacher1
{
    char name[64];
    int age; // 8
    int &a;  //8
    int &b;  //8
};

void modifyA3(int *p)
{
    *p = 200;
}

void example07()
{
    cout << "引用的本质思考？" << endl;

    int a = 10;
    int &b = a; // b像一个常量
    modifyA(a); //函数调用的时候，我们程序员不需要取a的地址
    //请思考：对同一内存空间 可以取好几个名字吗？
    //引用就是取别名
    //从使用的角度，引用会让人误会其只是一个别名，没有自己的存储空间
    printf("&a:%p, &b:%p \n", &a, &b);

    modifyA1(&a); //指针，我们需手动取地址
    //引用在C++的内部实现 就是一个常量指针

    printf("sizeof(Teacher1): %d \n", sizeof(Teacher1));
}

int main()
{
    // example01();
    example02();
    return 0;
}
