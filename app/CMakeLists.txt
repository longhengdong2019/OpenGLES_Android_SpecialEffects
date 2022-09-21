
cmake_minimum_required(VERSION 3.10.2)

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#判断编译器类型,如果是gcc编译器,则在编译选项中加入c++11支持
if(CMAKE_COMPILER_IS_GNUCXX)
    set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
    message(STATUS "optional:-std=c++11")
endif(CMAKE_COMPILER_IS_GNUCXX)

#-----------------------------第三方begin-----------------------------
#需要引入我们头文件，以这个配置的目录为基准
include_directories(src/main/jniLibs/include)
#还需要定义第三方的头文件查找目录
include_directories(src/main/cpp/FaceAlignment/include)
#-----------------------------第三方end-----------------------------


# 添加依赖 opencv.so 库
set(distribution_DIR ${CMAKE_SOURCE_DIR}/../../../../src/main/jniLibs)

add_library(
        opencv_java4
        SHARED
        IMPORTED
)

set_target_properties(
        opencv_java4
        PROPERTIES IMPORTED_LOCATION
        ../../../../src/main/jniLibs/armeabi-v7a/libopencv_java4.so)


# Declares and names the project.
project("opencv")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/FaceTrack.cpp
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

#导入第三方库的CMakeList.txt
add_subdirectory(src/main/cpp/FaceAlignment)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib opencv_java4
                       # 加入依赖库
                       jnigraphics
                       android
                       #----------------------- 第三方库 -----------------------
                       seeta_fa_lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )


