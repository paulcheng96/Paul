%线性回归matlab
%read file
m = csvread('regdata.csv');
X = m(:,1:2);
Y = m(:,3:3);
%scaling
mean1 = mean(X);
max1 = max(X);
min1 = min(X);
mean2 = mean(Y);
max2 = max(Y);
min2 = min(Y);
X=(X-mean1)./(max1-min1);
Y=(Y-mean2)./(max2-min2);
X1 = ones(length(X),3);
X1(:,1:2) = X;
X=X1;
%main function
kappa = 0.1;
num_iters = 400;
w = zeros(3,1);
[w, E_history] = gradientDescentMulti(X,Y,w,kappa,num_iters);
% Plot the convergence graph
figure;
%numel
plot(1:numel(E_history), E_history, '-b', 'LineWidth', 2);
fprintf('minimum cost \n');
fprintf('%f \n',E_history(length(E_history)))
xlabel('Number of iterations');
ylabel('Error');
% Display gradient descent's result
fprintf('w computed from gradient descent: \n');
fprintf(' %f \n', w);
fprintf('\n');




