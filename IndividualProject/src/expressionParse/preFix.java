package expressionParse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class preFix {
	//stack for operators to get correctly used
	private Stack<Character> myStack;
	//set for all left brackets
	private HashSet<Character> valueSet;
	//operator map and all priorities
	private HashMap<Character, Integer> operatorMap;
	//map for all brackets pairing to correct values
	private HashMap<Character, Character> pareMap;
	
	public preFix() {
		//Initialize stack
		myStack = new Stack<Character>();
		
		//Initialize hashMap
		pareMap = new HashMap<Character, Character>();
		//insert all the pairings
		pareMap.put(')', '(');
		pareMap.put('}', '{');
		pareMap.put(']', '[');
		pareMap.put('>', '<');
		
		//create a HashSet of values for optimization of for loop
		valueSet = new HashSet<Character>(pareMap.values());
		
		//Initialize other HashMap
		operatorMap = new HashMap<Character, Integer>();
		//create operator priorities
		operatorMap.put('+', 1);
		operatorMap.put('-', 1);
		operatorMap.put('*', 2);
		operatorMap.put('/', 2);
		operatorMap.put('%', 2);
		operatorMap.put('^', 3);
		operatorMap.put('!', 3);
		operatorMap.put('(', 0);
		operatorMap.put('{', 0);
		operatorMap.put('[', 0);
		operatorMap.put('<', 0);
	}
	
	public String postFix(String parsed) {
		//make multiple digit numbers work
		boolean prevNum = true;
		//
		boolean unary = false;
		//create the return string
		String returnString = new String();
		//unary minus at beginning
		if (parsed.charAt(0) == '-') {
			//if after a unary negative is a left bracket then use non sticky minus
			if (valueSet.contains(parsed.charAt(1))) {
				returnString += "0";
			}
			//Negative is a sticky negative
			else {
				prevNum = true;
				unary = true;
			}
		}
		//go through string one character at a time
		for (int i = 0; i < parsed.length(); i++) {
			//current character
			char myChar = parsed.charAt(i);
			//if currant character is left parentheses or like characters
			if (valueSet.contains(myChar)) {
				myStack.push(myChar);
				//when operator next to left bracket, adds 0 so not unary
				unary = false;
			}
			//if currant character is right parentheses or like characters
			else if (pareMap.containsKey(myChar)) {
				//pop all operators between matching brackets
				while (myStack.peek() != pareMap.get(myChar)) {
					returnString += " " + myStack.pop();
				}
				//remove matching bracket
				myStack.pop();
				//bracket isn't a number
				unary = false;

			}
			//if currant character is an operator, unary negatives act like numbers
			else if (operatorMap.containsKey(myChar) && unary != true) {
				//if the previous number was some sort of operator
				if (prevNum == false && myChar == '-') {
					
					if (valueSet.contains(parsed.charAt(i+1))) {
						returnString += " 0";
					}
					else {
						returnString += " -";
						prevNum = true;
						unary = true;
						continue;
					}
 				}
				//while the stack has data and the top of the stack has higher precedence then pop values into string
				while (!myStack.empty() && operatorMap.get(myStack.peek()) >= operatorMap.get(myChar)) {
					//add spaces between operators to make double digits easier
					returnString += " " + myStack.pop();
				}
				//push operator to stack
				myStack.push(myChar);
				//operator isn't a number
				prevNum = false;
				//if another operator, unary is true
				if (myChar != '!') {
					unary = true;
				}
				//Factorial should act like numbers to operators
				else {
					prevNum = true;
				}
			}
			//current character is a number or a unary -
			else {
				//if the previous character wasn't a number then add space to separate values
				if (prevNum == false) {
					returnString += " ";
				}
				//adds number to end of string, if a number is continuous this will be too
				returnString += myChar;
				//this is a number
				prevNum = true;
				//character doesn't make unary operator
				unary = false;
			}
		}
		//pop all remaining values of the stack to return string
		while (!myStack.empty()) {
			returnString += " " + myStack.pop();
		}
		return returnString;
	}
	
	
}
