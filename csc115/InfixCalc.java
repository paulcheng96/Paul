public class InfixCalc {
    /**
     * First ensure the arithmetic operations plus parentheses
     * are surrounded by spaces. Then go ahead and split up the
     * whole expression using spaces (i.e, the operands will be
     * nicely separated from everything else by at least a single
     * space). This will not work for negative numbers.
     */
    public static String[] tokenize(String s) {
        // Note the missing minus character...
        String symbols[] = {"\\(", "\\)", "\\+", "\\-", "\\*", "\\/",
            "\\^"};

        // First eliminate any quotation marks
        s = s.replaceAll("'", " ");
        s = s.replaceAll("\"", " ");

        // Now all operators or parentheses, surround the character
        // with a single space on either side.
        for (int i = 0; i < symbols.length; i++) {
            String regex = symbols[i];
            String replace = " " + regex + " ";
            s = s.replaceAll(regex, replace);
        }

        // Special case: If there is a unary minus, then then
        // what appears to the right of the symbol is whitespace
        // and a digit; what appears to the left whitespace
        // and a non-digit symbol.
        s = s.replaceAll("(^|([\\(\\+\\-\\*\\/]))\\s+\\-\\s+(\\d+)", "$1 -$3");

        // Eliminate extra whitespaces at start and end of the
        // transformed string. 
        s = s.trim();

        // Finally, take advantage of the whitespace to create an
        // array of strings where each item in the array is one
        // of the elements in the original string passed in to this
        // method.
        
		return s.split("\\s+");
		
    }
    
	public static boolean isBalanced(String expr){
		StringStack stack = new StringStackRefBased(100);
    try{
    for (int i = 0; i<expr.length(); i++){
        if (expr.charAt(i) == ('(')){
            stack.push("(");
        } else if (expr.charAt(i) == (')')){
            stack.pop();
        }
    }
    if (stack.isEmpty()){
        return true;
        } else {
            return false;
        }
    } catch (StringStackException e) {
        return false;
    }
	}
    
    public static StringList toPostfix(String[]	tokens){
        StringStack stack = new StringStackRefBased(100);
		StringList list = new StringList();
        try{
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals("(")) {
                stack.push("(");
            } else if (tokens[i].equals(")")) {
                String oper = stack.peek();
                while (!(oper.equals("(")) && !(stack.isEmpty())) {
					stack.pop();
					list.insertTail(oper);
					oper = stack.peek();
                }
                stack.pop();
            } else if (tokens[i].equals("+") || tokens[i].equals("-")) {                
                if (stack.isEmpty()) {
                    stack.push(tokens[i]);
                } else {
                    String oper = stack.peek();
                    while (!stack.isEmpty() && !oper.equals("(")) {                        
                        stack.pop();
						list.insertTail(oper);
						if (!stack.isEmpty()){
							oper = stack.peek();
						}
                    }
                    stack.push(tokens[i]);
                }
            } else if (tokens[i].equals("/") || tokens[i].equals("*")) {
                if (stack.isEmpty()) {
                    stack.push(tokens[i]);
                } else {                    
                    String oper = stack.peek();
                    while (!oper.equals("+") && !oper.equals("-") && !stack.isEmpty() && !oper.equals("(")) {
                        stack.pop();
						list.insertTail(oper);
						if (!stack.isEmpty()){
						oper = stack.peek();
						}
					}
					stack.push(tokens[i]);
				}
				
            } else if (tokens[i].equals("^")) {                
				stack.push(tokens[i]);			
				
			}else {
                list.insertTail(tokens[i]);
            }
        }
        
		while (!stack.isEmpty()) {
            String oper = stack.peek();
            if (!oper.equals("(")) {
                list.insertTail(stack.pop());
            }
        }
		} catch (StringStackException message){
			return null;
		}
        
		return list;
	}
	
    public static String evaluatePostfix(StringList expr) {
        StringStack stack = new StringStackRefBased(100);
		int number1;
		int number2;
		int result=0;
		try{
		for (int i=0; i<expr.getLength();i++){
			if (expr.retrieve(i).equals("+")){
				int x = Integer.parseInt(stack.pop()) + Integer.parseInt(stack.pop());
				result = result+x;
				stack.push(String.valueOf(x));
			} else if (expr.retrieve(i).equals("-")){
				number2 = Integer.parseInt(stack.pop());
				number1 = Integer.parseInt(stack.pop());
				result = number1-number2;
				stack.push(String.valueOf(number1-number2));
			} else if (expr.retrieve(i).equals("*")){
				number2 = Integer.parseInt(stack.pop());
				number1 = Integer.parseInt(stack.pop());
				result = number1*number2;
				stack.push(String.valueOf(number1*number2));
			} else if (expr.retrieve(i).equals("/")){
						try{
							number2 = Integer.parseInt(stack.pop());
							number1 = Integer.parseInt(stack.pop());
							result = number1/number2;
							stack.push(String.valueOf(number1/number2));
						}catch (ArithmeticException e){
				}
			} else if (expr.retrieve(i).equals("^")){
				number2 = Integer.parseInt(stack.pop());
				number1 = Integer.parseInt(stack.pop());
				result = (int)(Math.pow(number1,number2));
				stack.push(String.valueOf(result));
			} else {
					stack.push(expr.retrieve(i));
					
			}  
		}
		result=Integer.parseInt(stack.pop());
		
		if (!stack.isEmpty()){			
			return "<syntax error>";
		}
		} catch (StringStackException message){
			return "<syntax error>";
		} catch (ArithmeticException e){
			return "Divide by zero";
		} catch (NumberFormatException e){
			return "<noninteger>";
		}
		return String.valueOf(result);
    }


    public static String evaluateExpression(String expr) {
        String result = "<nothing working right now>";
        if (!isBalanced(expr)){
			result = "<unbalanced ()>";
			return result;
		}
		StringList list = toPostfix(tokenize(expr));
		result = evaluatePostfix(list);
		
        return result;
    }
 

    public static void main(String args[]){
        InfixCalc t1 = new InfixCalc();
		boolean passed = true;
		String expr = "1+1-1";
		args = t1.tokenize(expr);
		if (args.length < 1) {
           System.err.println("usage: java InfixCalc '<expression>'");
            System.exit(1);
        }				
		t1.toPostfix(args);
		StringList list2 = t1.toPostfix(args);
		//test addition and subtraction;
		//expr  = "1+1-1" answer should be 1, postfix should be 11+1-
		if (!t1.evaluateExpression(expr).equals("1")){
			passed = false;
		}
		String postfix = "11+1-";		
		for (int i=0;i<list2.getLength();i++){
			if(!list2.retrieve(i).equals(postfix.substring(i,i+1))){
				passed = false;
			}
		}
		System.out.println("Test1" + ": " +(passed ? "passed" : "FAILED"));
		
		
		//test multiplication and division;
		//expr  = "1*6/2" answer should be 3, postfix should be 16*2/
		expr = "1*6/2";
		args = t1.tokenize(expr);
		list2 = t1.toPostfix(args);
		postfix = "16*2/";
		if (!t1.evaluateExpression(expr).equals("3")){
			passed = false;
		}				
		for (int i=0;i<list2.getLength();i++){
			if(!list2.retrieve(i).equals(postfix.substring(i,i+1))){
				passed = false;
			}
		}
		System.out.println("Test2" + ": " +(passed ? "passed" : "FAILED"));
		
		//test multiplication, division, addition and subtraction;
		//expr  = "1+1*6/2-2" answer should be 2, postfix should be 116*2/+2-;
		expr = "1+1*6/2-2";
		args = t1.tokenize(expr);
		list2 = t1.toPostfix(args);
		postfix = "116*2/+2-";
		if (!t1.evaluateExpression(expr).equals("2")){
			passed = false;
		}
		for (int i=0;i<list2.getLength();i++){
			if(!list2.retrieve(i).equals(postfix.substring(i,i+1))){
				passed = false;
			}
		}
		System.out.println("Test3" + ": " +(passed ? "passed" : "FAILED"));
		
		//test multiplication, division, addition and subtraction and power;
		//expr  = "1+1*6/2-2^2" answer should be 0, postfix should be "1+1*6/2-2^2";
		expr = "1+1*6/2-2^2";
		args = t1.tokenize(expr);
		list2 = t1.toPostfix(args);
		postfix = "116*2/+22^-";
		if (!t1.evaluateExpression(expr).equals("0")){
			passed = false;
		}
		for (int i=0;i<list2.getLength();i++){
			if(!list2.retrieve(i).equals(postfix.substring(i,i+1))){
				passed = false;
			}
		}
		System.out.println("Test4" + ": " +(passed ? "passed" : "FAILED"));
		
        

    }
}

