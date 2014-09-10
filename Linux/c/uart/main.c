#include <stdio.h>
#include <termios.h>
#include <unistd.h>
#include <sys/select.h> 
#include <sys/time.h> 

#include "uart.h"
#include "status.h"

int initUART(char *dev)
{
	struct termios options;
	int status;
	int fd;
	
	if ( (fd = openComPort(dev)) < 0 )
		return fd;
	
	tcgetattr(fd , &options);	
	
	/*	set baund rate */
	if ( (status = setBaudRate( &options, BR115200)) < 0 )
		return status;
	
	/*	set parity	*/
	if ( (status = setParity(fd , &options, P_8N1)) < 0 )
		return status;
	
	return fd;
}

int main() 
{
	int fd;
	unsigned char byte[1];
	struct timeval timeout;
	fd_set readfds;
	
	
	if ( (fd = initUART("/dev/ttyAMA0")) < 0 ) {
		return -1;
	}
	
	timeout.tv_sec = 1;
	timeout.tv_usec = 0;
	
	while(1)
	{
		FD_ZERO(&readfds);
		FD_SET(fd,&readfds);
		
		if ( select(fd+1, &readfds,NULL, NULL, &timeout) < 0 )
			return -1;
			
		if(FD_ISSET(fd,&readfds)) {
			readByte(fd,&byte[0]);
			writeByte(fd,byte[0]);
		}
			
	}
	closeComPort(fd);
	return SUCCESS;
}