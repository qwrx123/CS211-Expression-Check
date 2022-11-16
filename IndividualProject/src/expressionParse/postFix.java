package expressionParse;

import java.util.HashSet;
import java.util.Stack;

public class postFix {
	//set for all operators
	private HashSet<Character> mySet;
	//a stack for storing the running total
	private Stack<Integer> myStack;
	//accumulator to accumulate values, our "register"
	private int accumulator;
	//change will change the accumulator, the other "register"
	private int change;
	
	public postFix() {
		//initialize the HashSet of operators
		mySet = new HashSet<Character>();
		//add operators to HashSet
		mySet.add('+');
		mySet.add('-');
		mySet.add('*');
		mySet.add('/');
		mySet.add('^');
		mySet.add('!');
		mySet.add('%');
		
		//initialize my stack
		myStack = new Stack<Integer>();
		
		//create the 2 "registers" to be used
		accumulator = 0;
		change = 0;
	}
	
	public int evaluate(String[] parsed) throws ArithmeticException {
		for (int i = 0; i < parsed.length; i++) {
			//get current char
			char currentChar = parsed[i].charAt(0);
			//if the current char is an operand
			if (mySet.contains(currentChar) && parsed[i].length() == 1) {
				//accumulator will be changed by change amount
				if (currentChar != '!') {
					change = myStack.pop();
					accumulator = myStack.pop();
				}
				//if factorial, then only 1 pop because factorial is a unary operator
				else {
					accumulator = myStack.pop();
				}
				//find which operand
                switch (currentChar) {
                //addition
                case '+':
                	//if overflows then throw error
                	if (change >= 0 && accumulator > Integer.MAX_VALUE - change) {
                		throw new ArithmeticException("Integer Overflow Error");
                	}
                	//if underflows then throw error
                	else if (change < 0 && accumulator < Integer.MIN_VALUE - change) {
                		throw new ArithmeticException("Integer Underflow Error");
                	}
                	//change accumulator
                    accumulator += change;
                    break;
                //subtraction
                case '-':
                	//if underflows then throw error
                	if (change >= 0 && accumulator < Integer.MIN_VALUE + change) {
                		throw new ArithmeticException("Integer Underflow Error");
                	}
                	//if overflows then throw error
                	else if (change < 0 && accumulator > Integer.MAX_VALUE + change) {
                		throw new ArithmeticException("Integer Overflow Error");
                	}
                	//change accumulator
                    accumulator -= change;
                    break;
                //multiplication
                case '*':
                	//4 cases for multiplication overflowing
                	//if overflows then throw error
                	if (accumulator > 0 && change > 0 && accumulator > Integer.MAX_VALUE/change) {
                		throw new ArithmeticException("Integer Overflow Error");
                	}
                	//if underflows then throw error
                	if (accumulator > 0 && change < 0 && change < Integer.MIN_VALUE/accumulator) {
                		throw new ArithmeticException("Integer Underflow Error");
                	}
                	//if underflows then throw error
                	if (accumulator < 0 && change > 0 && accumulator < Integer.MIN_VALUE/change) {
                		throw new ArithmeticException("Integer Underflow Error");
                	}
                	//if overflows then throw error
                	if (accumulator < 0 && change < 0 && change < Integer.MAX_VALUE/accumulator) {
                		throw new ArithmeticException("Integer Overflow Error");
                	}
                	//change accumulator
                    accumulator *= change;
                    break;
                //division
                case '/':
                	//only error is divide by 0, throw error if it will happen
                	if (change == 0) {
                		throw new ArithmeticException("Divide By 0 Error");
                	}
                	//change accumulator
                    accumulator /= change;
                    break;
                //exponent
                case '^':
                	//base equals accumulator because we have to multiply it by itself multiple times
                	int base = accumulator;
                	//if negative exponent
                	if (change < 0) {
                		//1 / (1 to any power) will always equal 1
                		if (accumulator == 1) {
                			break;
                		}
                		//1 / (-1 to any power) will equal 1 or negative one depending on if the power is odd or even
                		else if (accumulator == -1) {
                			//if absolute value of change is odd then it equates to -1
                			if ((change*-1) % 2 == 1) {
                				accumulator = -1;
                				break;
                			}
                			//if the absolute value of change is even then it equates to 1
                			else {
                				accumulator = 1;
                				break;
                			}
                		}
                		//a number to a negative exponent is 0 with integer division
                		else {
                			accumulator = 0;
                		}
                		break;
                	}
                	//anything to the 0 power is 1
                	if (change == 0) {
                		accumulator = 1;
                		break;
                	}
                	//calculate using normal exponents
                	for (int j = 1; j < change; j++) {
                		//same 4 cases as multiplying
                    	//if overflows then throw error
                    	if (accumulator > 0 && base > 0 && accumulator > Integer.MAX_VALUE/base) {
                    		throw new ArithmeticException("Integer Overflow Error");
                    	}
                    	//if underflows then throw error
                    	if (accumulator > 0 && base < 0 && base < Integer.MIN_VALUE/accumulator) {
                    		throw new ArithmeticException("Integer Underflow Error");
                    	}
                    	//if underflows then throw error
                    	if (accumulator < 0 && base > 0 && accumulator < Integer.MIN_VALUE/base) {
                    		throw new ArithmeticException("Integer Underflow Error");
                    	}
                    	//if overflows then throw error
                    	if (accumulator < 0 && base < 0 && base < Integer.MAX_VALUE/accumulator) {
                    		throw new ArithmeticException("Integer Overflow Error");
                    	}
                    	accumulator *= base;
                	}
                	break;
                //Modulus
                case '%':
                	//if not divide by 0 then Modulus change accumulator to base
                	if (change != 0) {
                		accumulator %= change;
                	}
                	//if divide by 0 answer is the accumulator
                	break;
                //factorial
                case '!':
                	//if factorial is negative then throw error
                	if (accumulator < 0) {
                		throw new ArithmeticException("Invalid Factorial Input");
                	}
                	//0 factorial is 1
                	if (accumulator == 0) {
                		accumulator = 1;
                	}
                	//positive factorial gets calculated
                	if (accumulator > 0) {
                		//multiply every number, from 1 below accumulator to 1
                		for (int j = accumulator-1; j > 0; j--) {
                			//because accumulator and j are always positive, 1 case. check overflow
                        	if (accumulator > Integer.MAX_VALUE/j) {
                        		throw new ArithmeticException("Integer Overflow Error");
                        	}
                        	//change accumulator
                			accumulator *= j;
                		}
                	}
                	break;
                }
                //push the result
                myStack.push(accumulator);
			}
			//current char is a number and not a space
			else if (currentChar != ' '){
				myStack.push(makeNumber(parsed[i]));
			}
		}
		//accumulator is the final result
		accumulator = myStack.pop();
		return accumulator;
	}
	
