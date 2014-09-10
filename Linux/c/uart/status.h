#ifndef _STATUS_H_
#define _STATUS_H_
/*
 *	status > 0 : information
 *  status = 0 : success
 *  status < 0 : error
 *  000000 
 */

int debug(int status, char *file, int line);
#define SUCCESS				0
#define DEBUG(status) debug(status, __FILE__,__LINE__)


 
// uart_status
#define ERR_BASE_UART(X)		-1000+X

#endif

