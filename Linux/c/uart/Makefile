# Compiler Flag

# Object link

OBJS = status.o uart.o

TARGET = uart_test

CFLAG = -std=c99

# Compiler
all: $(OBJS)
	$(CC) main.c $(CFLAGS) $(OBJS) -o $(TARGET)
	
%: %.o
	$(CC) -o $@ $^
	@$(NM) -n $@ | grep -v '\( [aUw] \)\|\(__crc_\)\|\( \$[adt]\)' > $@.map
	@$(STRIP) $@

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f $(TARGET) *.o *.bak *~
