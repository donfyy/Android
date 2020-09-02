#include <stdio.h>
#include <sys/stat.h>
#include <stdlib.h>

void test01()
{
    printf("文件拷贝\n");
    FILE *pr = fopen("./test/a.txt", "r");
    if (pr == NULL)
        return;
    FILE *pw = fopen("./test/a1.txt", "w");
    if (pw)
    {
        char c = fgetc(pr);
        while (c != EOF)
        {
            fputc(c, pw);
            c = fgetc(pr);
        }
        fclose(pw);
        pw = NULL;
    }
    fclose(pr);
    pr = NULL;
}

void test02(int argc, char **argv)
{
    printf("mycopy 自己可以输入参数\n");
    if (argc < 3)
        return;
    FILE *pr = fopen(argv[1], "r");
    if (pr == NULL)
        return;
    FILE *pw = fopen(argv[2], "w");
    if (pw)
    {
        char c = fgetc(pr);
        while (c != EOF)
        {
            fputc(c, pw);
            c = fgetc(pr);
        }
        fclose(pw);
        pw = NULL;
    }
    fclose(pr);
    pr = NULL;
}

#define BLOCK_SIZE 1024 * 64 // 64K
void test03(int argc, char **argv)
{
    if (argc < 3)
        return;
    FILE *pr = fopen(argv[1], "rb");
    if (pr == NULL)
        return;
    FILE *pw = fopen(argv[2], "wb");
    if (pw == NULL)
        return;
    struct stat st = {0};
    stat(argv[1], &st);
    const int buf_size = st.st_size > BLOCK_SIZE ? BLOCK_SIZE : st.st_size;
    printf("buf_size = %d\n", buf_size);
    char *buf = calloc(1, buf_size);
    int iterateCount = 0;
    while (!feof(pr))
    {
        int res = fread(buf, 1, buf_size, pr);
        fwrite(buf, 1, res, pw);
        iterateCount++;
    }
    printf("iterateCount=%d\n", iterateCount);
    free(buf);
    fclose(pw);
    fclose(pr);
    buf = NULL;
    pr = NULL;
    pw = NULL;
}

int main(int argc, char **argv)
{
    // test01();
    // test02(argc, argv);
    test03(argc, argv);
    return 0;
}