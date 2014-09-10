#ifndef _UART_H_
#define _UART_H_

typedef enum _PARITY {
	P_8N1,	/* No parity (8N1)	*/
	P_7E1,	/* Even parity (7E1)*/
	P_7O1,	/* Odd parity (7O1)	*/
	P_7S1	/* Space parity is setup the same as no parity (7S1)	*/
}PARITY;	

typedef enum _BAUDRATE {
	BR2400,
	BR4800,
	BR9600,
	BR19200,
	BR38400,
	BR57600,
	BR115200
} BAUDRATE;

int	openComPort(char *comPort);
int closeComPort(int fd);
int writeByte(int fd, unsigned char byte);
int writeBytes(int fd, char *bytes, int len);
int readByte(int fd,char *byte);
int readBytes(int fd,char *bytes, int len);
int setBaudRate(struct termios *options,BAUDRATE baud_rate);
int setParity(int fd,struct termios *options,PARITY parity);

#endif