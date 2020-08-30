cmake_minimum_required(VERSION 3.6.1)

set(APP_SRC main.c)
#指定执行文件生成的目录
#set(EXECUTABLE_OUTPUT_PATH ${PROJECT_BINARY_DIR}/bin)
add_executable(main ${APP_SRC})

