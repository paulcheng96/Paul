#define LCD_LIBONLY

.include "lcd.asm"

.cseg

	; initialize the Analog to Digital conversion

	ldi r16, 0x87
	sts ADCSRA, r16
	ldi r16, 0x40
	sts ADMUX, r16
		

		

    call lcd_init
    call lcd_clr
	call init_strings
	call init_ptrs

	; initialize PORTB and PORTL for ouput
	ldi	r16, 0xFF
	sts DDRB,r16
	sts DDRL,r16	
	ldi r17,0
	clr r2
	
lp: 
	call lcd_clr
	call cpy_msg
	call display_strings
	call inc_pointers
	call delay
	call LED
	call delay
	call LED

	
	
	jmp lp


; set l1ptr && l2ptr to point at the start of the display strings
init_ptrs:
	push XL
	push XH
	push r20
	push r21

	ldi XL, low(l1ptr)
	ldi XH, high(l1ptr)

	ldi r20, low(msg1)
	ldi r21, high(msg1)
	st X+, r20
	st X, r21

	ldi XL, low(l2ptr)
	ldi XH, high(l2ptr)

	ldi r20, low(msg2)
	ldi r21, high(msg2)
	st X+, r20
	st X, r21

	pop r21
	pop r20
	pop XH
	pop XL
	ret
; copy from the pointers in msg1 and msg2 to line1 and line2
cpy_msg:
	push r17 ; i
	push r18 ; tempChar
	push YL ; lineN_low
	push YH ; lineN_high
	push ZL ; lNptr_low
	push ZH ; lNptr_high
	push XL ; *lNptr_low
	push XH ; *lNptr_high

	; for line1
	ldi YL, low(line1)
	ldi YH, high(line1)
	ldi ZL, low(l1ptr)
	ldi ZH, high(l1ptr)
	ld XL, Z+
	ld XH, Z

	ldi r17, 0
	forLoop1Start:
		cpi r17, 16
		brge forLoop1End
		ld r18, X+
		cpi r18, 0x00
		breq resetPtr1
		st Y+, r18
		rjmp copyCompleted1	
		resetPtr1:
		ldi XL, low(msg1)
		ldi XH, high(msg1)
		rjmp forLoop1Start
		copyCompleted1:
		inc r17
		rjmp forLoop1Start
	forLoop1End:
	ldi r18, 0
	st Y, r18 ; add '\0'

	; for line2
	ldi YL, low(line2)
	ldi YH, high(line2)
	ldi ZL, low(l2ptr)
	ldi ZH, high(l2ptr)
	ld XL, Z+
	ld XH, Z

	ldi r17, 0
	forLoop2Start:
		cpi r17, 16
		brge forLoop2End
		ld r18, X+
		cpi r18, 0x00
		breq resetPtr2
		st Y+, r18
		rjmp copyCompleted2	
		resetPtr2:
		ldi XL, low(msg2)
		ldi XH, high(msg2)
		rjmp forLoop2Start
		copyCompleted2:
		inc r17
		rjmp forLoop2Start
	forLoop2End:
	ldi r18, 0
	st Y, r18 ; add '\0'

	pop XH
	pop XL
	pop ZH
	pop ZL
	pop YH
	pop YL
	pop r18
	pop r17
	ret

    
inc_pointers:
	push r18 ; tempChar
	push YL ; lineN_low
	push YH ; lineN_high
	push ZL ; lNptr_low
	push ZH ; lNptr_high
	push XL ; *lNptr_low
	push XH ; *lNptr_high
	
	; for line1
	ldi ZL, low(l1ptr)
	ldi ZH, high(l1ptr)
	ld XL, Z+
	ld XH, Z

	ld r18, X+
	ld r18, X
	cpi r18, 0x00
	brne noReset01
	; update ptr
	ldi XL, low(msg1)
	ldi XH, high(msg1)
	noReset01:
	ldi ZL, low(l1ptr)
	ldi ZH, high(l1ptr)
	st Z+, XL
	st Z, XH

	; for line2
	ldi ZL, low(l2ptr)
	ldi ZH, high(l2ptr)
	ld XL, Z+
	ld XH, Z

	ld r18, X+
	ld r18, X
	cpi r18, 0x00
	brne noReset02
	; update ptr
	ldi XL, low(msg2)
	ldi XH, high(msg2)
	noReset02:
	ldi ZL, low(l2ptr)
	ldi ZH, high(l2ptr)
	st Z+, XL
	st Z, XH

	pop XH
	pop XL
	pop ZH
	pop ZL
	pop YH
	pop YL
	pop r18
	ret

check_button:
		
		push r16
		push r17
		
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
		cpi r17, 3			;  if > 0x3E8, no button pressed 
		brne bsk1		    ;  
		cpi r16, 0xE8		; 
		brsh bsk_done		; 
bsk1:	tst r17				; if ADCH is 0, might be right or up  
		brne bsk2			; 
		cpi r16, 0x32		; < 0x32 is right
		brsh bsk3
		ldi r24, 0x01		; right button
		
		rjmp bsk_done
