cmake_minimum_required(VERSION 3.9)
project(Kepler C)

set(CMAKE_C_STANDARD 11)
set(PROJECT_DIR src)

INCLUDE_DIRECTORIES(src)

# Collections-C
file(GLOB collections ${PROJECT_DIR}/lib/collections/*.c)
file(GLOB dyad ${PROJECT_DIR}/lib/dyad/*.c)

# Kepler classes
file(GLOB src ${PROJECT_DIR}/*.c)
file(GLOB util ${PROJECT_DIR}/util/*.c)
file(GLOB communication ${PROJECT_DIR}/communication/*.c)
file(GLOB messages ${PROJECT_DIR}/communication/messages/*.c)
file(GLOB server ${PROJECT_DIR}/server/*.c)
file(GLOB encoding ${PROJECT_DIR}/util/encoding/*.c)

# Kepler managers
file(GLOB players ${PROJECT_DIR}/game/player/*.c)

add_executable(Kepler
        ${collections}
        ${src}
        ${dyad}
        ${util}
        ${messages}
        ${communication}
        ${server}
        ${players}
        ${encoding})

if(WIN32)
    target_link_libraries(Kepler wsock32 ws2_32)
endif()