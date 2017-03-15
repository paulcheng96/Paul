function [w, E_history] = gradientDescentMulti(X, Y, w, kappa, num_iters)
% Initialize some useful values
m = length(Y); % number of training examples
E_history = zeros(num_iters, 1);
for iter = 1:num_iters
    w = w + (kappa/m)*sum((Y-X*w).*X)'; 
    % Save the cost J in every iteration
    E_history(iter) = computeCostMulti(X, Y, w);
end