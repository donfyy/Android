#include <stdio.h>
#include <string.h>

void test(char *color[]) {
	/*sizeof on array function parameter will return size of
      'char **' instead of 'char *[]'*/
	printf("sizeof(color) in test: %lu\n", sizeof(color));
}

int main() {
	char *color[] = {"red", "blue", "green", "yellow"};
	int  n = 4;
	printf("=====================\n");
	printf("sizeof(color):%lu\n", sizeof(color)); // [] 4 * 8 = 32
	char **col1 = color;
	printf("sizeof(col1):%lu\n", sizeof(col1)); // * 8
	test(color);
	printf("=====================\n");
	printf("%p\n", color[0]);
	printf("sizeof(0):%lu\n", sizeof(color[0])); // 8
	printf("sizeof(1):%lu\n", sizeof(color[1])); // 8
	printf("sizeof(2):%lu\n", sizeof(color[2])); // 8
	printf("sizeof(char*):%lu\n", sizeof(char*)); // 8
	printf("strlen(\"red\"):%lu\n", strlen(color[0])); // 3
	printf("=====================\n");

	for (int i = 0; i < n; i++) {
		puts(color[i]);
		printf("color[%d]=%p\n", i, &color[i]);
		color[i] = "color";
		printf("color[%d]=%p\n", i, &color[i]);
	}

}

