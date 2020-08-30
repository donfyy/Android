#include <stdio.h>

#define  ONE 1;
const int C_ONE = 1;

int main() {
    /* 局部变量定义 */
    auto int a = 10;

    /* do 循环执行 */
    LOOP:
    do {
        if (a == 15) {
            /* 跳过迭代 */
            a = a + 1;
            goto LOOP;
        }
        printf("a 的值： %d\n", a);
        a++;

    } while (a < 20);

    return 0;
}