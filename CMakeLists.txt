cmake_minimum_required(VERSION 3.5)
project(Kepler C)

set(EXECUTABLE_NAME Kepler)
set(CMAKE_C_STANDARD 11)
set(PROJECT_DIR src)

INCLUDE_DIRECTORIES(src)

# Incldude library directories
include_directories(${PROJECT_DIR}/lib/collections/)
include_directories(${PROJECT_DIR}/lib/cthreadpool/)
link_directories(/usr/local/lib/)

# Load source files for libraries
file(GLOB collections ${PROJECT_DIR}/lib/collections/*.c)
file(GLOB cthreadpool ${PROJECT_DIR}/lib/cthreadpool/*.c)

# Kepler classes
file(GLOB src ${PROJECT_DIR}/*.c)
file(GLOB util ${PROJECT_DIR}/util/*.c)
file(GLOB communication ${PROJECT_DIR}/communication/*.c)
file(GLOB messages ${PROJECT_DIR}/communication/messages/*.c)
file(GLOB server ${PROJECT_DIR}/server/*.c)
file(GLOB encoding ${PROJECT_DIR}/util/encoding/*.c)
file(GLOB database ${PROJECT_DIR}/database/*.c)
file(GLOB queries ${PROJECT_DIR}/database/queries/*.c)
file(GLOB pathfinder ${PROJECT_DIR}/game/pathfinder/*.c)

# Kepler managers
file(GLOB room_mapping ${PROJECT_DIR}/game/room/mapping/*.c)
file(GLOB players ${PROJECT_DIR}/game/player/*.c)
file(GLOB room ${PROJECT_DIR}/game/room/*.c)
file(GLOB room_pool ${PROJECT_DIR}/game/room/pool/*.c)
file(GLOB room_manager ${PROJECT_DIR}/game/room/manager/*.c)
file(GLOB navigator ${PROJECT_DIR}/game/navigator/*.c)
file(GLOB items ${PROJECT_DIR}/game/items/*.c)
file(GLOB messenger ${PROJECT_DIR}/game/messenger/*.c)
file(GLOB catalogue ${PROJECT_DIR}/game/catalogue/*.c)

# Tasks
file(GLOB tasks ${PROJECT_DIR}/game/room/tasks/*.c)

add_executable(${EXECUTABLE_NAME}
        ${collections}
        ${cthreadpool}
        ${src}
        ${util}
        ${messages}
        ${communication}
        ${server}
        ${encoding}
        ${database}
        ${queries}
        ${pathfinder}
        ${players}
        ${room}
        ${room_pool}
        ${navigator}
        ${items}
        ${tasks}
        ${room_mapping}
        ${room_manager}
        ${messenger}
        ${catalogue})

find_package (Threads)

target_link_libraries(${EXECUTABLE_NAME} ${CMAKE_THREAD_LIBS_INIT})
target_link_libraries(${EXECUTABLE_NAME} sqlite3)
target_link_libraries(${EXECUTABLE_NAME} uv)