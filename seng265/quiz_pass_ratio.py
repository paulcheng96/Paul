import libxml2
import sys
import quiz_library

'''
purpose
	Accept 1 or more log file names on the command line.

	Accumulate across all the log files the pass ratio for each question.

	A question result is considered a pass if it is not 0 or None
	and fail otherwise.

	The pass ratio for a question is the number of passes
	divided by the number of passes + fails.
preconditions
	Each command-line argument is the name of a
	readable and legal quiz log file.

	All the log_files have the same number of questions.
'''

# check number of command line arguments
if len(sys.argv) < 2:
	print 'Syntax:', sys.argv[0], 'quiz_log_file ...'
	sys.exit()

y=1
list = []
list2 = []
while y<len(sys.argv):
	marks_list1 = quiz_library.load_quiz_log(sys.argv[y])
	m = quiz_library.compute_mark_list(marks_list1)
	list.append(m)
	y+=1
x=0
number = 0
y1 = 0
while x < len(list[0]):
	while y1 < len(list):
		number += float(list[y1][x])
		y1+=1
	y1 = 0
	ratio = float(number/len(list))
	number = 0
	list2.append(ratio)
	x+=1

print ','.join([str(x) for x in list2])
