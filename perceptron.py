import numpy
import matplotlib.pyplot as plt


'''
decision function
'''
def h_function(W,x):
    value = numpy.dot(W,x)
    
    if value >= 0.0:
        return 1
    else:
        return -1


'''
cost function
'''
def Jcost_function(W,X,Y):
    sum = 0.0
    for i in range(len(Y)):
        sum += (h_function(W,X[i]) - Y[i]) * numpy.dot(W,X[i])
    return sum
    
'''
SGD method
'''
def SGD(W,X,Y,learning_rate,limit,max_iter):
    cnt = 0
    Jcost_pre = Jcost_function(W,X,Y)
    print ('init cost = ',Jcost_function(W,X,Y))
    x = [-8,8]
    y = [0.0]*2
    while cnt < max_iter:
        for i in range(len(Y)):
            
            if (Y[i] == 1 and h_function(W,X[i]) == -1):
                print (numpy.dot(learning_rate,X[i]))
                W = W + numpy.dot(learning_rate,X[i])
                print ('W = ',W)
                print ('cost = ',Jcost_function(W,X,Y))
            elif (Y[i] == -1 and h_function(W,X[i]) == 1):
                print (numpy.dot(learning_rate,X[i]))
                W = W - numpy.dot(learning_rate,X[i])
                print ('W = ',W)
                print ('cost = ',Jcost_function(W,X,Y))
            else:
                print ('this point ',X[i],' do not need to update!')
        if (abs(Jcost_pre - Jcost_function(W,X,Y))) < limit:
            print ('this Algotithm is convergence!!')
            break;
        Jcost_pre = Jcost_function(W,X,Y)
        cnt += 1
        for i in range(len(Y)):
            if Y[i] == 1:
                plt.plot(X[i][0],X[i][1],'*')
            else:
                plt.plot(X[i][0],X[i][1],'or')
        plt.xlim(-2,8)
        plt.ylim(-2,6)
        for i in range(len(x)):
            y[i] = -((W[0]*x[i]+W[2])/W[1])
        plt.plot(x,y)
        plt.show()    
        
    
    
    
    
if __name__ == '__main__':
    # W = [theta1,theta2,theta0]
    W = [0.3,0.8,-2.2]
    X = [(3,0.2,1),(1,0.3,1),(4,0.5,1),(2,0.7,1),(0,1,1),(1,1.2,1),(1,1.7,1),(6,0.2,1),(7,0.3,1),(6,0.7,1),(3,1.1,1),(2,1.5,1),(4,1.7,1),(2,1.9,1)]
    Y = [-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1]
    
    SGD(W,X,Y,0.01,0.0001,30)