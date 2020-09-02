#include <stdio.h>
#include <string.h>

void test01()
{
    printf("文件读写\n");

    FILE *p = fopen("./test/a.txt", "w");
    if (p)
    {
        printf("success\n");
        fclose(p);
        p = NULL;
    }
    else
    {
        printf("fail\n");
    }
}

void test02()
{
    printf("字串读取fgetc\n");
    FILE *p = fopen("./test/a.txt", "r");
    if (p)
    {
        while (1)
        {
            char c = fgetc(p);
            if (c == EOF)
                break;
            printf("%c\n", c);
        }
        fclose(p);
        p = NULL;
    }
}

void dirtyWriter()
{
    char buf[100];
    memset(buf, 'a', sizeof(char));
}

int main()
{
    printf("写文件fputc\n");
    FILE *p = fopen("./test1/a.txt", "w");
    if (p)
    {
        while (1)
        {
            printf("please input： \n");
            const int n = 10;
            char buf[n] = {0};
            scanf("%s", buf);
            if (strcmp(buf, "exit") == 0)
                break;
            fputs(buf, p);
            fflush(p);
        }
        fclose(p);
        p = NULL;
    }
    return 0;
}
