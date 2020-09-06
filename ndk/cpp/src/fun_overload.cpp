#include <iostream>
using namespace std;

void test(int a)
{
    cout << "a: " << a << endl;
}

void test(char *a)
{
    cout << "a: " << a << endl;
}

void test(int a, int b)
{
    cout << "a: " << a << " b: " << b << endl;
}

// int test(int a,int b){ // 函数签名不考虑返回值
//    cout << "a: " <<a<< " b: " << b<< endl;
//    return 1;
// }

/**
 * 重载
 * 1. 函数名一致
 * 2. 参数不同(1,个数，类型)
 */
void example01()
{
    cout << "函数重载" << endl;
    test(1);
    test("aaaaaa");
    test(1, 2);
}

// 可以重载有默认参数的函数，但需要注意调用重载函数时不要产生二义性。
void test1(int a, int b)
{
    cout << "a: " << a << " b: " << b << endl;
}

void test1(int a, int b, int c = 3)
{
    cout << "a: " << a << " b: " << b << endl;
}

void test1(int a)
{
    cout << "a: " << a << endl;
}

void example02()
{
    // test1(1,2);//产生二义性
}

int main(int argc, char const *argv[])
{
    example01();
    example02();
    return 0;
}
