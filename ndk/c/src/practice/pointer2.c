#include <stdio.h>
#include <stdlib.h> //引入头文件 java import

void test01()
{
    //通用指针 void*
    int n;
    char c;
    short s;
    //指针类型 分别是int char short
    int *ptr_n = &n;
    char *ptr_c = &c;
    short *prt_s = &s;

    printf("ptr_n=%p\n", ptr_n);
    void *ptr_void = ptr_n; //把一个整型指针转化成了一个通用指针，地址不会改变，但是类型信息丢失
    printf("ptr_void=%p\n", ptr_void);

    int *ptr_n_1 = (int *)ptr_void;
    double *ptr_double = (double *)ptr_n; //语法正确，存在语义错误，编译器无法识别
}

void test02()
{
    //指针的算术运算
    //1, 对于变量，不同的类型，占用的空间大小是不一样
    //2, 对于不同类型的指针变量，它们占用的空间都是相同的
    //为了指针算术运算
    int i = 100;
    int *prt_i = &i;
    printf("prt_i=%p\n", prt_i);
    printf("prt_i+1=%p\n", prt_i + 1);
    printf("prt_i+2=%p\n", prt_i + 2);

    short c = 100;
    short *prt_c = &c;
    printf("prt_c=%p\n", prt_c);
    printf("prt_c+1=%p\n", prt_c + 1);
    printf("prt_c+2=%p\n", prt_c + 2);
    printf("sizeof(void)=%d\n", sizeof(void));
    //指针占4位，步进长度与其所指类型有关
    //只能进行加法和减法：+，-， ++，--，+=，-=
}

void test03()
{
    //指针与一维数值
    //数组名就是数值的起始地址，也是第一个元素的地址，数组名就是一个常量指针
    int array[] = {1, 2, 3, 4, 5};
    int *ptr_array = array;
    // ptr_array = ptr_array +1;
    printf("ptr_array=%p\n", ptr_array);
    printf("&array=%p\n", &array);
    printf("&array[0]=%p\n", &array[0]);
    ptr_array[2] = 100;
    *(ptr_array + 3) = 200;
    printf("array[2]=%d\n", array[2]);
    //*(array+3), *&array[3],*(ptr_array+3),*&ptr_array[3] 代表的是array[3]
    printf("*(array+3)=%d\n", *(array + 3));
    printf("*&array[3]=%d\n", *&array[3]);
    printf("\n");
    printf("*(ptr_array+3)=%d\n", *(ptr_array + 3));
    printf("\n");
    printf("ptr_array[3]=%d\n", ptr_array[3]);
    printf("&(ptr_array[3])=%p\n", &(ptr_array[3]));
    printf("*&(ptr_array[3])=%d\n", *&(ptr_array[3]));
    printf("\n");
    //array+3,&array[3], ptr_array+3,&ptr_array[3] 代表是同一个地址
    printf("array+3=%p\n", array + 3);
    printf("&array[3]=%p\n", &array[3]);
    printf("ptr_array+3=%p\n", ptr_array + 3);
    printf("&ptr_array[3]=%p\n", &ptr_array[3]);
}

void test04()
{
    //数组指针与指针数组
    //* [] ()
    // int (*ptr_array)[5] 数组指针：到数组int[5]的指针
    // int *ptr_arr[5]     指针数组：数组中的元素为指针int *
    int array[] = {1, 2, 3, 4, 5};
    //    int *ptr_array = array;
    printf("sizeof(&array) = %d\n", sizeof(array)); // 5 * 4
    int(*ptr_arr)[3] = &array;
    printf("sizeof(&ptr_arr) = %d\n", sizeof(*ptr_arr)); // 3 * 4
    printf("array=%p\n", array);
    printf("&array=%p\n", &array);
    printf("\n");
    printf("array+1=%p, *(array+1)=%d\n", array + 1, *(array + 1));
    printf("&array+1=%p,*(&array+1)=%d\n", &array + 1, *(&array + 1)); //偏移的array[5]类型的 20字节
    printf("ptr_arr+1=%p,*(ptr_arr+1)=%d\n", ptr_arr + 1, *(ptr_arr + 1));

    int *pointer = array;
    printf("pointer = %#lX\n", pointer);
    printf("pointer = %#llX\n", pointer);
    printf("pointer = %#X\n", pointer);
    printf("pointer = %p\n", pointer);
    printf("%lu\n", sizeof(pointer));
}

void test05()
{
    int array[] = {1, 2, 3, 4, 5};
    // int *ptr_array = &array;//错误的 把它当成了一个int类型的指针
    // 这里编译器警告说，不兼容的指针类型，程序使用了int (*)[5]来初始化int (*)[4], 但可以继续编译。
    // int (*ptr_array)[4] = &array;
    int(*ptr_array)[5] = &array; //数组指针
    for (int i = 0; i < 5; i++)
    {
        printf("array[%d] = %d, *(array + %d) = %d\n", i, array[i], i, *(array + i));
        printf("&array[%d] = %p, array+%d = %p\n", i, &array[i], i, array + i);
    }
    printf("\n");
    // 以指针的方式访问数组
    for (int *p = array; p < array + 5; p++)
    {
        printf("p = %p, *p = %d\n", p, *p);
    }
}

void test06()
{
    int array[5] = {0};
    printf("        array = %p\n", array);         // 0x7ffeefbffd60
    printf("       &array = %p\n", &array);        // 0x7ffeefbffd60 &array 返回的是一个到int[5]的指针
    printf("    array + 1 = %p\n", array + 1);     // 0x7ffeefbffd64
    printf("&array[0] + 1 = %p\n", &array[0] + 1); // 0x7ffeefbffd64
    printf("   &array + 1 = %p\n", &array + 1);    // 0x7ffeefbffd74 步长为sizeof(int[5]) == 20
    printf("\n");
    printf(" sizeof(int*) = %d\n", sizeof(int *));            // 8: 指针
    printf(" sizeof(&array[0]) = %d\n", sizeof(&array[0]));   // 8: 指针
    printf(" sizeof(0xffffcc04) = %d\n", sizeof(0xffffcc04)); // 4: int
    printf(" sizeof(array) = %d\n", sizeof(array));           // 20 数组类型的长度
    printf("sizeof(&array) = %d\n", sizeof(&array));          // 8 计算的是指针的长度

    // 在C中， 在几乎所有使用数组的表达式中，数组名的值是个常量指针(*const)，也就是数组第一个元素的地址。
    // 它的类型取决于数组元素的类型： 如果它们是int类型，那么数组名的类型就是“指向int的常量指针“。——《C和指针》
    // 在以下两中场合下，数组名并不是用指针常量来表示，就是当数组名作为sizeof操作符和单目操作符&的操作数时。
    // sizeof返回整个数组的长度，而不是指向数组的指针的长度。
    // 取一个数组名的地址所产生的是一个指向数组的指针，而不是一个指向某个指针的指针。
    // 所以&a后返回的是数组第一个元素的地址而不是a自身的地址，跟a（一个指向a[0]的指针）在指针的类型上是有区别的。——《C和指针》
    // "+1",偏移量的问题，一个类型为T的指针的移动，是以sizeof(T)为移动单位
    // 即array+1：在数组首元素的首地址的基础上，偏移一个sizeof(array[0])单位,此处的类型T就是数组中的一个int型的首元素
    // 即&array+1：在数组的首地址的基础上，偏移一个sizeof(array)单位。此处的类型T就是数组中的一个含有5个int型元素的数组
    printf("\n");
}

int main()
{
    // test05();
    test04();
}
