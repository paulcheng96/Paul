function nc(f)
h = (1-0.78)/6;
y = 6*h/20*(11*f(0.78+h)-14*f(0.78+2*h)+26*f(0.78+3*h)-14*f(0.78+4*h)+11*f(0.78+5*h));
fprintf('%f',y);
return 
end




