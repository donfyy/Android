//
// Created by Zero on 2019/10/13.
//

#include <iostream>
using namespace std;


void test(int a){
    cout << "a: " <<a<<endl;
}

void test(char* a){
    cout << "a: " <<a<<endl;
}

void test(int a,int b){
    cout << "a: " <<a<< " b: " << b<< endl;
}

//int test(int a,int b){
//    cout << "a: " <<a<< " b: " << b<< endl;
//    return 1;
//}

//重载
/*
 * 1. 函数名一致
 * 2. 参数不同(1,个数，类型)
 */
int main41(){
    //TODO: 函数重载
    cout<<"函数重载"<<endl;

    test(1);
    test("aaaaaa");
    test(1,2);
    return 0;
}

//函数重载 函数默认参数 在一起

void test1(int a,int b){
    cout << "a: " <<a<< " b: " << b<< endl;
}

void test1(int a,int b,int c = 3){
    cout << "a: " <<a<< " b: " << b<< endl;
}

void test1(int a){
    cout << "a: " <<a<< endl;
}

int main42(){
    //TODO:
    cout<<""<<endl;
//    test1(1,2);//函数调用时，会产生二义性
    return 0;
}
