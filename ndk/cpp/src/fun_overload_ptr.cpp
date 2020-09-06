#include <iostream>
using namespace std;

void fun01(int a)
{
    cout << "a: " << a << endl;
}

void fun01(char *a)
{
    cout << "a: " << a << endl;
}

void fun01(int a, int b)
{
    cout << "a: " << a << " b: " << b << endl;
}

void fun01(char *a, char *b)
{
    cout << "a: " << *a << " b: " << *b << endl;
}

//函数指针 基础的语法
typedef void fun_int_2(int, int); //声明了一个函数类型
typedef void fun_char_2(char*, char*);

//声明一个函数指针类型
typedef void (*fun_int_2_p)(int, int);
//typedef void (*fun_int_2_p)(char* a,char* b);

int main()
{
    cout << "函数指针与函数重载" << endl;
    fun_int_2 *p_fun_int_2 = fun01;
    p_fun_int_2(1, 2);

    fun_char_2 *p_fun_char_2 = fun01;
    char ca = 'a';
    char cb = 'b';
    p_fun_char_2(&ca, &cb);
    // p_fun_char_2(1, 1);  // 不可以

    fun_int_2_p p1_fun_int_2 = fun01;
    p1_fun_int_2(1, 2);
    // 重载不会反映到函数指针上
    // p1_fun_int_2(1);
    // p1_fun_int_2(1);
    // char buf1[] = "aaaaa";
    // char buf2[] = "adfwer";
    // p1_fun_int_2(buf1,buf2);

    return 0;
}
