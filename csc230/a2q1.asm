;
; a2q1.asm
;
; Write a program that displays the binary value in r16
; on the LEDs.
;
; See the assignment PDF for details on the pin numbers and ports.
;
;
;
; These definitions allow you to communicate with
; PORTB and PORTL using the LDS and STS instructions
;
.equ DDRB=0x24
.equ PORTB=0x25
.equ DDRL=0x10A
.equ PORTL=0x10B



		ldi r16, 0xFF
		sts DDRB, r16		; PORTB all output
		sts DDRL, r16		; PORTL all output

		ldi r16, 0x33		; display the value
		mov r0, r16			; in r0 on the LEDs

; Your code here
		mov r19, r0
		ldi r17, 0x00
		ldi r18, 0x00
	cp1:	
		andi r19, 0b00000001
		cpi r19, 0b00000001	
		breq foo
	cp2:
		mov r19, r0
		andi r19, 0b00000010
		cpi r19, 0b00000010	
		breq foo2
	cp3:
		mov r19, r0
		andi r19,0b00000100
		cpi r19, 0b00000100
		breq foo3
	cp4:
		mov r19, r0
		andi r19,0b00001000
		cpi r19, 0b00001000	
		breq foo4
	cp5:
		mov r19, r0
		andi r19,0b00010000
		cpi r19, 0b00010000	
		breq foo5
	cp6:
		mov r19, r0
		andi r19,0b00100000
		cpi r19, 0b00100000	
		breq foo6
	finish:
		sts PORTL, r17
		sts PORTB, r18
		jmp done

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
		ldi r20, 0b00000001
		add r17, r20
		jmp cp5
	foo5:
		ldi r20, 0b00001000
		add r18, r20
		jmp cp6
	foo6:
		ldi r20, 0b00000001
		add r18, r20
		jmp finish
	
;
; Don't change anything below here
;
done:	jmp done
