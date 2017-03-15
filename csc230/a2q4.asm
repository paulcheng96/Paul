;
; a2q4.asm
;
; Fix the button subroutine program so that it returns
; a different value for each button
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


		clr r0
		call display
lp:
		call check_button
		tst r24
		breq lp
		mov	r0, r24

		call display
		ldi r20,0x10
		call delay
		ldi r21, 0
		mov r0, r21
		call display
		rjmp lp

;
; An improved version of the button test subroutine
;
; Returns in r24:
;	0 - no button pressed
;	1 - right button pressed
;	2 - up button pressed
;	4 - down button pressed
;	8 - left button pressed
;	16- select button pressed
;
; this function uses registers:
;	r24
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
check_button:
		; start a2d
		lds	r16, ADCSRA	
		ori r16, 0x40
		sts	ADCSRA, r16

		; wait for it to complete
wait:		lds r16, ADCSRA
		andi r16, 0x40
		brne wait

		; read the value
		lds r16, ADCL
		lds r17, ADCH

		; put your new logic here:
		clr r24
check0:
		cpi r17,0
		breq opr
check1:
		cpi r17,1
		breq opr1
check2:
		cpi r17,2
		breq opr2
check3:	
		cpi r17,3
		breq opr3
		jmp no

opr4:
		jmp no
opr3:
		cpi r16,0x16
		brlo select
		jmp no
opr2:
		cpi r16,0x2B
		brlo left
		jmp select
opr1:
		cpi r16,0x7C
		brlo down
		jmp left

opr:
		cpi r16,0x32
		brlo right
		cpi r16,0xC3
		brlo up
		jmp down


right:
		ldi r24,1
		jmp end
up:
		ldi r24,2
		jmp end
down:
		ldi r24,4
		jmp end
left:
		ldi r24,8
		jmp end
select:
		ldi r24,16
		jmp end
no:
		ldi r24,0
		
end:	ret

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
; display the value in r0 on the 6 bit LED strip
;
; registers used:
;	r0 - value to display
;
display:
.def counter = r0
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
		ret