bsk3:	cpi r16, 0xC3		
		brsh bsk4	
		ldi r24, 0x02		; up			
		rjmp bsk_done
bsk4:	ldi r24, 0x04		; down (can happen in two tests)
		rjmp bsk_done
bsk2:	cpi r17, 0x01		; could be up,down, left or select
		brne bsk5
		cpi r16, 0x7c		; 
		brsh bsk7
		ldi r24, 0x04		; other possiblity for down
		rjmp bsk_done
bsk7:	ldi r24, 0x08		; left
		rjmp bsk_done
bsk5:	cpi r17, 0x02
		brne bsk6
		cpi r16, 0x2b
		brsh bsk6
		ldi r24, 0x08
		rjmp bsk_done
bsk6:	ldi r24, 0x10
bsk_done:
		pop r17
		pop r16
		ret

init_strings:

    ; ***** WHAT THIS FUNCTION DOES ******
    ; first it copies strings from program memory to data memory

    push r16
    ldi r16, high(msg1)     ; this the destination
    push r16
    ldi r16, low(msg1)
    push r16
    ldi r16, high(msg1_p << 1) ; this is the source
    push r16
    ldi r16, low(msg1_p << 1)
    push r16
    call str_init           ; copy from program to data
    pop r16                 ; remove the parameters from the stack
    pop r16
    pop r16
    pop r16

    ldi r16, high(msg2)
    push r16
    ldi r16, low(msg2)
    push r16
    ldi r16, high(msg2_p << 1)
    push r16
    ldi r16, low(msg2_p << 1)
    push r16
    call str_init
    pop r16
    pop r16
    pop r16
    pop r16

    pop r16
    ret

display_strings:

    ; ******* WHAT THIS FUNCTION DOES *********
    ; This subroutine sets the position the next
    ; character will be output on the lcd
    ;
    ; The first parameter pushed on the stack is the Y position
    ; 
    ; The second parameter pushed on the stack is the X position
    ; 
    ; This call moves the cursor to the top left (ie. 0,0)

    push r16

    call lcd_clr

    ldi r16, 0x00
    push r16
    ldi r16, 0x00
    push r16
    call lcd_gotoxy
    pop r16
    pop r16

    ldi r16, high(line1) ; Now display msg1 on the first line
    push r16
    ldi r16, low(line1)
    push r16
    call lcd_puts
    pop r16
    pop r16

    ldi r16, 0x01 ; Now move the cursor to the second line (ie. 0,1)
    push r16
    ldi r16, 0x00
    push r16
    call lcd_gotoxy
    pop r16
    pop r16

    ldi r16, high(line2) ; Now display msg1 on the second line
    push r16
    ldi r16, low(line2)
    push r16
    call lcd_puts
    pop r16
    pop r16

    pop r16
    ret

LED:
		

		call check_button
		cpi r24, 1
		breq inside
		cpi r24, 8
		breq decside
		cpi r24, 2
		breq stop1
		ret
stop1:
		call check_button
		cpi r24, 4
		breq down1	
		jmp stop1

down1: ret

outside:	
		call display
		ret
inside:	
		inc r2		
		
		rjmp outside
decside:
		dec r2
		
		rjmp outside
display:
		push r16
		push r17
		push r18
		push r20
		mov r16, r2
		ldi r17, 0x00
		ldi r18, 0x00
	cp1:	
		andi r16, 0b00000001
		cpi r16, 0b00000001	
		breq foo
	cp2:
		mov r16, r2
		andi r16, 0b00000010
		cpi r16, 0b00000010	
		breq foo2
	cp3:
		mov r16, r2
		andi r16,0b00000100
		cpi r16, 0b00000100
		breq foo3
	cp4:
		mov r16, r2
		andi r16,0b00001000
		cpi r16, 0b00001000	
		breq foo4
	cp5:
		mov r16, r2
		andi r16,0b00010000
		cpi r16, 0b00010000	
		breq foo5
	cp6:
		mov r16, r2
		andi r16,0b00100000
		cpi r16, 0b00100000	
		breq foo6
	finish:
		sts PORTL, r17
		sts PORTB, r18
		pop r20
		pop r18
		pop r17
		pop r16
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

delay:
		push r19
		push r21
		push r22
		ldi r19, 0x10
del1:	nop
		ldi r21,0xFF
del2:	nop
		ldi r22, 0xFF
del3:	nop
		dec r22
		brne del3
		dec r21
		brne del2
		dec r19
		brne del1	
		pop r22
		pop r21
		pop r19
		ret
msg1_p: .db "Damn, Iceland lost the game.", 0   
msg2_p: .db "And England is the worst team. ", 0

.dseg

; The program copies the strings from program memory
; into data memory.  These are the strings
; that are actually displayed on the lcd

msg1:   .byte 200
msg2:   .byte 200
line1:  .byte 17
line2:  .byte 17
l1ptr:  .byte 2
l2ptr:  .byte 2
