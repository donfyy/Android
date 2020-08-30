#include <stdio.h>
#include <string.h>

int main1() {
    //TODO: 文件读写
    printf("文件读写\n");

    FILE *p = fopen("./test/a.txt", "w");
//    FILE *p = fopen("D:\\enjoy01\\source\\c_study\\vip_day_6_1\\src\\test\\a.txt", "w");
    if (p) {
        printf("success\n");
        fclose(p);
        p = NULL;
    } else {
        printf("fail\n");
    }


    return 0;
}

int main2() {
    //TODO: 字串读去fgetc
    printf("字串读去fgetc\n");

    FILE *p = fopen("./test/a.txt", "r");
    if (p) {
        while (1) {
            char c = fgetc(p);
            if (c == EOF)
                break;
            printf("%c\n", c);
        }

        fclose(p);
        p = NULL;
    }


    return 0;
}

int main() {
    //TODO: 写文件fputc
    printf("写文件fputc\n");

    FILE *p = fopen("./test1/a.txt", "w");
    if (p) {

        while(1){
            printf("please input： \n");
            char buf[100] = {0};
            scanf("%s",buf);
            if(strcmp(buf,"exit") == 0)
                break;
            fputs(buf,p);
            fflush(p);
        }
        fclose(p);
        p = NULL;
    }

    return 0;
}

