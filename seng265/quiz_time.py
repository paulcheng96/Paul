import libxml2
import sys
import quiz_library

'''
purpose
	Accept 1 or more log file names on the command line.

	For each log file, compute the total time taken for each question. 

	Write to standard output, the average time spent for each question.
preconditions
	Each command-line argument is the name of a readable and
	legal quiz log file.

	All the log_files have the same number of questions.
'''

 #handle command line arguments
if len(sys.argv) < 2:
	print 'Syntax:', sys.argv[0], 'quiz_log_file ...'
	sys.exit()


list1 = []
y1 = 1
while y1<len(sys.argv):
	marks_list1 = quiz_library.load_quiz_log(sys.argv[y1])
	list1.append(marks_list1)
	y1+=1
question_count = quiz_library.compute_question_count(marks_list1)




y = 0
list = []
list_main = []
time = 0
index = 0
inital = 0
x = 0
gate = True

while x < len(list1):
	while index < question_count:
		#print index
		while y < len(list1[x]):

			if list1[x][y].time == 'None' :
				y = y+1
				continue
			if int(list1[x][y].index) == index and gate == True:
				inital = int(list1[x][y].time) 				
				gate = False
				y = y+1
				continue
			if int(list1[x][y].index) == index :
				time1 = int(list1[x][y-1].time)
				time = time+int(list1[x][y].time)-time1
				
 
			if int(list1[x][y].index) != index and gate ==  False:	
				time = (int(list1[x][y].time) - inital)
				gate = True
				
			y = y+1
		list.append(time)
		index +=1
		gate = True
		y=0
		time = 0
	list_main.append(list)
	list = []
	x = x+1
	index = 0


list2 = []
x=0
number = 0
y1 = 0
while x < len(list_main[0]):
	while y1 < len(list_main):
		number += float(list_main[y1][x])
		y1+=1
	y1 = 0
	ratio = float(number/len(list_main))
	number = 0
	list2.append(ratio)
	x+=1
print ','.join([str(x) for x in list2])




















	