	//make a number from a string, throws error if number is too high
	private int makeNumber(String numString) throws ArithmeticException {
		//start at 0
		int accumulate = 0;
		//if the string thats being converted is negative
		if (numString.charAt(0) == '-') {
	        for (int i = 1; i < numString.length(); i++) {
	        	//if shift is an overflow throw error
	        	if (accumulate < Integer.MIN_VALUE/10) {
	        		throw new ArithmeticException("Integer Underflow Error");
	        	}
	        	//shift numbers 1 to the left in base 10
	            accumulate *= 10;
	            //if adding would overflow then throw error
	        	if (accumulate < Integer.MIN_VALUE+(numString.charAt(i)-'0')) {
	        		throw new ArithmeticException("Integer Underflow Error");
	        	}
	            //accumulate the current char
	            accumulate -= numString.charAt(i)-'0';
	        }
		}
		//the string is positive
		else {
			//for loop every character in string
	        for (int i = 0; i < numString.length(); i++) {
	        	//if shift is an overflow throw error
	        	if (accumulate > Integer.MAX_VALUE/10) {
	        		throw new ArithmeticException("Integer Overflow Error");
	        	}
	        	//shift numbers 1 to the left in base 10
	            accumulate *= 10;
	            //if adding would overflow then throw error
	        	if (accumulate > Integer.MAX_VALUE-(numString.charAt(i)-'0')) {
	        		throw new ArithmeticException("Integer Overflow Error");
	        	}
	            //accumulate the current char
	            accumulate += numString.charAt(i)-'0';
	        }
		}
        //return the correct value
		return accumulate;
	}
}
