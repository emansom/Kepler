cmake_minimum_required(VERSION 3.5)
project(Kepler C)

set(CMAKE_C_STANDARD 11)
set(PROJECT_DIR src)

INCLUDE_DIRECTORIES(src)

# Incldude library directories
include_directories(${PROJECT_DIR}/lib/collections/)
include_directories(${PROJECT_DIR}/lib/dyad/)
include_directories("C:/Program Files (x86)/MySQL/MySQL Connector C 6.1/include/")

# Link library .dlls on Windows
if(WIN32)
    link_directories("C:/Program Files (x86)/MySQL/MySQL Connector C 6.1/lib/")
endif()

# Load source files for libraries
file(GLOB collections ${PROJECT_DIR}/lib/collections/*.c)
file(GLOB dyad ${PROJECT_DIR}/lib/dyad/*.c)
file(GLOB sqlite ${PROJECT_DIR}/lib/sqlite/include/*.c)

# Kepler classes
file(GLOB src ${PROJECT_DIR}/*.c)
file(GLOB util ${PROJECT_DIR}/util/*.c)
file(GLOB communication ${PROJECT_DIR}/communication/*.c)
file(GLOB messages ${PROJECT_DIR}/communication/messages/*.c)
file(GLOB server ${PROJECT_DIR}/server/*.c)
file(GLOB encoding ${PROJECT_DIR}/util/encoding/*.c)
file(GLOB database ${PROJECT_DIR}/database/*.c)

# Kepler managers
file(GLOB players ${PROJECT_DIR}/game/player/*.c)

add_executable(Kepler
        ${collections}
        ${dyad}
        ${sqlite}
        ${src}
        ${util}
        ${messages}
        ${communication}
        ${server}
        ${players}
        ${encoding}
        ${database})

target_link_libraries(Kepler mysql)

if(WIN32)
    target_link_libraries(Kepler wsock32 ws2_32)
endif()