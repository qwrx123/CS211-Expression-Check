package expressionParse;

import java.util.HashMap;
import java.util.HashSet;

public class expressionCheck {
	
	//error codes for when a error happens
	private HashMap<Integer, String> errorCodes;
	
	//hashSets for grouping up like characters for clean code
	private HashSet<Character> leftBrackets;
	private HashSet<Character> rightBrackets;
	private HashSet<Character> operators;
	private HashSet<Character> numbers;
	
	//how much to shift when error happens
	private static final int shiftError = 9;
	
	public expressionCheck() {
		//if no error codes specified, create defaults
		this(createDefaultError());
	}
	
	public expressionCheck(HashMap<Integer, String> errorCodes) {
		//set errorCodes to errorCodes, saves memory if using supplied errorCodes
		this.errorCodes = errorCodes;
		
		//create a hashSet for operators
		operators = new HashSet<Character>();
		//add operators to hashSet
		operators.add('+');
		operators.add('-');
		operators.add('*');
		operators.add('/');
		operators.add('^');
		operators.add('!');
		operators.add('%');
		
		//create hashSet for characters
		numbers = new HashSet<Character>();
		//less lines to add numbers this way
		for (char i = '0'; i <= '9'; i++) {
			numbers.add(i);
		}
		
		//make hashSet for left brackets
		leftBrackets = new HashSet<Character>();
		//add all left brackets
		leftBrackets.add('(');
		leftBrackets.add('{');
		leftBrackets.add('[');
		leftBrackets.add('<');
		//make hashSet for right brackets
		rightBrackets = new HashSet<Character>();
		//ass all right brackets
		rightBrackets.add(')');
		rightBrackets.add('}');
		rightBrackets.add(']');
		rightBrackets.add('>');
	}
	
	private static HashMap<Integer, String> createDefaultError() {
		//make new hashMap for default errors
		HashMap<Integer, String> defaultHashMap = new HashMap<Integer, String>();
		
		//make errors as necessary, skips due to those errors used by the other class
		defaultHashMap.put(0, "^ incomplete expression");
		defaultHashMap.put(7, "^ expected ");
		defaultHashMap.put(8, "^ expected operator");
		defaultHashMap.put(9, "^ expected integer");
		defaultHashMap.put(10, "^ no double digit number");
		defaultHashMap.put(11, "^ no letters");
		
		return defaultHashMap;
	}
	
	//print error function for reducing lines of code
	private void printError(int location, int error) {
		for (int i = 0; i < location + shiftError; i++) {
			System.out.print(" ");
		}
		System.out.println(errorCodes.get(error));
	}
	
	
	//the main method called to this class
	public boolean checkExpression(String evalCheck) {
		evalCheck = evalCheck.replaceAll(" ", "");
		
		
		//if length == 1 check for number otherwise give false
		if (evalCheck.length() == 1) {
			//if character is a number then expression is true
			if (evalCheck.charAt(0) >= '0' && evalCheck.charAt(0)<= '9') {
				return true;
			}
			//if character is not a number then we expect a number and expression is false
			else {
				printError(0, 9);
				return false;
			}
		}
		
		//create char variables for looking at the current and next char
		char currentChar = evalCheck.charAt(0);
		char nextChar = evalCheck.charAt(1);
		
		//if first character is operator, expression is false
		if (operators.contains(currentChar)) {
			if (currentChar != '-') {
				printError(0, 9);
				return false;
			}
		}

		//if first character is a letter then expression is false
		if (!numbers.contains(currentChar) && !operators.contains(currentChar) &&  !leftBrackets.contains(currentChar) && !rightBrackets.contains(currentChar)) {
			printError(0, 11);
			return false;
		}
		//start at 1 end at one less then the end because we check before and after
		for (int i = 0; i < evalCheck.length() - 1; i++) {
			//chars are now in the correct places
			currentChar = evalCheck.charAt(i);
			nextChar = evalCheck.charAt(i+1);
			
			//if we find an operator
			if (operators.contains(currentChar)) {
				//if current char is not the factorial operator
				if (currentChar != '!') {
					//if the next char is an operator that is not minus
					if (operators.contains(nextChar) && nextChar != '-') {
						printError(i+1, 9);
						return false;
					}
					//right bracket after an operator is false
					if (rightBrackets.contains(nextChar)) {
						printError(i+1, 9);
						return false;
					}
				}
				//current char is the factorial operator
				else {
					//a number after factorial is false
					if (numbers.contains(nextChar)) {
						printError(i+1, 8);
						return false;
					}
					//left bracket after a factorial false
					if(leftBrackets.contains(nextChar)) {
						printError(i+1, 8);
						return false;
					}
				}
			}
			//if we find a left bracket
			if (leftBrackets.contains(currentChar)) {
				//if right bracket follows left bracket then expression is false
				if(rightBrackets.contains(nextChar)) {
					printError(i+1, 9);
					return false;
				}
				//if operator comes after left bracket expression is false
				if (nextChar != '-') {
					if (operators.contains(nextChar)) {
						printError(i+1, 9);
						return false;
					}
				}
			}
			//if we find a right bracket
			if (rightBrackets.contains(currentChar)) {
				//if a number comes after a right bracket then expression is false
				if (numbers.contains(nextChar)) {
					printError(i+1, 8);
					return false;
				}
				//if left bracket comes after a right bracket then expression is false
				if(leftBrackets.contains(nextChar)) {
					printError(i+1, 8);
					return false;
				}
			}
			//if we find a number
			if (numbers.contains(currentChar)) {
				//if left bracket comes directly after a number then expression is false
				if (leftBrackets.contains(nextChar)) {
					printError(i+1, 8);
					return false;
				}
			}
			//if we find a letter then expression is false
			if (!numbers.contains(currentChar) && !operators.contains(currentChar) &&  !leftBrackets.contains(currentChar) && !rightBrackets.contains(currentChar)) {
				printError(i, 11);
				return false;
			}
		}
		
		//get the last char in the string
		currentChar = evalCheck.charAt(evalCheck.length()-1);

		//if last character is an operator then expression is false unless its a factorial
		if (operators.contains(currentChar) && currentChar != '!') {
			printError(evalCheck.length(), 9);
			return false;
		}
		//if last character is a letter then expression is false
		if (!numbers.contains(currentChar) && !operators.contains(currentChar) &&  !leftBrackets.contains(currentChar) && !rightBrackets.contains(currentChar)) {
			printError(evalCheck.length()-1, 9);
			return false;
		}
		return true;
	}
}
