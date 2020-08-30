#include <stdio.h>
#include <stdlib.h> //引入头文件 java import

void test01()
{
    //指针和二维数组
    int array[5] = {0}; // int * ptr_a

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
    //遍历数组  作业 1
    // 下标 a[i][j] 或者 ptr_a[i][j]
    // 指针偏移 *( *(ptr_a + i) + j)
    // 下标 和指针偏移
}

int main02()
{
    // 指针数组 和数组指针的区别 int * p[] 和 int (*p)[]

    char *color[] = {"red", "green", "blue"}; //这里是指针数组
    int i;
    for (i = 0; i < 3; i++)
    {

        char *tmp = color[i];
        printf("tmp=%s，sizeof(tmp)= %d\n", tmp, sizeof(tmp));
        //        *tmp ='a';
    }

    char col[][16] = {"red", "green", "blue"};
    char(*pcol)[16] = col; //这里是数组指针
    int j;
    for (j = 0; j < 3; j++)
    {
        puts(pcol[j]);
        printf("sizeof(pcol[j])=%d\n", sizeof(pcol[j]));
    }

    return 0;
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
{ //作业//
    printf("sizeof(array) = %d\n", sizeof(&array + 1));
    array[0] = 100;
}
void test1(int *array)
{
    *(array + 0) = 100;
}

void test09()
{
    //作业 find可以 find2不行 swap
    //指针的指针 ...
    //    int i = 100;
    //    int *pInt = &i;
    //    int **ppInt = &pInt;//指针的指针
    //    printf("i         =%d\n",i);
    //    printf("*pInt     =%d\n",*pInt);
    //    printf("**ppInt   =%d\n",**ppInt);
    //
    //    char str[] ="huan ying lai xiangxue";
    //    char * ret =NULL;
    //    if(find2(str,'y',ret)){//ret = NULL;
    //        printf("%s\n",ret);
    //    }

    int array[5] = {1, 2, 3, 4, 5}; //C语言 它不会去检查数组下标越界的情况
    // test(array);
    //    test1(array);
    printf("array[0]=%d\n", array[0]);
}
int main()
{
    test01();
}

void test2(int n)
{
    printf("test1 %d ... \n", n);
}

int call(int i, int j)
{
    return i + j;
}

int call1(int i, int j)
{
    return i - j;
}

int test3(int a, int b, int (*callback)(int, int))
{
    return (callback(a, b));
}

int main04()
{ //实现一个函数回调
    //函数指针
    void (*p)(int); //flutter
    // void * p(int)  //  p(int)代表一个函数，void* 代表是函数的返回值

    p = &test2;
    test2(22);
    p(100);

    printf("xxx =%d\n", test3(1, 2, call));
    printf("xxx =%d\n", test3(1, 2, call1));

    // int *p
    // int *p[n]
    // int (*p)[n]
    // int *p()
    // int (*p)()
    // int ** p
    // 指针最重要的 就是 用指针的时候 你脑子里面一定要明确的知道你这个指针它指向哪儿

    return 0;
}
