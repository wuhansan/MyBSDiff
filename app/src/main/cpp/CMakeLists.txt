# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
# 查找指定目录的.c文件 并将其设置为变量 bzip_source  新版本CMAKEList.txt 在cpp目录下
file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

# 设置本地动态库 编译生成动态库
add_library( # Sets the name of the library.
        native-lib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        native-lib.cpp
        bspatch.c
        ${bzip_source})

# 查找·系统库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

#需要链接或者编译的库
target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})