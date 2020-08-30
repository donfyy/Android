#include <stdio.h>
#include <stdlib.h>//引入头文件 java import


void test_0();

void test_1() {

    int i = 258;
    int j = 10000;
    int *ptr_i = &i;

    printf("%p\n", &i);
    printf("%p\n", &j);
    printf("ptr_i自己的地址=%p\n", &ptr_i);
}

void test_2() {

    int i = 258;
    int j = 10000;
    int *ptr_i = &i;

    printf("%p\n", &i);
    printf("%p\n", &j);
    printf("ptr_i自己的地址=%p\n", &ptr_i);

    printf("========================\n");
    ptr_i = (int*)malloc(sizeof(int));//使用malloc函数在堆上分配了一块内存给ptr_i
    printf("ptr_i自己的地址=%p\n", &ptr_i);
    printf("ptr_i=%p\n", ptr_i);//这里是指针所指向变量的内存地址，可以指向任何地址
    free(ptr_i);
    //prt_i 悬空指针，野指针 是一样的危险，必须避免
    ptr_i = NULL;//0地址，一般不能被访问
    printf("ptr_i=%p\n", ptr_i);

    //总结一波
    // &运算符，用于取一个对象的地址
    // *是一个间接寻址符，用于访问指针所指向的地址的值
    // int *ptr 定义一个指针
    // int b = *ptr 用于访问指针所指向的地址的值
    // & 与 * 是一对互逆运算

    int var = 222, *ptr;
    ptr = &var;
    int temp = *ptr;

    printf("var = %d\n",var);
    printf("*ptr = %d\n",*ptr);
    printf("temp = %d\n",temp);

}

// 值传递，不会影响最终的结果
void swap(int x,int y){
    printf("交换前: x= %d, y= %d\n",x,y);
    int tmp = x;
    x = y;
    y = tmp;
    printf("交换前: x= %d, y= %d\n",x,y);
}

// 指针传递，变量的地址
void swap1(int* x,int* y){
    printf("交换前: x= %p, y= %p\n",x,y);
    printf("交换前: *x= %d, *y= %d\n",*x,*y);
    int tmp = *x;
    *x = *y;
    *y = tmp;
    printf("交换后: x= %p, y= %p\n",x,y);
    printf("交换后: *x= %d, *y= %d\n",*x,*y);
}

void test_3(){
    int a = 2,b = 3;
    printf("交换前: a= %d, b= %d\n",a,b);
    swap(a,b);
    printf("交换后: a= %d, b= %d\n",a,b);

    a= 20,b=30;
    printf("===========================\n");
    printf("交换前: a= %d, b= %d\n",a,b);
    printf("交换前: &a= %p, &b= %p\n",&a,&b);
    swap1(&a,&b);
    printf("交换后: a= %d, b= %d\n",a,b);
}

void test_4(){

    int i1[] = {11,22,33,44,55};
    int *p1 = i1;
    printf("i1[0]=%p\n", i1[0]);
    printf("&i1[0]=%p\n", &i1[0]);
    printf("i1=%p\n", i1);
    printf("&i1=%p\n", &i1);
    printf("p1=%p\n", p1);

}



int main() {


    //指针 最重要，最核心 灵魂
    // int float char 指针 数据类型
    // int i=10 10数值
    // int *ptr_i = &i;  ptr_i 存的是地址
    // 指针是一种特殊的变量(指针变量)，它保存另一个变量的内存地址

//    test_0();
//    test_1();
//    test_2();
//    test_3();
    test_4();



    return 0;
}

void test_0() {

    int i = 258;
    int *ptr_i = &i;//存储是另一个变量的内存地址

    printf("%p\n", &i);
    printf("%p\n", ptr_i);
    printf("*ptr_i=%d\n", *ptr_i);
    printf("ptr_i自己的地址=%p\n", &ptr_i);// 指针它本身也是一种变量类型，它也会有自己的地址
    printf("int*的大小=%d\n", sizeof(int *));

    printf("char*的大小=%d\n", sizeof(char *));

    void *ptr_v = ptr_i;
    printf("Ptr_v=%p\n", ptr_v);
    printf("void*的大小=%d\n", sizeof(void *));//void* 是一种特殊的指针，其他类型的指针都可以赋值给void*

    printf("long long*的大小=%d\n", sizeof(long long *)); //int* , char* long long* 区别在哪儿？

}



