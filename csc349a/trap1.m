function trap(a, b, maxiter, tol,f)
   m = 1;
   x = linspace(a, b, m+1);
   y = f(x);
   approx = trapz(x, y);
   disp(' m integral approximation');
   fprintf('%5.0f %16.10f \n ', m, approx);
   for i = 1 : maxiter
     m = 2^i;
     oldapprox = approx ;
     x = linspace ( a , b , m+1 ) ;
     y = f(x);
     approx = trapz(x, y);
     fprintf('%5.0f %16.10f \n ', m, approx);
     if abs((approx-oldapprox)/oldapprox ) < tol
       return
     end
    end
    fprintf('Did not converge in %g iterations', maxiter)

