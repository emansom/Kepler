#include "uv.h"

void server_on_new_connection(uv_stream_t *server, int status);
void server_on_read(uv_stream_t *handle, ssize_t nread, const uv_buf_t *buf);
void server_alloc_buffer(uv_handle_t* handle, size_t  size, uv_buf_t* buf);
void server_on_connection_close(uv_handle_t *handle);
void server_on_write(uv_write_t* req, int status);
void start_server(const char *ip, int port);
