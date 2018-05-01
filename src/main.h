#include <stdbool.h>

#ifndef MAIN_H
#define MAIN_H

#define COMMAND_INPUT_LENGTH 200

int main(void);
bool handle_command(char *command);
void exit_program();
void dispose_program();

#endif