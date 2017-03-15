import numpy as np
import matplotlib.pyplot as plt

da = np.genfromtxt('regdata.csv',delimiter=',')

X = da[:,0:2]
y = da[:,-1:]

n=X.shape[0]   # number of rows
m=X.shape[1]+1   # number of columns + 1

X = (X - np.mean(X,0))/(np.max(X,0)-np.min(X,0))

X = np.concatenate((np.ones((n,1)), X), axis=1)

w = np.ones((m,1))   # initialize w

kappa = 0.5

E = []

for i in range(100):
    E.append(1.0/(2*n)*sum((y-X.dot(w))**2))
    w = w + kappa*(1.0/n)*sum((y-X.dot(w))*X).reshape(w.shape)
    
print(w)

# compare with:
print(np.linalg.pinv(X.T.dot(X)).dot(X.T.dot(y)))

# plot evolution of E
t = range(100)
tm = 8
plt.plot(t[:tm],E[:tm],'b-')
plt.xlabel('iter')
plt.ylabel('E')
plt.show()
