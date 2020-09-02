#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int calc(int a, char b, int c)
{
    switch (b)
    {
    case '+':
        return a + c;
    case '-':
        return a - c;
    case '*':
        return a * c;
    case '/':
        if (c != 0)
            return a / c;
    }

    return 0;
}

#define ROW_SIZE 10
#define BUF_SIZE 100

void test01()
{
    FILE *p = fopen("./test1/a.txt", "r");
    if (p)
    {
        printf("==============================\n");
        char *memo = calloc(0, sizeof(char));
        char *tmp = memo; //当前的位置
        int rowCount = 0;
        char buf[BUF_SIZE];
        while (feof(p) == 0)
        {
            memset(buf, 0, sizeof(char) * BUF_SIZE);
            fgets(buf, sizeof(buf), p);
            int a = 0;
            char b = 0;
            int c = 0;
            int fillCount = sscanf(buf, "%d%c%d=", &a, &b, &c);
            printf("fillCount = %d\n", fillCount);
            if (fillCount != 3)
                continue;
            printf("rowIdx = %d, s = %s", rowCount, buf);
            memo = realloc(memo, ROW_SIZE * (++rowCount) * sizeof(char));
            sprintf(tmp, "%d%c%d=%d\n", a, b, c, calc(a, b, c));
            tmp += ROW_SIZE;
        }
        fclose(p);
        printf("rowCount = %d\n", rowCount);
        printf("==============================\n");
        p = fopen("./test1/a.txt", "w");
        int i;
        tmp = memo;
        for (i = 0; i < rowCount; i++)
        {
            printf("%s", tmp);
            fputs(tmp, p); // 从tmp中读取字符并写入到p中直至读到NULL
            tmp += ROW_SIZE;
        }
        free(memo);
        memo = NULL;
        tmp = NULL;
        fclose(p);
        p = NULL;
    }
}

void testFputs()
{
    FILE *p = fopen("./a.txt", "a");
    // fputs("abc", p);
    char str[10];
    memset(str, 0, 10 * sizeof(char));
    strcpy(str, "abc");
    fputs(str, p);
    fclose(p);
    p = NULL;
}

int main()
{
    test01();
    return 0;
}