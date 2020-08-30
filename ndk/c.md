## 例1
```c
     printf("===========\n");
     printf("%lu\n",sizeof(char));           // 1
     printf("%lu\n",sizeof(short));          // 2
     printf("%lu\n",sizeof(int));            // 4
     printf("%lu\n",sizeof(long));           // 8
     printf("%lu\n",sizeof(long long));      // 8
     printf("%lu\n",sizeof(float));          // 4
     printf("%lu\n",sizeof(double));         // 8
     printf("%lu\n",sizeof(long double));    // 16
     printf("===========\n");
     printf("%d\n",-1);
     printf("%u\n",-1);

     char c = 128;
     printf("d = %d\n",c);               // -128
     printf("hhd = %hhd\n",c);           // -128
     printf("hd = %hd\n",c);             // -128
     printf("hu = %hu\n",c);             // 65408 0xff80
     printf("hu = %hu\n",(char)127);     // 127
     printf("hu = %hu\n", (char)129);    // 65409 0xff81
```

## 例2

```c
// 定义常量
#define  ONE 1;         // 编译期常量
const int C_ONE = 1;    // const描述不变化的值，并没有指出常量应该如何分配

int main() {
    // 用goto跳出嵌套循环
    int break_from_inner_loop = 0;
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            printf("i = %d, j = %d\n", i, j);
            if (i == 1 && j == 1) {
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

