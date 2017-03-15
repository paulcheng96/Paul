function [ root ] = Newton( f, fp, x0, s, imax )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    i=1;
    fprintf ( ' iteration approximation \n')
    while (i <= imax)
        root = x0-feval(f,x0)/feval(fp,x0);
        fprintf ( ' %6.0f %18.8f \n', i, root )  
        if (abs(1-x0/root)<s)
           return;
        end
        i = i + 1;
        x0 = root;
    end
    fprintf ( ' failed to converge in %g iterations\n', imax )
end


