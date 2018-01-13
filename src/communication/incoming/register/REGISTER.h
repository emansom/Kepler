#include "communication/messages/incoming_message.h"
#include "communication/messages/outgoing_message.h"

void REGISTER(player *player, incoming_message *message) {
    /*
     @k
     @B
     @Galex123

     @D@Y1000118001270012900121001

     @E
     @A
     M

     @F
     @@
     @G
     @K

     dedfe@c.com@H@J06.01.1998@JA@A@@IA@C@Iqwerty123
    */
    im_read_b64_int(message);
    char *name = im_read_str(message);

    im_read_b64_int(message);
    char *figure = im_read_str(message);

    im_read_b64_int(message);
    char *gender = im_read_str(message);

    im_read_b64_int(message);
    im_read_b64_int(message);
    im_read_b64_int(message);

    char *mail = im_read_str(message);
    char *birth;
    char *password;



    printf("username: %s, figure: %s, mail: %s, birth: %s", name, figure, mail, birth);

    free(name);
    free(figure);
    free(gender);
    free(mail);
    /*free(birth);
    free(password);*/
}
