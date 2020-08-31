## 例1：复杂声明

```c
		
		int *f();             // f是一个函数，他返回到int的指针
    int(*pf);             // pf是一个到函数的指针，该函数返回一个int类型的对象
    char **argv;          // argv: pointer to pointer to char
    int(*pdaytab)[13];    // pdaytab: pointer to array[13] of int
    int *daytab[13];      // daytab: array[13] of pointer to int
    void *comp();         // comp: function returning pointer to void
    void (*pcomp)();      // pointer to function returning void
    char (*(*x())[])();   // x: function returning pointer to array[] of pointer to function returning char
    char(*(*x1[3])())[5]; // x1: array[3] of pointer to function returning pointer to array[5] of char

```



## 例2：sizeof运算符

```c
    printf("===========\n");
    printf("%lu\n", sizeof(char));        // 1
    printf("%lu\n", sizeof(short));       // 2
    printf("%lu\n", sizeof(int));         // 4
    printf("%lu\n", sizeof(long));        // 8
    printf("%lu\n", sizeof(long long));   // 8
    printf("%lu\n", sizeof(float));       // 4
    printf("%lu\n", sizeof(double));      // 8
    printf("%lu\n", sizeof(long double)); // 16
    printf("===========\n");
    printf("%d\n", -1);
    printf("%u\n", -1);

    char c = 128;
    printf("d = %d\n", c);           // -128
    printf("hhd = %hhd\n", c);       // -128
    printf("hd = %hd\n", c);         // -128
    printf("hu = %hu\n", c);         // 65408 0xff80
    printf("hu = %hu\n", (char)127); // 127
    printf("hu = %hu\n", (char)129); // 65409 0xff81

```

## 例3：用goto跳出嵌套循环

```c
// 定义常量
#define ONE 1;       // 编译期常量
const int C_ONE = 1; // const描述不变化的值，并没有指出常量应该如何分配

int main()
{
    // 用goto跳出嵌套循环
    int break_from_inner_loop = 0;
    for (int i = 0; i < 5; i++)
    {
        for (int j = 0; j < 5; j++)
        {
            printf("i = %d, j = %d\n", i, j);
            if (i == 1 && j == 1)
            {
                break_from_inner_loop = 1;
                goto found;
            }
        }
    }
found:
    printf("end\n");

    return 0;
}
```



## 例4：内存管理

```c
    int *ptr_i = malloc(sizeof(int)); //使用malloc函数在堆上分配了一块内存给ptr_i
    printf("ptr_i自己的地址=%p\n", &ptr_i);
    printf("ptr_i=%p\n", ptr_i); //这里是指针所指向变量的内存地址，可以指向任何地址
    *ptr_i = 1;
    free(ptr_i);
    //prt_i 悬空指针，野指针 是一样的危险，必须避免
    ptr_i = NULL; //0地址，一般不能被访问
    printf("value of ptr_i: %p\n", ptr_i);
    // *ptr_i = 2; // 访问0地址抛出该异常，EXC_BAD_ACCESS
    // printf("value of *ptr_i: %d\n", *ptr_i);  // 1
    // *ptr_i = 2; // 释放指针所指向的堆内存后，仍可以对该堆内存做修改，所以释放内存后，需要将指针置为NULL
    // printf("value of *ptr_i: %d\n", *ptr_i);  // 2
```



## 例5：指针与数组

```c
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
```

## 例6：以指针访问数组

```c 
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
```

## 例7：二维数组遍历

```c

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
```

## 例8:指针与二维数组

```c
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
```

## 例9:函数指针

```c
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
```



# GCC编译C/C++的四个过程

### 预处理(导入头文件，替换宏定义)
`gcc -E main.c -o main.i`
### 编译阶段(翻译至汇编语言)
`gcc -S main.i -o main.s`
### 汇编阶段(翻译至二进制指令)
`gcc -c main.s -o main.o`
### 链接阶段(填充可执行文件内部声明但是没有定义的函数地址)
`gcc main.o -o main`


# GCC 使用

指定格式	说明
--sysroot=XX	使用xx作为这一次编译的头文件与库文件的查找目录，查找下面的 usr/include usr/lib目录，--sysroot即可指定头文件又可指定库文件
-isysroot XX	指定头文件查找目录,覆盖--sysroot ，查找 XX/usr/include目录下头文件
-isystem XX	指定头文件查找路径（直接查找根目录）
-IXX	头文件查找目录，I是大写的

window10 x86_64 -> android linux arm 

arm-linux-androideabi-gcc.exe -o F:\CWork\demo01\libmain F:\CWork\demo01\main.c

arm-linux-androideabi-gcc.exe -isystem D:\Android\android-ndk-r17c\sysroot\usr\include -o F:\CWork\demo01\libmain F:\CWork\demo01\main.c

arm-linux-androideabi-gcc.exe -isystem D:\Android\android-ndk-r17c\sysroot\usr\include\arm-linux-androideabi -isystem D:\Android\android-ndk-r17c\sysroot\usr\include -o F:\CWork\demo01\libmain F:\CWork\demo01\main.c

arm-linux-androideabi-gcc.exe --sysroot=D:\Android\android-ndk-r17c\platforms\android-22\arch-arm -lc -isystem D:\Android\android-ndk-r17c\sysroot\usr\include -isystem D:\Android\android-ndk-r17c\sysroot\usr\include\arm-linux-androideabi -o F:\CWork\demo01\libmain F:\CWork\demo01\main.c


arm-linux-androideabi-gcc.exe --sysroot=D:\Android\android-ndk-r17c\platforms\android-22\arch-arm -lc -isystem D:\Android\android-ndk-r17c\sysroot\usr\include -isystem D:\Android\android-ndk-r17c\sysroot\usr\include\arm-linux-androideabi -pie -o F:\CWork\demo01\libmain F:\CWork\demo01\main.c


arm-linux-androideabi-gcc.exe --sysroot=D:\Android\android-ndk-r17c\platforms\android-22\arch-arm -lc -isystem D:\Android\android-ndk-r17c\sysroot\usr\include -isystem D:\Android\android-ndk-r17c\sysroot\usr\include\arm-linux-androideabi -fPIC -shared -o F:\CWork\demo01\libmain.so F:\CWork\demo01\main.c



假定我們的计算机的字长8位 1字节 = 8bit

为了表示负数，将最高位解释为符号位
正数的原码、反码、补码均相同
对于负数，已知原码求反码，符号位不变，其它位按位求反
对于负数，已知原码求补码，先求反码，再在反码末位加1


10 的二进制 原码  0000 1010  反码、补码
-10的二进制 原码  1000 1010  = -10
            反码  1111 0101  = 
			     +        1
			
            补码  1111 0110

在计算机里面 数值  一律用补码来表示(存储)
	         符号位和其他的位统一处理   减法 也可以按照加法来处理

5 - 10 = -5 =》 5 + (-10)

  0000 0101
+ 1111 0110
= 1111 1011 

5  补码          0000 0101

-5     	原码	 1000 0101	 
        反码     1111 1010
        补码     1111 1011
		
补码 = 反码 + 1   

0   补码  0000 0000
1   补码  0000 0001 
-1  补码  1111 1111

1111 1111 1111 1111 1111 1111 1111 1111

128 补码

127 
-128  

