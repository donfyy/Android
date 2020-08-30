#include <stdio.h>
#include <stdlib.h>

int calc(int a, char b, int c) {
    switch (b) {
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

int main() {
    //TODO:
    printf("\n");

    FILE *p = fopen("./test1/a.txt", "r");
    if (p) {
        printf("success\n");
        char *array =calloc(100,sizeof(char));
        char *tmp = array;//当前的位置
        int index = 0;
        while (1){
            char buf[100] = {0};
            if(feof(p))
                break;
            fgets(buf, sizeof(buf),p);

            int a = 0;
            char b = 0;
            int c = 0;
            sscanf(buf,"%d%c%d=",&a,&b,&c);
            sprintf(tmp,"%d%c%d=%d\n",a,b,c,calc(a,b,c));
            array = realloc(array,100 * (index +2));
            tmp = array +100*(index+1);
            index++;
        }
        fclose(p);
        //写回去
        p = fopen("./test1/a.txt","w");
        int i;
        tmp = array;
        for(i = 0; i < index; i++){
            fputs(tmp,p);
            tmp += 100;
        }
        free(tmp);
        free(array);
        fclose(p);
        p = NULL;
    }


    return 0;
}