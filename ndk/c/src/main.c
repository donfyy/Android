#include <stdio.h>

extern void func(void);

int main(){
	printf("\n Inside main()\n");
	func();
}
