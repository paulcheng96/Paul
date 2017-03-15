import numpy as np

import random

from numpy import genfromtxt



def getData(dataSet):
    trainData1 = dataSet[:,:-1]
    trainLabel = dataSet[:,-1]        

    print (trainData1)
    print (trainLabel)
    

    xTrains = np.transpose(trainData1)    
    print(xTrains)
    print(xTrains*trainData1)
    w = np.linalg.pinv(xTrains*trainData1)*xTrains*trainLabel
    print (w)
    return trainData1, trainLabel


dataPath = r"C:\Users\admin\Desktop\seng474\regdata.csv"

dataSet = genfromtxt(dataPath, delimiter=',')

trainData, trainLabel = getData(dataSet)

x = [[1,2,3],[4,5,6],[7,8,9],[10,11,12]]
y = [[1,2],[1,2],[3,4]]

mx = np.matrix(x)
my = np.matrix(y) 
x = np.arange(4).reshape((2,2))
print(np.transpose(x))