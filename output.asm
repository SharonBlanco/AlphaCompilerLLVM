	.file	"mi_modulo"
	.text
	.globl	main
	.p2align	4
	.type	main,@function
main:
	.cfi_startproc
	subq	$24, %rsp
	.cfi_def_cfa_offset 32
	movl	$10, 20(%rsp)
	movl	$20, 16(%rsp)
	movl	$30, 12(%rsp)
	movl	$.Lfmt, %edi
	movl	$30, %esi
	xorl	%eax, %eax
	callq	printf@PLT
	xorl	%eax, %eax
	addq	$24, %rsp
	.cfi_def_cfa_offset 8
	retq
.Lfunc_end0:
	.size	main, .Lfunc_end0-main
	.cfi_endproc

	.type	.Lfmt,@object
	.section	.rodata.str1.1,"aMS",@progbits,1
.Lfmt:
	.asciz	"%d\n"
	.size	.Lfmt, 4

	.section	".note.GNU-stack","",@progbits
