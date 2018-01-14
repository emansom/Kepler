cmake_minimum_required(VERSION 3.5)
project(Kepler C)

set(EXECUTABLE_NAME Kepler)
set(CMAKE_C_STANDARD 11)
set(PROJECT_DIR src)
set(CMAKE_BUILD_TYPE Debug)

INCLUDE_DIRECTORIES(src)

# Incldude library directories
include_directories(${PROJECT_DIR}/lib/collections/)

if(WIN32)
    include_directories(${PROJECT_DIR}/lib/sqlite/)
else()
    link_directories(/usr/local/lib/)
endif()

# Load source files for libraries
file(GLOB collections ${PROJECT_DIR}/lib/collections/*.c)
file(GLOB dyad ${PROJECT_DIR}/lib/dyad/*.c)

# Kepler classes
file(GLOB src ${PROJECT_DIR}/*.c)
file(GLOB util ${PROJECT_DIR}/util/*.c)
file(GLOB communication ${PROJECT_DIR}/communication/*.c)
file(GLOB messages ${PROJECT_DIR}/communication/messages/*.c)
file(GLOB server ${PROJECT_DIR}/server/*.c)
file(GLOB encoding ${PROJECT_DIR}/util/encoding/*.c)
file(GLOB database ${PROJECT_DIR}/database/*.c)
file(GLOB queries ${PROJECT_DIR}/database/queries/*.c)

# Kepler managers
file(GLOB players ${PROJECT_DIR}/game/player/*.c)
file(GLOB room ${PROJECT_DIR}/game/room/*.c)

add_executable(${EXECUTABLE_NAME}
        ${collections}
        ${src}
        ${util}
        ${messages}
        ${communication}
        ${server}
        ${encoding}
        ${database}
        ${queries}
        ${players}
        ${room})

target_link_libraries(${EXECUTABLE_NAME} sqlite3)
target_link_libraries(${EXECUTABLE_NAME} uv)

if(WIN32)
    target_link_libraries(${EXECUTABLE_NAME} wsock32 ws2_32)
endif()