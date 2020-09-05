#include <stdio.h>

void testShift();

int main(int argc, char const *argv[])
{
    testShift();
    return 0;
}

void testShift() {
    int a = 0x7FFFFFFF;
    int b = 0x7FFFFFFF;
    printf("a + b = %d (-2)\n", (a + b));                    // a + b = -2
    printf("((a + b) >> 1) = %d\n", (unsigned)(a + b) >> 1); // ((a + b) >> 1) = 2147483647
    printf("((a + b) >> 1) = %d\n", (a + b) >> 1);           // ((a + b) >> 1) = -1
}