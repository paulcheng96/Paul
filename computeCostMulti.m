function J = computeCostMulti(X, y, w)
% Initialize some useful values
n = length(y); % number of training examples
J = 0;
J = sum(((w'*(X')-y').^2))/(2*n);
end