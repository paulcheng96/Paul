import libxml2
import sys

'''
purpose
	store the information from an answer element
'''
class Answer:
	def __init__(self, index, path, result, answer, time):
		self.index = index
		self.path = path
		self.result = result
		self.answer = answer
		self.time = time

'''
purpose
	Store the information from a display element.
'''
class Display:
	def __init__(self, index, path, time):
		self.index = index
		self.path = path
		self.time = time

'''
purpose
	Extract the information from log_file and return it as a list
	of answer and display objects.
preconditions
	log_file is the name of a legal, readable quiz log XML file
'''
def load_quiz_log(log_file):
	parse_tree = libxml2.parseFile(log_file)
	context = parse_tree.xpathNewContext()
	root = parse_tree.getRootElement()
	node = root.children
	list1 = []
	answer_node = root.children
	while answer_node is not None:
		if answer_node.name == "answer":
			child = answer_node.children
			list = []		
			for node in child:		
				if node.name == "index":					
					index = node.content
					if index == '':
						list.append("None")
					else:
						list.append(int(index))
				if node.name == "path" :							
					path = node.content
					if path == '':
						list.append("None")
					else:
						list.append(path)
				if node.name == "result" :								
					result = node.content
					if result == '':
						list.append("None")
					else:
						list.append(int(result))			
				if node.name == "answer" :								
					answer = node.content
					if answer == '':
						list.append("None")
					else:
						list.append(answer)
				if node.name == "time" :								
					time = node.content
					if time == '':
						list.append("None")
					else:
						list.append(int(time))
			a = Answer(list[0],list[1],list[2],list[3],list[4])
			list1.append(a)		
		if answer_node.name == "display":
			child = answer_node.children
			list = []		
			for node in child:		
				if node.name == "index":					
					index = node.content
					if index == '':
						list.append("None")
					else:
						list.append(index)
				if node.name == "path" :							
					path = node.content
					if path == '':
						list.append("None")
					else:
						list.append(path)
				if node.name == "time" :								
					time = node.content
					if time == '':
						list.append("None")
					else:
						list.append(time)
			a = Display(list[0],list[1],list[2])
			list1.append(a)
		answer_node = answer_node.next
	return list1

	

'''
purpose
	Return the number of distinct questions in log_list.
preconditions
	log_list was returned by load_quiz_log
'''
def compute_question_count(log_list):
	number = 0
	for x in log_list:
		if int(x.index) > number:
			number = int(x.index)
	return number+1
	
	


'''
purpose
	Extract the list of marks.
	For each index value, use the result from the last non-empty answer,
	or 0 if there are no non-empty results.
preconditions
	log_list was returned by load_quiz_log
'''
def compute_mark_list(log_list):
	length = compute_question_count(log_list)
	list = [0]*(length)
	for x in log_list:
		if isinstance(x, Answer):
			if x.result != 'None':			
				m = int(x.result)
				list[x.index]= m
	return list
			


	
	