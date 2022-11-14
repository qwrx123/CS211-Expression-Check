package expressionParse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;


public class parenthesisCheck{
	
	//map of all parenthesis parings
	private HashMap<Character, Character> myMap = new HashMap<Character, Character>();
	//stack for making sure the parentesis pairs up
	private Stack<Character> myStack = new Stack<Character>();
	//a set for the values of the map for speed optimization
	private HashSet<Character> valueSet;
	//error codes for when something goes wrong
	private HashMap<Integer, String> errorCodes;
	
	public parenthesisCheck() {
		//if no errors given, use default errors
		this(createDefaultError());
	}
	

	public parenthesisCheck(HashMap<Integer, String> errorCodes) {
		this.errorCodes = errorCodes;
		//insert all parenthesis parings
		myMap.put('(', ')');
		myMap.put('{', '}');
		myMap.put('[', ']');
		myMap.put('<', '>');
		
		//create HashSet of all values for optimization
		valueSet = new HashSet<Character>(myMap.values());
	}
	
	private static HashMap<Integer, String> createDefaultError() {
		//initialize error HashMap
		HashMap<Integer, String> defaultHashMap = new HashMap<Integer, String>();
		
		//add all default errors
		defaultHashMap.put(0, "^ incomplete expression");
		defaultHashMap.put(1, "^ expected (");
		defaultHashMap.put(2, "^ expected )");
		defaultHashMap.put(3, "^ expected {");
		defaultHashMap.put(4, "^ expected }");
		defaultHashMap.put(5, "^ expected [");
		defaultHashMap.put(6, "^ expected ]");
		defaultHashMap.put(12, "^ expected <");
		defaultHashMap.put(13, "^ expected >");
		defaultHashMap.put(7, "^ expected ");
		
		//return the default errors
		return defaultHashMap;
	}
	public boolean valid(String myExpression) {
		//loop through the string making sure all pairs work
		for (int i = 0; i < myExpression.length(); i++) {
			//use myChar for everything that needs to see a char
			char myChar = myExpression.charAt(i);
			//make sure char is part of the parings
			if (myMap.containsKey(myChar)) {
				myStack.push(myChar);
			}
			else {
				//make sure char is part of the parings
				if(valueSet.contains(myChar)) {
					//if stack is empty there are too many right parenthesis
	                if (myStack.empty()) {
	                	//add number of spaces to reach where we are in the string - 1
	                	for (int j = 0; j < i + 9; j++) {
	                		System.out.print(" ");
	                	}
	                	//if right parenthesis then expect left parenthesis
	                	if (myChar == ')') {
	        				System.out.println(errorCodes.get(1));
	                	}
	                	//if right curly brackets then expect left curly brackets
	                	if (myChar == '}') {
	        				System.out.println(errorCodes.get(3));
	                	}
	                	//if right square brackets then expect left square brackets
	                	if (myChar == ']') {
	                		System.out.println(errorCodes.get(5));
	                	}
	                	//if right angle brackets then expect left angle brackets
	                	if (myChar == '>') {
	                		System.out.println(errorCodes.get(12));
	                	}
	                    return false;
	                }
	                //if brackets are mismatched then expression is false
	                if (myChar != myMap.get(myStack.peek())) {
	                	//add number of spaces to reach where we are -1
	                	for (int j = 0; j < i + 9; j++) {
	                		System.out.print(" ");
	                	}
	                	//get incomplete error then add the matching parenthesis to the end
	                	System.out.println(errorCodes.get(7) + myMap.get(myStack.peek()));
	                	//clear the stack for next use
	                	myStack.clear();
	                    return false;
	                }
	                myStack.pop();
				}
			}
		}
		//if stack is not empty then return false, too many left brackets
		if (!myStack.empty()) {
			//add number of spaces as characters in string
        	for (int i = 0; i < myExpression.length(); i++) {
        		System.out.print(" ");
        	}
        	//add incomplete error then add matching parenthesis
        	System.out.println(errorCodes.get(7) + myMap.get(myStack.peek()));
        	//clear stack for next use
			myStack.clear();
			return false;
		}
		return true;
	}
	

}