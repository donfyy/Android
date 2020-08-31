#include <stdio.h>
#include <stdlib.h> //引入头文件 java import
#include <string.h>

void test01();
void test02();
void test03();
void test04();
int main()
{
    // test01();
    // test02();
    // test03();
    test04();
}

void test2(int n)
{
    printf("test2 %d ... \n", n);
}

int plus(int i, int j)
{
    return i + j;
}

int minus(int i, int j)
{
    return i - j;
}

int apply(int a, int b, int (*callback)(int, int))
{
    return (callback(a, b));
}

void test04()
{
    //实现一个函数回调
    //函数指针
    void (*p)(int); //flutter
    // void * p(int)  //  p(int)代表一个函数，void* 代表是函数的返回值

    p = test2;
    // 函数名的值与对函数名取址的值相同
    printf(" test2 = %p\n", test2);  // 0x1000006d0
    printf("&test2 = %p\n", &test2); // 0x1000006d0
    test2(100);
    p(100);

    printf("plus of 1, 2 is %d\n", apply(1, 2, plus));
    printf("minus of 1, 2 is  %d\n", apply(1, 2, minus));

    // int *p
    // int *p[n]
    // int (*p)[n]
    // int *p()
    // int (*p)()
    // int ** p
    // 指针最重要的 就是 用指针的时候 你脑子里面一定要明确的知道你这个指针它指向哪儿
}

void test01()
{
    //指针和二维数组
    int array[5] = {300}; // int * ptr_a
    for (int i = 0; i < 5; i++)
    {
        printf("array[%d] = %d\n", i, array[i]);
    }

    const int m = 4, n = 3;
    int arr[][3] = {
        {1, 2, 3}, //首地址
        {4, 5, 6},
        {7, 8, 9},
        {10, 11, 12},
    };
    printf("         sizeof arr %d\n", sizeof(arr));  // 48
    printf("        sizeof &arr %d\n", sizeof(&arr)); // 8
    printf("     address of arr %p\n", arr);          // 0x7ffeefbffd40
    printf("    address of &arr %p\n", &arr);         // 0x7ffeefbffd40
    printf("address of &arr + 1 %p\n", &arr + 1);     // 0x7ffeefbffd70

    int(*ptr_a)[3] = arr;
    // int **ptr_a = arr; ?
    // printf("*(ptr_a[%d] + %d) = %d\n", 1, 1, **ptr_a); ?
    // 遍历数组
    for (int i = 0; i < m; i++)
    {
        for (int j = 0; j < n; j++)
        {
            // 指针偏移 *( *(ptr_a + i) + j)
            printf("*(*(ptr_a + %d) + %d) = %d\n", i, j, *(*(ptr_a + i) + j));
            // 下标 和指针偏移
            printf("*(ptr_a[%d] + %d) = %d\n", i, j, *(ptr_a[i] + j));
            // 下标 a[i][j] 或者 ptr_a[i][j]
            printf("ptr_a[%d][%d] = %d\n", i, j, ptr_a[i][j]);
        }
    }
}

void test02()
{
    // 指针数组和数组指针的区别 int * p[] 和 int (*p)[]
    char *color[] = {"red", "green", "blue"}; //这里是指针数组
    // *color[0] = "yee"; EXC_BAD_ACCESS
    color[0] = "yee"; // ok
    for (int i = 0; i < 3; i++)
    {
        char *e = color[i];
        printf("e=%s, sizeof(e)= %d\n", e, sizeof(e));
    }

    char col[][16] = {"red", "green", "blue"};
    char(*ptr_col)[16] = col; //这里是数组指针
    for (int j = 0; j < 3; j++)
    {
        // puts(ptr_col[j]);
        printf("ptr_col[j] = %s, sizeof(ptr_col[j])=%d\n", ptr_col[j], sizeof(ptr_col[j]));
    }
}

int find(const char *src, char ch, char **ret)
{
    char *index = (char *)src;
    while (*index)
    {
        if (*index == ch)
        {
            *ret = index;
            return 1;
        }
        index++;
    }
    return 0;
}

int find2(const char *src, char ch, char *ret)
{
    char *index = (char *)src;
    while (*index)
    {
        if (*index == ch)
        {
            *ret = index;
            return 1; //c bool 非零为true
        }
        index++;
    }
    return 0; //false
}

void test(int array[], int size)
{
    printf("sizeof(array) = %d\n", sizeof(array));
    array[0] = 100;
}
void test1(int *array)
{
    *(array + 1) = 520;
}

void test03()
{
    //指针的指针 ...
    //    int i = 100;
    //    int *pInt = &i;
    //    int **ppInt = &pInt;//指针的指针
    //    printf("i         =%d\n",i);
    //    printf("*pInt     =%d\n",*pInt);
    //    printf("**ppInt   =%d\n",**ppInt);
    //
    //    char str[] ="huan ying";
    //    char * ret =NULL;
    //    if(find2(str,'y',ret)){//ret = NULL;
    //        printf("%s\n",ret);
    //    }

    int array[5] = {1, 2, 3, 4, 5}; //C语言 它不会去检查数组下标越界的情况
    test(array, 5);
    test1(array);
    printf("array[0]=%d\n", array[0]);
    printf("array[1]=%d\n", array[1]);
}