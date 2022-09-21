
#主工程已经指定cmake版本为3.10.2 所以去掉
#cmake_minimum_required(VERSION 2.8.4)

#生成的目录也不用，因为我们已经指明了
#project(seeta_fa_lib)

#我们不需要编译示例程序 把option 的 BUILD_EXAMPLES 定义成ON，则打开这个开关，
#这行注释后，下面的 if(BUILD_EXAMPLES) 也可以去掉了
#option(BUILD_EXAMPLES  "Set to ON to build examples"  ON)

# Use C++11
#set(CMAKE_CXX_STANDARD 11)
#set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
message(STATUS "C++11 support has been enabled by default.")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -msse4.1")

include_directories(include)

set(src_files 
    src/cfan.cpp
    src/face_alignment.cpp
    src/sift.cpp
    )

#习惯编译成一个静态库
add_library(seeta_fa_lib STATIC ${src_files})
#add_library(seeta_fa_lib SHARED ${src_files})

#定义变量也可以把它删除掉
#set(fa_required_libs seeta_fa_lib)

#if (BUILD_EXAMPLES)
#    message(STATUS "Build with examples.")
#    find_package(OpenCV)
#    if (NOT OpenCV_FOUND)
#        message(WARNING "OpenCV not found. Test will not be built.")
#    else()
#        include_directories(${OpenCV_INCLUDE_DIRS} build)
#	link_directories(build)
#        list(APPEND fa_required_libs ${OpenCV_LIBS} seeta_facedet_lib)
#        add_executable(fa_test src/test/face_alignment_test.cpp)
#        target_link_libraries(fa_test ${fa_required_libs})
#    endif()
#endif()
