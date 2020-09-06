#include <iostream>
using namespace std;

void printA(); //方法声明

#define MYFUNC(a, b) ((a) < (b)) ? (a) : (b)

//C++ 内联函数 inline
inline void printB()
{ //内联函数 声明时候必须实现，没办法分开，如果分开了C++编译器会取消内联
    int a = 10;
    cout << "a: " << a << endl;
    for (int i = 0; i < 10000; i++)
    { //有循环不会内联
    }
    //if else switch 条件判断的语句过多 取消内联
}

//1. 内联函数 声明时候必须实现，没办法分开，如果分开了C++编译器会取消内联
//2. 内联函数必须放在调用它的方法的前面
//3. C++编译器不一定会允许函数内联
//限制：
/*
 * 1. 不能存在任何形式的循环语句
 * 2. 不能存在过多的条件判断语句
 * 3. 函数体不能过于庞大
 * 4. 不能对函数进行取地址操作
 * 5. 函数内联声明必须在调用语句之前
 */
// 当我们的函数本身代码执行的代价 大于函数调用压栈出栈的开销的时候，内联将没有任何意义

//g++ __attribute__((always_inline)) 属性 ，强制内联
void example01()
{
    cout << "函数" << endl;
    printA();
    //C语言中
    MYFUNC(1, 2);
    printB();
    // 把printB的代码嵌入到调用点
    // int a = 10;
    // cout <<"a: " << a<<endl;
}

void printA()
{
    cout << "printA" << endl; //C++编译器有可能会给你内联
}

//inline void printB(){
//    //C++编译器会取消内联
//}
