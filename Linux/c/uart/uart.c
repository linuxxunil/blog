#include <stdio.h>
#include <stdlib.h>
#include <string.h>  /* String function definitions */
#include <unistd.h>  /* UNIX standard function definitions */
#include <fcntl.h>   /* File control definitions */
#include <errno.h>   /* Error number definitions */
#include <termios.h> /* POSIX terminal control definitions */
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>

#include "uart.h"
#include "status.h"
#include "uart_status.h"


int	openComPort(char *comPort)
{
	int fd; /* File descriptor for the port */

	/*	O_NOCTTY = doesn't want to be the "controlling terminal" */	
	fd = open(comPort, O_RDWR | O_NOCTTY | O_NDELAY);
	
	if ( fd < 0 ) {
		return DEBUG(ERR_OPEN_COMPORT_FAILURE);
	} else {
		fcntl(fd, F_SETFL, 0);
	}
	
    return (fd);
}

int closeComPort(int fd)
{
	if(close(fd)) {
		return DEBUG(ERR_CLOSE_COMPORT_FAILURE);
	}
	return SUCCESS;
}

int writeByte(int fd, unsigned char byte) 
{
	int n = write(fd, &byte, 1);
	return (n < 0) ? DEBUG(ERR_WRITE_BYTE_FAILURE) : n;
}

int writeBytes(int fd, char *bytes, int len) 
{
	int n = write(fd, bytes, len);
	return (n < 0) ? DEBUG(ERR_WRITE_BYTE_FAILURE) : n;
}

int readByte(int fd,char *byte)
{
	int n = read(fd, &byte[0], 1 );
	return (n < 0) ? DEBUG(ERR_WRITE_BYTE_FAILURE) : n; 
}

int readBytes(int fd,char *byte,int len)
{
	int n = read(fd, byte, len);
	return (n < 0) ? DEBUG(ERR_WRITE_BYTE_FAILURE) : n; 
}

int setBaudRate(struct termios *options,BAUDRATE baud_rate)
{
	/* SET BOUD RATE */
	switch (baud_rate)
	{
	case BR2400: 
		cfsetispeed(options,B2400);
		cfsetospeed(options,B2400);
		break;
	case BR4800:
		cfsetispeed(options,B4800);
		cfsetospeed(options,B4800);
		break;
	case BR9600:
		cfsetispeed(options,B9600);
		cfsetospeed(options,B9600);
		break;
	case BR19200:	
		cfsetispeed(options,B19200);
		cfsetospeed(options,B19200);
		break;
	case BR38400:	
		cfsetispeed(options,B38400);
		cfsetospeed(options,B38400);
		break;
	case BR57600:	
		cfsetispeed(options,B57600);
		cfsetospeed(options,B57600);
		break;
	case BR115200:	
		cfsetispeed(options,B115200);	
		cfsetospeed(options,B115200);
		break;
	default:
		return DEBUG(ERR_BAUDRATE_NOT_FOUND);
	}
	
	return SUCCESS;
}

int setParity(int fd,struct termios *options,PARITY parity)
{

	/*	Enable the receiver and set local mode...	*/
    options->c_cflag |= (CLOCAL | CREAD);
	
	/*	Disable	hardware flow	*/ 	
	options->c_cflag &= ~CRTSCTS;
	
	/*	Disable	software flow	*/
    options->c_iflag &= ~(IXON | IXOFF | IXANY); 
	
	/*	Enable non-canonical	*/
	options->c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
	
	options->c_oflag &= ~OPOST;	
	
	/*	Minimum number of characters to read	*/
	//options->c_cc[VTIME] = 0;	
	
	/*	Time to wait for data	*/
	//options->c_cc[VMIN] = 1;		 

	switch (parity){
	case P_8N1:
	/*	No parity (8N1)	*/
      	options->c_cflag &= ~PARENB;
      	options->c_cflag &= ~CSTOPB;
      	options->c_cflag &= ~CSIZE;
      	options->c_cflag |= CS8;
		break;
	case P_7E1:
    /*	Even parity (7E1)	*/
      	options->c_cflag |= PARENB;
      	options->c_cflag &= ~PARODD;
      	options->c_cflag &= ~CSTOPB;
      	options->c_cflag &= ~CSIZE;
      	options->c_cflag |= CS7;
		break;
	case P_7O1:
    /* Odd parity (7O1)	*/
      	options->c_cflag |= PARENB;
      	options->c_cflag |= PARODD;
     	options->c_cflag &= ~CSTOPB;
     	options->c_cflag &= ~CSIZE;
     	options->c_cflag |= CS7;
		break;
	case P_7S1:
    /* Space parity is setup the same as no parity (7S1)	*/
      	options->c_cflag &= ~PARENB;
      	options->c_cflag &= ~CSTOPB;
      	options->c_cflag &= ~CSIZE;
      	options->c_cflag |= CS8;
		break;
	default:
		return DEBUG(ERR_PARITY_NOT_FOUND);
	}

	/*	Set the new options for the port...	*/
    tcsetattr(fd, TCSANOW, options);

	return SUCCESS;
}
