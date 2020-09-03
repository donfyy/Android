#include <stdio.h>

// 定义常量
#define ONE 1;       // 编译期常量
const int C_ONE = 1; // const描述不变化的值，并没有指出常量应该如何分配
#define dprint(a) printf(#a " = %g\n", a)

int main()
{
    double d = 1;
    dprint(d);
    printf("""""" " a"  "bcde\n");
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