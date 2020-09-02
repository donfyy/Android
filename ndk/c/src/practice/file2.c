#include <stdio.h>

int main1()
{
    FILE *pr = fopen("./test/a.txt", "r");
    if (pr == NULL)
        return 0;
    FILE *pw = fopen("./test/b.txt", "w");
    if (pw)
    {

        while (1)
        {
            char c = fgetc(pr);
            if (c == EOF)
                break;
            c++;
            fputc(c, pw);
        }

        fclose(pw);
        pw = NULL;
    }
    fclose(pr);

    return 0;
}

int main2()
{
    FILE *pr = fopen("./test/b.txt", "r");
    if (pr == NULL)
        return 0;
    FILE *pw = fopen("./test/c.txt", "w");
    if (pw)
    {

        while (1)
        {
            char c = fgetc(pr);
            if (c == EOF)
                break;
            c--;
            fputc(c, pw);
        }

        fclose(pw);
        pw = NULL;
    }
    fclose(pr);

    return 0;
}

int main(int argc, char **argv)
{
    printf("加密解码，第三个 0代表加密，1代表解码\n");
    if (argc < 4)
        return 0;
    FILE *pr = fopen(argv[1], "r");
    if (pr == NULL)
        return 0;
    FILE *pw = fopen(argv[2], "w");
    if (pw)
    {
        char key = argv[3][0];
        printf("key = %c\n", key);
        char c = fgetc(pr);
        while (c != EOF)
        {
            if (key == '0')
                c++;
            else
                c--;
            putc(c, pw);
            c = fgetc(pr);
        }

        fclose(pw);
        pw = NULL;
    }

    fclose(pr);
    return 0;
}
