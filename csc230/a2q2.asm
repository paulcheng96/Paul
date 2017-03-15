;
; a2q2.asm
;
;
; Turn the code you wrote in a2q1.asm into a subroutine
; and then use that subroutine with the delay subroutine
; to have the LEDs count up in binary.
;
;
; These definitions allow you to communicate with
; PORTB and PORTL using the LDS and STS instructions
;
.equ DDRB=0x24
.equ PORTB=0x25
.equ DDRL=0x10A
.equ PORTL=0x10B


; Your code here
; Be sure that your code is an infite loop
.def counter = r0
		clr counter

start:		
		call display
		inc counter
		ldi r20, 0x10
		call delay
		rjmp start


done:		jmp done	; if you get here, you're doing it wrong

;
; display
; 
; display the value in r0 on the 6 bit LED strip
;
; registers used:
;	r0 - value to display
;
display:
		mov r19, counter
		ldi r17, 0x00
		ldi r18, 0x00
	cp1:	
		andi r19, 0b00000001
		cpi r19, 0b00000001	
		breq foo
	cp2:
		mov r19, counter
		andi r19, 0b00000010
		cpi r19, 0b00000010	
		breq foo2
	cp3:
		mov r19, counter
		andi r19,0b00000100
		cpi r19, 0b00000100
		breq foo3
	cp4:
		mov r19, counter
		andi r19,0b00001000
		cpi r19, 0b00001000	
		breq foo4
	cp5:
		mov r19, counter
		andi r19,0b00010000
		cpi r19, 0b00010000	
		breq foo5
	cp6:
		mov r19, counter
		andi r19,0b00100000
		cpi r19, 0b00100000	
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
;
; delay
;
; set r20 before calling this function
; r20 = 0x40 is approximately 1 second delay
;
; registers used:
;	r20
;	r21
;	r22
;
delay:	
del1:	nop
		ldi r21,0xFF
del2:	nop
		ldi r22, 0xFF
del3:	nop
		dec r22
		brne del3
		dec r21
		brne del2
		dec r20
		brne del1	
		ret
