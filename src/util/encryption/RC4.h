#ifndef RC4_H
#define RC4_H

typedef struct RC4_s {
    int key[256];
    int table[256];
    int i;
    int j;
} encryption;

typedef struct player_s player;

encryption *rc4_create(char *key);
void rc4_initialise(encryption *enc, int key);
int rc4_decode_key(char *key);
void rc4_premix_table(encryption *enc, char *key);
char *rc4_encipher(encryption *enc, char *input);
char *rc4_decipher(encryption *enc, char *input);

#endif