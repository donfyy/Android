#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void test01()
{
    // C语言的字符串，就是字符数组
    // " aaa string"
    // "3234sadsk \ 折行符
    // sddd"

    int a[10];  // int数组
    char c[10]; // 字符数组

    //C语言字符串用字符数组表示
    char str[10] = {'h', 'e', 'l', 'l', 'o', '\0'}; // 字符串是以'\0'结束的字符数组
    char str1[10] = "hello";                        // '\0'可以省略的 栈
    char *str2 = "hello";                           // str2在栈中分配内存，"hello"在常量区中分配内存 可以读，不可以写
    printf("str=%s\n", str);
    printf("str1=%s\n", str1);
    printf("str2=%s\n", str2);
}

int main2()
{
    //TODO:
    printf("输入一个字符串\n");
    char str[11];
    //    scanf("%s",str);//溢出问题 崩溃
    //   gets(str);
    fgets(str, 11, stdin);
    printf("str=%s\n", str);

    return 0;
}

int mystrlen(char *str)
{
    int i = 0;
    while (*(str + (++i)))
        ; //'\0' ASCII 0 非零为true
    return i;
}

int main3()
{
    //TODO: 如何给字符数组赋值
    printf("给字符数组赋值\n");

    char str[10] = "abc";
    //    //循环
    int i;
    for (i = 0; i < 10; i++)
    {
        str[i] = 'i';
    }
    // str = i i i i i i i i i i
    printf("str=%s\n", str);
    //    strcpy(str,"dekrje");
    // str = d e k r j e \0 i i i
    printf("str=%s\n", str);

    printf("sizeof(str)=%d\n", sizeof(str));
    printf("strlen(str)=%d\n", strlen(str)); //'\0'没有算进去
    printf("mystrlen(str)=%d\n", mystrlen(str));

    return 0;
}

void mystrcat(char *s1, char *s2)
{
    //s1 = abc
    //s2 = 123
    //s1+s2 = abc123
    while (*s1)
        s1++;
    while (*s1++ = *s2++)
        ;
}

int main4()
{
    //TODO:
    printf("字符串拼接\n");
    char s1[] = "abc";
    char s2[] = "123";

    //    char *s11= "abc";
    //    char *s21 = "123";
    mystrcat(s1, s2);
    printf("s1 = %s\n", s1);

    //    char s3[10];
    //    char *s4 = (char*)calloc(10,sizeof(char));
    //    scanf("%s",s4);
    //    printf("s3=%s\n",s4);

    char *s5 = "he";
    strcpy(s1, s5);
    printf("s1 = %s\n", s1);

    char str[10] = "abc";
    strncpy(str, "123456", 3);
    printf("str=%s\n", str);

    printf("%d\n", strcmp("a", "b"));

    return 0;
}

int main5()
{
    //TODO:
    printf("字符串数组\n");
    char str[4][6] = {"abcd", "1234", "aaa", "bbbbb"};
    char *str1[4] = {"abc", "bbb", "ccc"};
    printf("str[0]=%s\n", str[0]);
    //指针常量 指向的地址不可以改变了，
    //    str[0] = "aaaa";//str[0]是一个一维数组的数组名(常量)，不能作为左值
    // 指针所指向的地址里面的值是可以修改的
    strcpy(str[0], "aaaa");
    printf("str[0]=%s\n", str[0]);
    printf("str1[0]=%s\n", str1[0]);
    //常量指针，指针的指向可以改变
    str1[0] = "ABC";
    printf("str1[0]=%s\n", str1[0]);
    //指针指向的值是不能修改的
    strcpy(str1[0], "BBB"); //编译没报错，运行是报错
    printf("str1[0]=%s\n", str1[0]);

    const char *p = "abc";  //常量指针,
    char const *p1;         //常量指针
    p = p1;                 //指针的指向可以修改
    strcpy(p, "aaa");       //错误，指针所指向的值不可以修改
    char *const p2 = "abc"; //指针常量，指向的地址不可以修改

    return 0;
}

//结构图
struct Books
{
    char title[100];
    char author[10];
    int book_id;
} book = {"c语言基础", "zero", 1};
int main6()
{
    //TODO:
    printf("Hello world!\n");

    struct Books book1;
    strcpy(book1.title, "c programming");
    strcpy(book1.author, "av");
    book1.book_id = 2;
    printf("sizeof(book1)=%d\n", sizeof(book1));
    struct Books *p;
    p = &book1;
    printf("book1.title = %s\n", book1.title);
    printf("book1.author = %s\n", book1.author);
    printf("book1.book_id = %d\n", book1.book_id);

    printf("p->title = %s\n", p->title);
    printf("p->author = %s\n", p->author);
    printf("p->book_id = %d\n", p->book_id);

    printf("p->title = %s\n", (*p).title);
    printf("p->author = %s\n", (*p).author);
    printf("p->book_id = %d\n", (*p).book_id);
    return 0;
}

union Data
{
    int i;
    float f;
    char str[20];
};

void test07() {
    //TODO:
    printf("Hello world!\n");

    union Data data;
    data.i = 10;
    data.f = 3333.2;
    strcpy(data.str, "cccccc");

    printf("sizeof(data)=%d\n", sizeof(data));
    printf("data.i = %d\n", data.i);
    printf("data.f = %d\n", data.f);
    printf("data.str = %s\n", data.str);
}

int main()
{
    test01();
    return 0;
}
