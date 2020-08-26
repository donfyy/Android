# GCC编译C/C++的四个过程
### 预处理
`gcc -E main.c -o main.i`
### 编译阶段
`gcc -S main.i -o main.s`
### 汇编阶段
`gcc -c main.s -o main.o`
### 链接阶段
`gcc main.o -o main.exe`


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

