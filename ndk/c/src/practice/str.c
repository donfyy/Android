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

    // C语言字符串用字符数组表示
    char str[10] = {'h', 'e', 'l', 'l', 'o', '\0'}; // 字符串是以'\0'结束的字符数组
    char str1[10] = "hello";                        // '\0'可以省略的 栈
    char *str2 = "hello";                           // str2在栈中分配内存，"hello"在常量区中分配内存 可以读，不可以写
    printf("str=%s\n", str);
    printf("str1=%s\n", str1);
    printf("str2=%s\n", str2);
}

void test02()
{
    printf("输入一个字符串\n");
    char str[11];
    // scanf("%s",str);//溢出问题 崩溃 [1]    68033 abort      ./str
    // gets(str); // [1]    68120 abort      ./str
    fgets(str, 11, stdin);
    printf("str=%s\n", str);
}

int mystrlen(const char *str)
{
    const char *p = str;
    while (*p)
        p++;
    return p - str;
}

void test03()
{
    // 如何给字符数组赋值
    printf("给字符数组赋值\n");

    char str[10] = "abc";
    // 循环
    for (int i = 0; i < 10; i++)
    {
        str[i] = 'i';
    }
    str[9] = 0;
    // str = i i i i i i i i i i
    printf("str=%s\n", str);
    strcpy(str, "dekrje");
    // strcpy(str, "1234567890abc"); // EXC_BAD_INSTRUCTION (code=EXC_I386_INVOP, subcode=0x0)
    // str = d e k r j e \0 i i i
    printf("str=%s\n", str);
    printf("sizeof(str)=%lu\n", sizeof(str));
    printf("strlen(str)=%lu\n", strlen(str)); //'\0'没有算进去
    printf("mystrlen(str)=%d\n", mystrlen(str));
}

void mystrcat(char *dest, char *src)
{
    while (*dest)
        dest++;
    while ((*dest++ = *src++))
        ;
}

void test04()
{
    printf("字符串拼接\n");
    char s1[] = "abc";
    char s2[] = "123";

    printf("sizeof(s1) = %lu\n", sizeof(s1)); // "sizeof(s1) = 4"
    // strcat(s1, s2);                           // EXC_BAD_INSTRUCTION
    mystrcat(s1, s2);
    printf("sizeof(s1) = %lu, s1 = %s\n", sizeof(s1), s1); // "sizeof(s1) = 4, s1 = abc123" with mystrcat

    //    char s3[10];
    //    char *s4 = (char*)calloc(10,sizeof(char));
    //    scanf("%s",s4);
    //    printf("s3=%s\n",s4);

    char *s5 = "he";
    strcpy(s1, s5);
    printf("s1 = %s\n", s1);
    char str[10] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
    strncpy(str, "123456", 4);
    printf("str=%s\n", str);
    printf("%d\n", strcmp("a", "b"));
}

void test05()
{
    printf("字符串数组\n");
    char str[4][6] = {"abcd", "1234", "aaa", "bbbbb"};
    char *str1[4] = {"abc", "bbb", "ccc"};
    printf("str[0] = %s\n", str[0]);
    // 常量指针(* const) 指向的地址不可以改变了，
    // str[0] = "aaaa";// str[0]是一个一维数组的数组名(常量)，不能作为左值
    // 指针所指向的地址里面的值是可以修改的
    strcpy(str[0], "aaaa");
    printf("str[0] = %s\n", str[0]);
    printf("str1[0] = %s\n", str1[0]);
    //指针常量(const *)，指针的指向可以改变
    str1[0] = "ABC";
    printf("str1[0] = %s\n", str1[0]);
    //指针指向的值是不能修改的
    // strcpy(str1[0], "BBB"); //编译没报错，运行是报错 EXC_BAD_ACCESS (code=2, address=0x100001ec7)
    printf("str1[0] = %s\n", str1[0]);

    const char *p = "abc"; // 到常量的指针
    char const *p1;        // 到常量的指针
    p = p1;                // 指针本身可以修改
    // strcpy(p, "aaa");             // 错误，指针所指向的对象不可以修改 EXC_BAD_ACCESS (code=1, address=0x0)
    const char *const p2 = "abc"; // 指常量针，指针本身不可以修改 p2 is a const pointer to const char
}

struct Books
{
    char title[100];
    char author[10];
    int book_id;
} book = {"c语言基础", "zero", 1};

void test06()
{
    printf("book.title = %s\n", book.title);
    struct Books book1;
    struct Author
    {
        // 基本类型按照16个字节对齐，数组的长度就是数组的长度。
        char * name;
        char gender;
    } author = { "zero", 1};

    printf("author.name = %s\n", author.name);
    printf("sizeof(Author) = %lu\n", sizeof(author)); // 16
    
    printf("book1.title = %s\n", book1.title);
    strcpy(book1.title, "c programming");
    printf("book1.title = %s\n", book1.title);
    strcpy(book1.author, "av");
    book1.book_id = 2;
    printf("sizeof(book1)=%d\n", sizeof(book1)); // 116
    struct Books *p = &book1;
    printf("book1.title = %s\n", book1.title);
    printf("book1.author = %s\n", book1.author);
    printf("book1.book_id = %d\n", book1.book_id);

    printf("p->title = %s\n", p->title);
    printf("p->author = %s\n", p->author);
    printf("p->book_id = %d\n", p->book_id);

    printf("p->title = %s\n", (*p).title);
    printf("p->author = %s\n", (*p).author);
    printf("p->book_id = %d\n", (*p).book_id);
}

union Data
{
    int i;
    float f;
    char str[20];
};

void test07()
{
    union Data data;
    data.i = 10;
    data.f = 3333.2;
    strcpy(data.str, "cccccc");

    printf("sizeof(data) = %d\n", sizeof(data));
    printf("data.i = %d\n", data.i);
    printf("data.f = %d\n", data.f);
    printf("data.str = %s\n", data.str);
}

int main()
{
    // test01();
    // test02();
    // test03();
    // test04();
    // test05();
    // test06();
    test07();
    return 0;
}
