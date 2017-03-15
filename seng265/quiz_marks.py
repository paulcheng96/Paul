import libxml2
import sys
import quiz_library

'''
purpose
	Accept 1 or more log file names on the command line.
	For each log file
		write to standard output the course mark for the log file,
		in CSV format
preconditions
	Each command-line argument is the name of a legal, readable quiz log file.

	All of the log files have the same number of questions.
'''

# handle command line arguments
if len(sys.argv) < 2:
	print 'Syntax:', sys.argv[0], 'quiz_log_file ...'
	sys.exit()
y=1
mark1 = 0
while y<len(sys.argv):
	marks_list1 = quiz_library.load_quiz_log(sys.argv[y])
	m = quiz_library.compute_mark_list(marks_list1)
	
	for x in m:
		mark1 = x + mark1
	print sys.argv[y]+','+str(mark1)
	mark1 = 0
	y=y+1

