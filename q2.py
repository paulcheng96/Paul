import numpy as np
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.svm import LinearSVC
import xml.etree.ElementTree as ET
"""
Created on Thu Jul 15 16:00:45 2017

@author: Paul
"""
def readstring(filename):
    with open(filename, "r") as file:
        First = True
        Last = False
        s = ""
        for lines in file:
            lines = lines.strip()
            if len(lines) == 0: 
                continue
            if First and lines == "<lexelt item=\"appear.v\">":
                First = False
                Last = True
            if Last: 
                lines = lines.replace("<head>", "::").replace("</head>", "::")
                lines = lines.replace("&", "_")
                s += lines + "\n"
            if Last and lines == "</lexelt>":
                break
    return s

def make_dict(doc):
    data = {}
    for ins in doc:
        instid = ins.attrib["id"]
 
        c1 = []
        for children in ins:
            if children.tag == "context":
                context = children.text.strip()
                words = context.split()
                for x in words:
                    if x.startswith("::appear") and x.endswith("::"): 
                        x = x.replace("::", "")
                    # clean up the word. Lower case, and no punctuation.
                    x = x.lower()
                    include = True
                    i = 0
                    for c in x:
                        if c < 'a' or c > 'z': include = False
                    if include: c1.append(x)
                    i = i+1
                win = 6
                if win > 0:
                    first = max(0, i - win)
                    last = min(i+win, len(words)-1)
                    c1 = c1[first:last+1]
        data[instid] = " ".join(c1)
    return data
def final(filename,data):
    contexts = []
    classes = []
    answers = {}
    with open(filename + ".key", "r") as f:
        for lines in f:
            lines = lines.strip()
            if len(lines) == 0: continue
            parts = lines.split()
            if parts[1] in data:
                answers[parts[1]] = parts[2:]
    for id in data:
        for a in answers[id]:
            if a!= "U":
                contexts.append(data[id])
                classes.append(a)
    return contexts, classes
def build_map(train_answers):
    map1 = {}
    i = 0
    unique = np.unique(train_answers)
    for word in unique:
        map1[word] = i
        i += 1
    return map1

def ytrain(answers,Map):
    y= np.zeros(len(answers))
    for i in range(len(answers)):
        y[i] = Map[answers[i]]
        i += 1   
    return y


s = readstring("EnglishLS.train/EnglishLS.train")
doc = ET.fromstring(s)
dict1 = make_dict(doc)
data_train, train_answers = final("EnglishLS.train/EnglishLS.train",dict1)

s1 = readstring("EnglishLS.test/EnglishLS.test")
doc1 = ET.fromstring(s1)
dict2 = make_dict(doc1)
data_test, test_answers = final("EnglishLS.test/EnglishLS.test",dict2)
    
print("number of training data:",len(data_train))
print("number of testing data:",len(data_test))

Map = build_map(train_answers)
y_train = ytrain(train_answers,Map)
y_test = ytrain(test_answers,Map)

vectorizer = CountVectorizer(lowercase=True, stop_words="english",  max_df=1.0, min_df=1, max_features=None,  binary=True)
X_train = vectorizer.fit_transform(data_train).toarray()
X_test = vectorizer.transform(data_test).toarray()
#feature_names = vectorizer.get_feature_names()
clf = LinearSVC(C=1)
clf.fit(X_train,y_train)
y_pred = clf.predict(X_test)
print ('accuracy = ', np.mean(y_test == y_pred))    

