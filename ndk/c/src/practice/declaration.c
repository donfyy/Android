int main()
{
    int *f();             // f是一个函数，他返回到int的指针
    int(*pf);             // pf是一个到函数的指针，该函数返回一个int类型的对象
    char **argv;          // argv: pointer to pointer to char
    int(*pdaytab)[13];    // pdaytab: pointer to array[13] of int
    int *daytab[13];      // daytab: array[13] of pointer to int
    void *comp();         // comp: function returning pointer to void
    void (*pcomp)();      // pointer to function returning void
    char (*(*x())[])();   // x: function returning pointer to array[] of pointer to function returning char
    char(*(*x1[3])())[5]; // x1: array[3] of pointer to function returning pointer to array[5] of char
    return 0;
}
