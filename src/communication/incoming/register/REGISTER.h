#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

#include "database/queries/player_query.h"

#include "shared.h"

/*
 * Arguments we will send the to the login thread
 */
typedef struct register_context_s {
    char username[255];
    char password[255];
    char figure[255];
    char gender[2];
    session *player;
} register_context;

/*
 * This function does the off-server-thread login
 */
void *do_register(void *args) {
    register_context *ctx = (register_context *)args;
    session *player = ctx->player;

    player_query_create(ctx->username, ctx->figure, ctx->gender, ctx->password);

    pthread_exit((void*) 0);
}

/*
 * Off-server-thread login, as the password hashing function blocks the server thread for too long
 *
 * @param username Login username
 * @param password Login password
 */
void async_register(char *username, char *figure, char* gender, char *password, session *player) {
    register_context *ctx = malloc(sizeof(register_context));
    strcpy(ctx->username, username);
    strcpy(ctx->password, password);
    strcpy(ctx->figure, figure);
    strcpy(ctx->gender, gender);
    ctx->player = player;

    pthread_t register_thread;
    pthread_attr_t attr;

    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);

    if (pthread_create(&register_thread, &attr, &do_register, (void*) ctx)) {
        log_fatal("Uh-oh! Could not create thread for async register");
    }
}

void REGISTER(session *player, incoming_message *message) {
    im_read_b64_int(message);
    char *name = im_read_str(message);

    im_read_b64_int(message);
    char *figure = im_read_str(message);

    im_read_b64_int(message);
    char *gender = im_read_str(message);

    im_read_b64_int(message);
    im_read_b64_int(message);

    // don't give a shit about emails
    im_read_b64_int(message);
    free(im_read_str(message));

    // couldn't give a shit about your birthday either
    im_read_b64_int(message);
    free(im_read_str(message));

    im_read(message, 11);
    char *password = im_read_str(message);

    if (name != NULL && figure != NULL && gender != NULL && password != NULL) {
        if (valid_password(name, password) == 0 && get_name_check_code(name) == 0) {
            async_register(name, figure, gender, password, player);
        }
    }

    free(name);
    free(figure);
    free(gender);
    free(password);
}
