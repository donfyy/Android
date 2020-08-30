#include <stdio.h>
#include<sys/stat.h>
#include <stdlib.h>

int main1() {
    //TODO: 文件拷贝
    printf("文件拷贝\n");
    FILE *pr = fopen("./test/a.txt", "r");
    if (pr == NULL) return 0;
    FILE *pw = fopen("./test/a1.txt", "w");
    if (pw) {
        while (1) {
            char c = fgetc(pr);
            if (c == EOF)
                break;
            fputc(c, pw);
        }

        fclose(pw);
        pw = NULL;
    }
    fclose(pr);

    return 0;
}

int main2(int argc, char **argv) {
    //TODO: mycopy 自己可以输入参数
    printf("mycopy 自己可以输入参数\n");

    if (argc < 3)return 0;
    FILE *pr = fopen(argv[1], "r");
    if (pr == NULL) return 0;
    FILE *pw = fopen(argv[2], "w");
    if (pw) {
        while (1) {
            char c = fgetc(pr);
            if (c == EOF)
                break;
            fputc(c, pw);
        }
        fclose(pw);
        pw = NULL;
    }
    fclose(pr);

    return 0;
}


#define   BLOCK_SIZE 1024*64 //64K

int main(int argc, char** argv){
    //TODO:
    printf("\n");
    if (argc < 3)return 0;
    FILE *pr = fopen(argv[1], "rb");
    if (pr == NULL) return 0;
    FILE *pw = fopen(argv[2], "wb");
    if(pw == NULL) return 0;
    struct stat st = {0};
    stat(argv[1],&st);
    int size = st.st_size;
    if(size > BLOCK_SIZE)
        size = BLOCK_SIZE;
    printf("size = %d\n", size);
    char *buf = calloc(1,size);
    unsigned int index = 0;
    while (!feof(pr)){
        int res = fread(buf,1,size,pr);
        fwrite(buf,1,res,pw);
        index++;
    }
    free(buf);
    fclose(pw);
    fclose(pr);
    printf("index=%ld\n",index);
    return 0;
}