package expressionParse;

import java.util.Scanner;

public class expressionParse {
	static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		//make new object to initialize prefix object variables
		preFix myFix = new preFix();
		//make new object to initialize postFix object variables
		postFix myPostFix = new postFix();
		//make new object to initialize expressionEvaluation object that will initialize parenthesis check and expression check 
		expressionEvaluation myEval = new expressionEvaluation();
		//variable to see if user wants to continue
		boolean userContinue = true;
		do {
			//get user input
			System.out.println("Welcome to CS211 Expression Parser");
			System.out.println("Allowed inputs are Numbers, +, -, *, /, %, ^, !, (), {}, [], <>");
			System.out.print("Please enter a mathmatical expression: ");
			String myExpression = input.nextLine();
			//remove spaces so i don't have to work around them
			myExpression = myExpression.replaceAll(" ", "");
			//give user expression
			System.out.println("Infix:   " + myExpression);
			//make sure user inputs something and make sure the expression is valid
			while (myExpression.length() == 0 || !myEval.evaluateExpression(myExpression)) {
				//if length is equal to 0 then tell them
				if (myExpression.length() == 0) {
					System.out.print("Your expression doesnt exist, Please re-enter a mathmatical expression: ");
				}
				//if length is not equal to 0 then the expression was incorrect
				else {
					System.out.print("Your expression was incorrect, please reinput expression: ");
				}
				//get user input
				myExpression = input.nextLine();
				//remove all spaces
				myExpression = myExpression.replaceAll(" ", "");
				//give user expression
				System.out.println("Infix:   " + myExpression);
			}
			
			//turn expression into postFix expression
			myExpression = myFix.postFix(myExpression);
			//give user good looking posFix expression, the one without spaces
			System.out.println("PostFix: " + myExpression.replaceAll(" ", ""));
			//turn expression into an array for easy finding of multiple digit characters
			String[] expressionArray = myExpression.split(" ");
			
			//if expression overflows underflows or gets divided by 0 then throws an error
			try {
				//print out value of the expression
				System.out.println("Result:  " + myPostFix.evaluate(expressionArray));
			} 
			catch (ArithmeticException e) {
				//if there is an error then give the message what happened
				System.out.println(e.getMessage());
				System.out.println("Sorry there was an issue with evaulating your expression,");
			}
			boolean validMessage = false;
			System.out.println("Would you like to calculate another expression");
			System.out.println("\"Yes\" and \"Y\" for yes and \"No\" and \"N\" for No");
			while (!validMessage) {
				String userInput = input.next();
				if (userInput.toLowerCase().equals("yes") || userInput.toLowerCase().equals("y")) {
					System.out.println("Lets continue");
					validMessage = true;
				}
				else if (userInput.toLowerCase().equals("no") || userInput.toLowerCase().equals("n")) {
					System.out.println("Bye");
					validMessage = true;
					userContinue = false;
				}
				else {
					System.out.println("Please add a valid message");
				}
			}
		} while (userContinue);
	}
}
