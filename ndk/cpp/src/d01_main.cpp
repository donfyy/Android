#include <iostream>

using namespace std;//使用命名空间std标准的命名空间









int main01() {
    //TODO: vip_day_1
    cout << "vip_day_1" << endl;

    //求圆的面积
    double r = 0;
    double s = 0;

    cout << "请输入圆的半径";//printf 改造增强
    cin >> r;//scanf 操作符重载 以后讲

    cout << "r的值是： " << r << endl;// \n flush
    s = 3.14 * r * r;
    cout << "圆的面积： " << s << endl;

    return 0;
}

class MyCicle {
public:
    double m_s;//圆的面积
    double m_r;//圆的半径
public:
    void setR(double r) {//类的成员函数
        m_r = r;
    }

    double getR() {
        return m_r;
    }

    double getArea() {
        m_s = 3.14 * m_r * m_r;
        return m_s;
    }
};

int main02() {
    //TODO: c++的方式求圆的面积
    cout << "c++的方式求圆的面积" << endl;

    MyCicle c1, c2, c3;//用类定义变量对象
    double r;
    cout << "请输入圆的半径";//printf 改造增强
    cin >> r;//scanf 操作符重载 以后讲
    c1.setR(r);

    cout << "圆的面积： " << c1.getArea() << endl;

    return 0;
}

// namespace 指标识符的各种可见范围 std
namespace namespaceA {
    int a = 10;
}

namespace namespaceB {
    int a = 20;
    namespace namespaceC {
        struct Teacher {
            char name[20];
            int age;
        };
    }
}

int main03() {
    //TODO: 命名空间
    cout << "命名空间" << endl;
    cout << "namespaceA: " << namespaceA::a << endl;
    using namespace namespaceA;
    using namespace namespaceB;
//    cout<< "namespaceA: " << a <<endl;


    cout << "namespaceA: " << namespaceA::a << endl;
    cout << "namespaceB: " << namespaceB::a << endl;

    namespaceB::namespaceC::Teacher t1;
    t1.age = 33;
    using namespace namespaceC;
    Teacher t2;
    t2.age = 34;

    return 0;
}

//新增bool
// c++中的bool类型 要么是true ,要么是false
//bool 理论上 只占用一个字节 = 8 bit
// 如果多个bool变量定义在一起的时候，可能各自占一个bit
//取决于编译器的实现
//true代表真值，编译器内部用1表示
//false代表假值，编译器内部用0表示
//c++编译器会在赋值的时候将非0转换为true,0转化给false
int main04() {
    //TODO: bool类型
    cout << "bool类型" << endl;
    bool b1 = true;//c++编译器
    cout << "sizeof(bool): " << sizeof(bool) << endl;
    b1 = 10;//要么是1，要么是0
    cout << "b1: " << b1 << endl;
    b1 = -1;
    cout << "b1: " << b1 << endl;
    b1 = 0;
    cout << "b1: " << b1 << endl;
    return 0;
}

struct Teacher{
    char name[20];
    int age;
};

int operatorT1(const Teacher *pT){//常量指针，表示是一种输入参数
//    pT->age = 23;
   cout << pT->age<<endl;
   pT = NULL;
}

int operatorT2(Teacher * const pT){
    pT->age =2;//输出参数
//    pT = NULL;
}

// c/c++ const
/*
 * const的好处
 * 1. 指针函数参数，可以有效的提高代码的可读性，减少bug
 * 2. 清楚的区分参数是输入还是输出特性
 *
 * const  和 #define相同与不相同的地方
 * #define 是在编译预处理阶段，只是简单的文本替换
 * const 是由编译器处理的，提供类型检查和作用域检查
 */



int main05() {
    //TODO: const的基本用法
    cout << "const的基本用法" << endl;
    const int a = 10;
    int const b = 20;//一样的

    const int *c;//常量指针 const修饰的是指针所指向的变量
    //代表指针所指向的内存空间，不能被修改
    c = &a;
    c = &b;
//    *c = 30;//常量指针不允许修改指针所执向的内存空间
    int a1 = 1;
    int *const d = &a1;//指针常量，const修饰的是指针本身，指向不可变
    const int *const e = &a1;//常量指针常量
    //指针的指向不能改变，所指向的内存空间也不能改变

    return 0;
}

#define d1 20

void fun1(){
#define a 10
    const int b = 20;
#undef a;//卸载   MFC
}

void fun2(){
//    printf("a = %d \n",a);
//    printf("b = %d \n",b);
}

int main06(){
    //TODO: const 和 #define
    cout<<"const 和 #define"<<endl;
    int a = 10;
    int b = 20;
//    int array[a + b];linux内核可以，gcc编译器支持
    const int c =10;
    const int d = 20;
    int array2[c + d];
    int array3[c + d1];

    return 0;
}

int main(int argc, char const *argv[])
{
    cout << "hello world!" << endl;
    return 0;
}
