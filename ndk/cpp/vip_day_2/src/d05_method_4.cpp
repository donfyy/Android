//
// Created by Zero on 2019/10/13.
//
#include <iostream>
using namespace std;

void myfunc5(int a){
    cout <<"a: " <<a<<endl;
}


void myfunc5(char* a){
    cout << "a: " <<a<<endl;
}

void myfunc5(int a,int b){
    cout << "a: " <<a<< " b: " << b<< endl;
}

void myfunc5(char* a,char* b){
    cout << "a: " <<a<< " b: " << b<< endl;
}


//函数指针 基础的语法
typedef void (myTypeFunc5)(int a,int b);//声明了一个函数类型

//声明一个函数指针类型
typedef void (*myPFunc5)(int a,int b);
//typedef void (*myPFunc5)(char* a,char* b);

typedef int SIZE_OF;

int main51(){
    //TODO: 函数指针与函数重载
    cout<<"函数指针与函数重载"<<endl;
    SIZE_OF a;
//    myTypeFunc5 * func = NULL;
//    func = myfunc5;
//    func(1,2);
    myPFunc5 func =nullptr;
    myPFunc5 func1 = myfunc5;
    func = myfunc5;
    func(1,2);
//    func(1);
//    func(1);
//char buf1[] = "aaaaa";
//char buf2[] = "adfwer";
//func(buf1,buf2);

    return 0;
}
