# NDK概览

### JNI

定义：Java Native Interface，Java本地接口

作用：使得Java 与 本地其他类型语言（如C、C++）交互

JNI是 Java 调用 Native 语言的一种特性

JNI 是属于 Java 的，与 Android 无直接关系

实际中的驱动都是C/C++开发的,通过JNI,Java可以调用C/c++实现的驱动，从而扩展Java虚拟机的能力。
另外，在高效率的数学运算、游戏的实时渲染、音视频的编码和解码等方面，一般都是用C开发的

### 为什么要有 JNI

- 实际使用中，Java 需要与 本地代码 进行交互
- 因为 Java 具备跨平台的特点，所以Java 与 本地代码交互的能力非常弱
- 采用 JNI特性 增强 Java 与 本地代码交互的能力

### 实现步骤 面试考点

1. 在Java中声明Native方法（即需要调用的本地方法）
2. 编译上述Java源文件javac（得到 .class文件）
3. 通过 javah 命令导出JNI的头文件（.h文件）
4. 使用 Java需要交互的本地代码 实现在 Java中声明的Native方法
5. 编译.so库文件
6. 通过Java命令执行Java程序，最终实现Java调用本地代码

### NDK(C/C++) SDK(Java)性质是一样的

定义：Native Development Kit，是 Android的一个工具开发包

作用：快速开发C、 C++的动态库，并自动将so和应用一起打包成 APK

在Android的场景下 使用JNI

提供了把.so和.apk打包的工具

NDK提供的库有限，仅拥有算法效率和敏感的问题

提供了交叉编译器，用于生成特定的CPU平台动态库

#### 特点

- 运行效率高
- 代码安全性高
- 功能拓展性好
- 易于代码复用和移植

### NDK与JNI关系

- JNI是实现Java调用C/C++的途径，NDK是Android中实现JNI的工具包
- 即在Android的开发环境中，通过NDK从而实现JNI的功能
- Java的优点是跨平台，和操作系统之间的调用由JVM完成，但是一些和操作系统相关的操作就无法完成
，JNI的出现刚好弥补了这个缺陷，也完善了Java语言，将java扩展得更为强大

### 常用C/C++编译器

#### clang 

是一个C、C++、Object-C的轻量级编译器。基于LLVM（LLVM是以C++编写而成的构架编译器的框架系统，可以说是一个用于开发编译器相关的库

#### gcc

GNU C编译器。原本只能处理C语言，很快扩展，变得可处理C++

#### g++

GNU c++编译器，后缀为.c的源文件，gcc把它当作是C程序，而g++当作是C++程序；
后缀为.cpp的，两者都会认为是c++程序，g++会自动链接c++标准库stl，gcc不会，gcc不会定义__cplusplus宏，而g++会

#### GDB

是一个由GNU开源组织发布的、UNIX/Linux操作系统下的、基于命令行的、功能强大的程序调试工具

### C/C++文件编译过程 面试点

1. 预处理: 预处理阶段主要处理include和define等。
它把#include包含进来的.h 文件插入到#include所在的位置，
把源程序中使用到的用#define定义的宏用实际的字符串代替
2. 编译: 编译阶段，编译器检查代码的规范性、语法错误等，检查无误后，编译器把代码翻译成汇编语言
3. 汇编: 汇编阶段把 .s文件翻译成二进制机器指令文件.o,这个阶段接收.c, .i, .s的文件都没有问题
4. 链接: 链接阶段，链接的是其余的函数库，比如我们自己编写的c/c++文件中用到了三方的函数库，
在连接阶段就需要连接三方函数库，如果连接不到就会报错

### 静态库（static library .a）

通常情况下，对函数库的链接是放在编译时期（compile time）完成的。
所有相关的对象文件（object file）与牵涉到的函数库（library）被链接合成一个可执行文件（executable file）。
程序在运行时，与函数库再无瓜葛，因为所有需要的函数已拷贝到自己包下。
所以这些函数库被称为静态库（static library），通常文件名为“library.a”的形式 
**Android可以使用静态库**

### 动态链接库（dynamic link library .so）

- 把对一些库函数的链接载入推迟到运行时（runtime）
- 可以实现进程之间的资源共享
- 将一些程序升级变得简单
- 甚至可以真正坐到链接载入完全由程序员在程序代码中控制

### 什么是交叉编译

#### 本地编译

在当前编译平台下，编译出来的程序只能放到当前平台下运行，比如，我们在 x86 平台上，编写程序并编译成可执行程序。
这种方式下，我们使用 x86 平台上的工具，开发针对 x86 平台本身的可执行程序，这个编译过程称为本地编译

#### 交叉编译

在当前编译平台下，编译出来的程序能运行在体系结构不同的另一种目标平台上，但是编译平台本身却不能运行该程序，
比如，我们在 x86 平台上，编写程序并编译成能运行在 ARM 平台的程序，编译得到的程序在 x86 平台上是不能运行的，必须放到 ARM 平台上才能运行

### 什么是交叉编译链

严格意义上来说，交叉编译器，只是指交叉编译的gcc，但是实际上为了方便，我们常说的交叉编译器就是交叉工具链

### 交叉编译链的命名规则

arch-core-kernel-system
- arch： 用于哪个目标平台。
- core： 使用的是哪个CPU Core，如Cortex A8，但是这一组命名好像比较灵活，
在其它厂家提供的交叉编译链中，有以厂家名称命名的，也有以开发板命名的，或者直接是none或cross的。
- kernel： 所运行的OS，见过的有Linux，uclinux，bare（无OS）。
- systen：交叉编译链所选择的库函数和目标映像的规范，如gnu，gnueabi等。其中gnu等价于glibc+oabi；gnueabi等价于glibc+eabi

例如：
arm-none-linux-gnueabi-gcc
arm-cortex_a8-linux-gnueabi-gcc
mips-malta-linux-gnu-gcc

### 参考链接

- [Android ABI](https://developer.android.com/ndk/guides/abis?hl=zh-cn)
- [CMAKE手册](https://www.zybuluo.com/khan-lau/note/254724)
- [Makefile 语法详解](https://quanzhuo.github.io/2016/06/06/Makefile/)