#include <stdio.h>

int main()
{
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

    return 0;
}
