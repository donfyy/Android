//
// Created by Zero on 2019/10/10.
//

#include <iostream>
#include <string.h>

using namespace std;//使用命名空间std标准的命名空间

class A {
public:

    A(int a) {
        m_a = a;
        cout << "构造函数: " << m_a << endl;
    }

    ~A() {
        cout << "析构函数" << endl;
    }

    int getMA() {
        return m_a;
    }

private:
    int m_a;
};

/*
 * 构造函数的初始化列表
 * 语法：ClassName():m1(xxx),m2(xxx),m3(xxx)
 * 1. 为了在B类中组合了一个A类对象(A类有有参数的构造函数)
 * 2. 初始化const type 常变量，必须用初始化列表
 */
class B {
public:
//    B(int b,A a):m_a(2),m_b(12)//构造函数的初始化列表 默认值
//        m_b = b;
//        m_a = a;
//    }

    B() : m_a2(3), m_a1(2), m_a(m_a2), m_b(10),c(3) {//初始化列表一定要注意顺序问题，是一种错误的习惯
    }

    B(int b) :  m_a(2), m_a2(3), m_a1(4),c(5) {//正确的初始化列表顺序应该和声明的变量的顺序是一致的

    }

    void print() {
        cout << m_a.getMA() << " " << m_b << endl;
    }

private://初始化列表里面初始化的顺序是按照我们成员变量声明的顺序，不是初始化列表的顺序
    int m_b;//在初始化列表里面 m_a(1),m_b(2) 这个是谁先初始化
    A m_a;
    A m_a2;//编译器它不知道要调用哪个构造函数
    A m_a1;

    const int c;//常变量java final
};


//我要初始化B的b1的时候，那么我必须先初始化B类的成员变量A类型 m_a
//默认构造函数
/*
 * 1. 默认的无参构造函数，当我们的类没有定义构造函数时，C++编译器默认会提供一个无参构造函数，函数体是空实现
 * 2. 当我们自己写了构造函数之后，系统不会给我们提供默认无参的构造函数了
 * 3. 默认拷贝构造函数
 * 4. 只要你写了构造函数，C++编译器就不会提供默认的
 */
int main01() {
    //TODO: 多个对象构造和析构
    cout << "多个对象构造和析构" << endl;

//    int b(12);
//
//    bool bb(true);
//
//    float f(3.14);
//    float f1 = 1.2;
//
//    cout << "b: " << b << " bb: " << bb << " f: " << f << endl;
    //java 自动装箱 自动拆箱

//    B b1(4, A(45));
//    b1.print();

    //初始化列表顺序研究
    B b2;

    return 0;
}
//构造函数再去调用构造函数是一个危险的行为
class Test{

public:
    Test(int a,int b,int c){
       this->a = a;
       this->b = b;
       this->c = c;
    }

    Test(int a,int b){
        this->a = a;
        this->b = b;

        Test(a,b ,10);//构造函数里面调用另外一个构造函数 新产生一个匿名对象
    }

    void print(){
        cout << a<< " " << b << " " << c <<endl;
    }


    ~Test(){
        cout<<"析构函数"<<endl;
    }

private:
    int a;
    int b;
    int c;//int 默认值是0，但是不要相信编译器给的默认值
};
//如何在C++里面正确的初始化一个对象
int main02(){
    //TODO: 构造函数研究
    cout<<"构造函数研究"<<endl;
    //在栈上面创建对象和释放对象

    Test t1(1,2);
    t1.print();

    return 0;
}

// new delete /new[]  delete[]
// 对象的动态创建和释放
// 在栈上创建对象 和释放对象 与 在堆上创建对象和释放对象 的区别
// 栈
// 1. 在栈上创建的对象，一经创建，对象的大小是无法改变的
// 2. 在栈上的对象 系统自动创建和销毁
// 堆
// 1. 堆上申请的内存空间 是可以动态调整的
// 2. 堆上的申请的空间，必须自己申请与释放

//   malloc/calloc free C语言的  是函数
//   new  delete   C++的语法 new   delete 它们是属于运算符 不是函数

int main03(){
    //TODO: new delete基本语法
    cout<<"new delete基本语法"<<endl;
    sizeof(int);//这也是运算符

    int *p  = (int*)malloc(sizeof(int));//在堆上申请了一块内存空间
    *p = 10;

    free(p);//释放内存
    p = nullptr;

    int *p1 = new int;//在堆上申请内存 分配基础类型

    *p1 = 20;
    cout << "*p1: " << *p1<<endl;
    delete p1;

    int *p2 = new int(30);

    cout << "*p2: " << *p2<<endl;
    delete(p2);

    cout << "*p1: " << *p1<<endl;//这里有问题


    return 0;
}

int main04(){
    //TODO: 在堆上分配数组
    cout<<"在堆上分配数组"<<endl;
    // C
    int *array = (int*)malloc(sizeof(int) *10); // int array[10]
    array[0] = 20;

    free(array);//释放内存

    //C++
    int *array1 = new int[10];//int array[10] C++的方式在堆上申请了一个int[10]的数组
    array1[2] = 3;
//    delete array1; //对吗？

     delete[] array1;//这才是正取的释放数组

     // new/delete  与new[]/delete[] 是两组不同的运算符


    return 0;
}

//复杂类型

class Test1{
public:
    Test1(int a){
       m_a = a;
        cout<<"构造函数 "<< m_a<<endl;
    }

    void print(){
        cout <<m_a <<endl;
    }

    ~Test1(){

        cout<<"析构函数 "<< m_a<<endl;
    }
private:
    int m_a;
};

int main(){
    //TODO: 在堆上分配复杂类型对象
    cout<<"在堆上分配复杂类型对象"<<endl;

    //c
    Test1 *pt1 = (Test1*) malloc(sizeof(Test1));
    pt1->print();
    free(pt1);
    cout <<"================"<<endl;
    //c++
    Test1 *pt2 = new Test1(10);//会自动调用构造函数
    pt2->print();
    delete pt2;//会自动调用析构函数

    return 0;
}


