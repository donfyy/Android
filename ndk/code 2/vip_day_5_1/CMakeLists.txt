cmake_minimum_required(VERSION 3.6.1)

aux_source_directory(./src APP_SRC)

set(EXECUTABLE_OUTPUT_PATH ${PROJECT_BINARY_DIR})

add_executable(stringdemo ${APP_SRC})