cmake_minimum_required(VERSION 3.4.1)
project(edge_native)

# Update OpenCV_DIR if you include OpenCV Android SDK in the project.
# Example: set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/../opencv-android/sdk/native/jni")
find_package(OpenCV REQUIRED COMPONENTS core imgproc)

add_library(edge_native SHARED jni/edge_native.cpp)

include_directories(${OpenCV_INCLUDE_DIRS})

target_link_libraries(edge_native ${OpenCV_LIBS})

find_library(log-lib log)
target_link_libraries(edge_native ${log-lib})
