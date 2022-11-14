package expressionParse;

import java.util.HashMap;

public class expressionEvaluation {
	
	//error codes for both classes
	private HashMap<Integer, String> errorCodes;
	
	//classes so our main method doesn't need to create 2 classes
	private parenthesisCheck paraCheck;
	private expressionCheck expCheck;
	
	//constructor
	public expressionEvaluation() {
		
		//create hashMap for all errors in both classes
		errorCodes = new HashMap<Integer, String>();
		
		//put all errors for both classes
		errorCodes.put(0, "^ incomplete expression");
		errorCodes.put(1, "^ expected (");
		errorCodes.put(2, "^ expected )");
		errorCodes.put(3, "^ expected {");
		errorCodes.put(4, "^ expected }");
		errorCodes.put(5, "^ expected [");
		errorCodes.put(6, "^ expected ]");
		errorCodes.put(7, "^ expected ");
		errorCodes.put(8, "^ expected operator");
		errorCodes.put(9, "^ expected integer");
		errorCodes.put(10, "^ no double digit number");
		errorCodes.put(11, "^ no letters");
		errorCodes.put(12, "^ expected <");
		errorCodes.put(13, "^ expected >");
		
		//create paranthesisCheck class with our errorCodes for memory saving
		paraCheck = new parenthesisCheck(errorCodes);
		
		//create expressionCheck class with our errorCodes for memory saving
		expCheck = new expressionCheck(errorCodes);
	}
	
	//the main method called to this class
	public boolean evaluateExpression(String myString) {
		//return paraCheck and expCheck anded, order is important, checkExpression needs correct parenthesis to work
		return paraCheck.valid(myString) && expCheck.checkExpression(myString);
	}
}
