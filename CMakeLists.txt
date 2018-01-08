cmake_minimum_required(VERSION 3.9)
project(Kepler C)

set(CMAKE_C_STANDARD 11)
set(PROJECT_DIR src)

INCLUDE_DIRECTORIES(src)

file(GLOB src ${PROJECT_DIR}/*.c)
file(GLOB dyad ${PROJECT_DIR}/dyad/*.c)
file(GLOB server ${PROJECT_DIR}/server/*.c)

add_executable(Kepler
        ${src}
        ${dyad}
        ${server})

if(WIN32)
    target_link_libraries(Kepler wsock32 ws2_32)
endif()