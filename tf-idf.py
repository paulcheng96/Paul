# -*- coding: utf-8 -*-
"""
Created on Sun Jun 18 22:40:07 2017

@author: admin
"""

import os
import numpy as np
from sklearn.naive_bayes import MultinomialNB, BernoulliNB
from sklearn.svm import LinearSVC
from sklearn.feature_extraction.text import  TfidfVectorizer 
from sklearn.feature_selection import VarianceThreshold, SelectFromModel
from sklearn.model_selection import train_test_split
from sklearn.metrics import recall_score,f1_score

def read_data(path):
	x, y = [], []
	for f in os.listdir(path):
		fn = os.path.join(path, f)
		if ('.txt' in fn or '.tsv' in fn) and f != 'README.txt':
			for l in open(fn).readlines():
				l = l.strip()
				if l.find('negative') > 0:
					tx = l[l.index('negative')+len('negative')+1:]
					ty = -1
				elif l.find('neutral') > 0:
					tx = l[l.index('neutral')+len('neutral')+1:]
					ty = 0
				elif l.find('positive') > 0:
					tx = l[l.index('positive')+len('positive')+1:]
					ty = 1
			
				if tx not in x:
					x.append(tx)
					y.append(ty)

	return np.array(x), np.array(y)


def build_dict_from_corpus(x, min_freq):
	# build a dictionary from corpus x
	dictionary = {}
	for _x in x:
		for _w in _x.split():
			if _w not in dictionary:
				dictionary.update({_w: 1})
			else:
				dictionary[_w] += 1

	# sort the dictionary based on each word's frequency
	filter_dict = sorted(dictionary.items(), key=lambda d:d[1], reverse = True)
	# filter out some words with low frequency
	filter_dict = [d for d in filter_dict if d[1] >= min_freq]
	
	dictionary = {}
	i = 0
	for d in filter_dict:
		dictionary.update({d[0]:i})
		i += 1
	
	return dictionary

def measure(y_test, z):
	print ('accuracy = ', np.mean(y_test == z))
	print ('macro f1-score = ', f1_score(y_test, z, average='macro'))
	print ('macro-recall = ', recall_score(y_test, z, average='macro'))
	

if __name__ == '__main__':
	x, y = read_data('./2017_English_final/GOLD/Subtask_A')
#	print (x.shape,y.shape)	
	x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.33)
	
	print (x_train.shape, x_test.shape)	
	dictionary = build_dict_from_corpus(x_train, 10)
	count_vec = TfidfVectorizer(binary = True,stop_words = 'english')    


	x_train = count_vec.fit_transform(x_train)  
	x_test  = count_vec.transform(x_test)  
                       	

	clf2 = MultinomialNB()
	clf3 = LinearSVC(C=0.05)
	clf4 = BernoulliNB()        

	print ('----------')
	print (x_train.shape, x_test.shape)

    
	clf2.fit(x_train,y_train)
	z= clf2.predict(x_test)
	measure(y_test, z)

	clf4.fit(x_train, y_train)
	z= clf4.predict(x_test)
	measure(y_test, z)	
	
	clf3.fit(x_train, y_train)
	z= clf3.predict(x_test)
	measure(y_test, z)	
	print ('----------')

	selector = VarianceThreshold(0.0001)
	selector.fit(x_train)
	x_train_new, x_test_new = selector.transform(x_train), selector.transform(x_test)
	print (x_train_new.shape, x_test_new.shape)


	clf2.fit(x_train_new,y_train)
	z= clf2.predict(x_test_new)
	measure(y_test, z)

	clf4.fit(x_train, y_train)
	z= clf4.predict(x_test)
	measure(y_test, z)	
	
	clf3.fit(x_train_new, y_train)
	z= clf3.predict(x_test_new)
	measure(y_test, z)	

	print ('----------')
	lsvc = LinearSVC(C=0.05, penalty="l1", dual=False).fit(x_train, y_train)
	selector = SelectFromModel(lsvc, prefit=True)
	x_train_new, x_test_new = selector.transform(x_train), selector.transform(x_test)
	print (x_train_new.shape, x_test_new.shape)

	
	clf2.fit(x_train_new, y_train)
	z = clf2.predict(x_test_new)
	measure(y_test, z)

	clf4.fit(x_train, y_train)
	z = clf4.predict(x_test)
	measure(y_test, z)		
	
	clf3.fit(x_train_new, y_train)
	z = clf3.predict(x_test_new)
	measure(y_test, z)		
