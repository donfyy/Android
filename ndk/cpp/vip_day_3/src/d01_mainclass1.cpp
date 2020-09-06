//
// Created by Zero on 2019/10/10.
//

#include <iostream>
#include <string.h>

using namespace std;//使用命名空间std标准的命名空间


/*
 * 构造函数
 * 1. 没有返回值
 * 2. 函数名称和类名相同 ClassName(){}
 * 析构函数
 * 1. 没有返回值
 * 2. 函数名称和类名相同 ~ClassName(){}
 */
class Test {
public:

    Test() {//构造函数 初始化工作

        a = 10;
        p = (char *) malloc(100);//给p变量在堆区分配了一块内存 colloc
        strcpy(p, "adgabasdfer");

        cout << "构造函数" << endl;
    }

    ~Test() {//析构函数 释放资源的工作
        //析构函数是当我们的对象被销毁之前，由编译器主动去调用的
        if (p != NULL) {
            free(p);
            p = NULL;
        }
        cout << "析构函数" << endl;
    }

    void print() {//成员函数
        cout << "a: " << a << " p: " << p << endl;
    }


private:
    int a;//类的成员变量
    char *p;

};

void objTest() {
    Test t1;//创建了一个对象
    t1.print();
}

int main01() {
    //TODO: C++类与对象
    cout << "C++类与对象" << endl;

    objTest();

    return 0;
}


//构造函数分类
class Test2 {
public:

    void init(int a, int b) {
        m_a = a;
        m_b = b;
    }

    Test2() {//无参数构造函数
        m_a = 0;
        m_b = 0;
        cout << "无参构造函数 " << m_a << " " << m_b << endl;
    }

    Test2(int a) {//有参数的构造函数
        m_a = a;
        m_b = 0;
        cout << "有参构造函数 " << m_a << " " << m_b << endl;
    }

    Test2(int a, int b) {//有参数的构造函数
        m_a = a;
        m_b = b;
        cout << "有参构造函数 " << m_a << " " << m_b << endl;
    }

    Test2(const Test2 &obj) {//是一个拷贝构造函数

        m_a = obj.m_a;
        m_b = obj.m_b;
        cout << "拷贝构造函数 " << m_a << " " << m_b << endl;
    }

    ~Test2() {
        cout << "析构函数 " << m_a << " " << m_b << endl;
    }

    void print() {
        cout << "print " << m_a << " " << m_b << endl;
    }

    int getA() {
        return m_a;
    }

private:
    int m_a;
    int m_b;

};


void objTest1() {
    Test2 tl;
}

//有参数的构造函数调用
void objTest2() {

    //C++编译器自动调用的构造函数
//    //1. 括号法
    Test2 t1(1);//c++编译器自动的调用对应的构造函数
    Test2 t2(2, 3);
//
//    //2. =号法
    Test2 t3 = (4, 5, 6, 7, 8);//= C++对"=" 功能增强，调用了构造函数
    Test2 t4 = 9;//这里是不是把9赋值给t4;,不是的，这里调用了Test2的构造函数

    //3. 直接调用构造函数，手动调用的构造函数
    Test2 t5 = Test2(1, 2);

    t1 = t5;//把 t5 copy给 t1 //赋值操作

    //调用了拷贝构造函数
    Test2 t6 = t5;

    Test2 t7(t5);
    //对象的初始化和对象的赋值  是两个不同的概念


}

void objTest3() {
    Test2 t1;
    t1.init(2, 3);

    Test2 tArr[3];
    tArr[0].init(1, 2);
    tArr[1].init(1, 2);
    tArr[2].init(1, 2);
}


//拷贝构造函数的调用时机
//3.用实参去初始化一个函数的形参的时候会调用拷贝构造函数
void copyTest(Test2 t) {

    cout << t.getA() << endl;
}

void objTest4() {

    Test2 t1(1, 2);
    Test2 t2 = t1;//1 =
    Test2 t3(t2);//2 ()
    t2.print();

    cout << "t2初始化完毕" << endl;
    copyTest(t2);// 当我们用t2 实参初始化形参t的时候，会自动调用拷贝构造函数

}

//4.
Test2 getTest2() {
    Test2 t(4, 5);
    cout<<"t初始化"<<endl;
    return t;
}

void objTest5() {

//    getTest2();

    Test2 t1 = getTest2();//初始化 t1, 创建一个匿名对象，(扶正)从匿名转成了有名字了 t1
    cout << "getTest2调用完毕" << endl;
    t1.print();

}

void objTest6() {

//    getTest2();

    Test2 t1(1,2);//t1已经初始化了
    t1 = getTest2();//赋值
    cout << "getTest2调用完毕" << endl;
    t1.print();

}
//objTest5
//有参构造函数 4 5
//getTest2调用完毕
//        print 4 5
//析构函数 4 5
//==========================
//objTest6
//有参构造函数 1 2
//有参构造函数 4 5
//析构函数 4 5 getTest2的t被析构
//getTest2调用完毕
//        print 4 5
//析构函数 4 5

int main() {
    //TODO: 构造函数分类
    cout << "构造函数分类" << endl;


//    objTest1();
//    objTest2();
//    objTest3();
//    objTest4();
//    objTest5();
    cout <<"=========================="<<endl;
    objTest6();
    return 0;
}

/*
 * 默认拷贝函数只可以完成对象的数据成员的简单复制 传值
 * 当对象的数据资源是由指针指向堆时，我们需要考虑深拷贝
 */
class Test3 {
public:

    Test3() {//构造函数 初始化工作

        a = 10;
        p = (char *) malloc(100);//给p变量在堆区分配了一块内存 colloc
        strcpy(p, "adgabasdfer");

        cout << "构造函数" << endl;
    }

    Test3(const char *arg){
        a = 20;
        p = (char *) malloc(100);//给p变量在堆区分配了一块内存 colloc
        strcpy(p, arg);
        cout << "构造函数" << endl;
    }

    Test3(const Test3& t){//深拷贝
        a = t.a;
        p = (char *) malloc(100);
        strcpy(p,t.p);
    }

    ~Test3() {//析构函数 释放资源的工作
        //析构函数是当我们的对象被销毁之前，由编译器主动去调用的
        if (p != NULL) {
            free(p);
        }
        cout << "析构函数" << endl;
    }

    void print() {//成员函数
        cout << "a: " << a << " p: " << p << endl;
    }


private:
    int a;//类的成员变量
    char *p;

};

void objTest7(){
    Test3 t1("adfwe");

    Test3 t2 = t1;

    cout<<"objTest7调用"<<endl;

}

int main3(){
    //TODO: 浅拷贝和深拷贝
    cout<<"浅拷贝和深拷贝"<<endl;


//    objTest7();
    cout <<"main 调用"<<endl;

    return 0;
}