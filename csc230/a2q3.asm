; a2q3.asm
;
; Write a main program that increments a counter when the buttons are pressed
;
; Use the subroutine you wrote in a2q2.asm to solve this problem.
;
;
; Definitions for PORTA and PORTL when using
; STS and LDS instructions (ie. memory mapped I/O)
;
.equ DDRB=0x24
.equ PORTB=0x25
.equ DDRL=0x10A
.equ PORTL=0x10B

;
; Definitions for using the Analog to Digital Conversion
.equ ADCSRA=0x7A
.equ ADMUX=0x7C
.equ ADCL=0x78
.equ ADCH=0x79


		; initialize the Analog to Digital conversion

		ldi r16, 0x87
		sts ADCSRA, r16
		ldi r16, 0x40
		sts ADMUX, r16

		; initialize PORTB and PORTL for ouput
		ldi	r16, 0xFF
		sts DDRB,r16
		sts DDRL,r16

; Your code here
; make sure your code is an infinite loop
		clr r0
start:
		call check_button
		cpi r24, 1
		breq inside
		rjmp start
outside:	
		call display
		rjmp start
inside:	
		inc r0
		ldi r20,0x10
		call delay
		rjmp outside

done:		jmp done		; if you get here, you're doing it wrong

;
; the function tests to see if the button
; UP or SELECT has been pressed
;
; on return, r24 is set to be: 0 if not pressed, 1 if pressed
;
; this function uses registers:
;	r16
;	r17
;	r24
;
; This function could be made much better.  Notice that the a2d
; returns a 2 byte value (actually 12 bits).
; 
; if you consider the word:
;	 value = (ADCH << 8) +  ADCL
; then:
;
; value > 0x3E8 - no button pressed
;
; Otherwise:
; value < 0x032 - right button pressed
; value < 0x0C3 - up button pressed
; value < 0x17C - down button pressed
; value < 0x22B - left button pressed
; value < 0x316 - select button pressed
;
; This function 'cheats' because I observed
; that ADCH is 0 when the right or up button is
; pressed, and non-zero otherwise.
; 
check_button:
		; start a2d
		lds	r16, ADCSRA	
		ori r16, 0x40
		sts	ADCSRA, r16

		; wait for it to complete
wait:	lds r16, ADCSRA
		andi r16, 0x40
		brne wait

		; read the value
		lds r16, ADCL
		lds r17, ADCH

		clr r24
		cpi r17, 3
		brlo set1
		cpi r17, 3
		breq check
		jmp skip		
set1:		
		ldi r24,1
skip:	ret

check:
		cpi r16, 0x16
		brlo set1
		jmp skip 
;
; delay
;
; set r20 before calling this function
; r20 = 0x40 is approximately 1 second delay
;
; this function uses registers:
;
;	r20
;	r21
;	r22
;
delay:	
del1:		nop
		ldi r21,0xFF
del2:		nop
		ldi r22, 0xFF
del3:		nop
		dec r22
		brne del3
		dec r21
		brne del2
		dec r20
		brne del1	
		ret

;
; display
;
; copy your display subroutine from a2q2.asm here
 
; display the value in r0 on the 6 bit LED strip
;
; registers used:
;	r0 - value to display
;	r17 - value to write to PORTL
;	r18 - value to write to PORTB
;
;   r16 - scratch
display:
		mov r16, r0
		ldi r17, 0x00
		ldi r18, 0x00
	cp1:	
		andi r16, 0b00000001
		cpi r16, 0b00000001	
		breq foo
	cp2:
		mov r16, r0
		andi r16, 0b00000010
		cpi r16, 0b00000010	
		breq foo2
	cp3:
		mov r16, r0
		andi r16,0b00000100
		cpi r16, 0b00000100
		breq foo3
	cp4:
		mov r16, r0
		andi r16,0b00001000
		cpi r16, 0b00001000	
		breq foo4
	cp5:
		mov r16, r0
		andi r16,0b00010000
		cpi r16, 0b00010000	
		breq foo5
	cp6:
		mov r16, r0
		andi r16,0b00100000
		cpi r16, 0b00100000	
		breq foo6
	finish:
		sts PORTL, r17
		sts PORTB, r18
		clr r20
		ret

	foo:
		ldi r20, 0b10000000
		add r17, r20
		jmp cp2
	foo2:
		ldi r20, 0b00100000
		add r17, r20
		jmp cp3
	foo3:
		ldi r20, 0b00001000
		add r17, r20
		jmp cp4
	foo4:
		ldi r20, 0b00000010
		add r17, r20
		jmp cp5
	foo5:
		ldi r20, 0b00001000
		add r18, r20
		jmp cp6
	foo6:
		ldi r20, 0b00000010
		add r18, r20
		jmp finish


