package org.semanticweb.vlog4j.examples.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.vlog4j.core.model.api.PositiveLiteral;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.KnowledgeBase;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.implementation.VLogReasoner;
import org.semanticweb.vlog4j.examples.ExamplesUtils;
import org.semanticweb.vlog4j.parser.ParsingException;
import org.semanticweb.vlog4j.parser.RuleParser;


public class ParsingClass{
	final static Variable xVariable = Expressions.makeUniversalVariable("x");
	final static Variable yVariable = Expressions.makeUniversalVariable("y");
	final static Variable zVariable = Expressions.makeUniversalVariable("z");
	static StringBuilder rules = new StringBuilder(""); 
	static int counter = 0 ;
	static class PathExpression{
		String first_operand, second_operand;
		char operator;
	}

	static Stack<PathExpression> result = new Stack<PathExpression>();
	final static LinkedHashSet<Variable> queryVariables = new LinkedHashSet<>(Arrays.asList(xVariable,yVariable,zVariable));

	static class stack  
	{ 
		int top=-1; 
		char items[] = new char[100]; 

		void push(char x)  
		{ 
			if (top == 99)  
			{ 
				System.out.println("Stack full"); 
			}  
			else 
			{ 
				items[++top] = x; 
			} 
		} 

		char pop()  
		{ 
			if (top == -1)  
			{ 
				System.out.println("Underflow error"); 
				return '\0'; 
			}  
			else 
			{ 
				char element = items[top]; 
				top--; 
				return element; 
			} 
		} 

		boolean isEmpty()  
		{ 
			return (top == -1) ? true : false; 
		} 
	} 

	public static boolean isMatchingPair(char character1, char character2) 
	{ 
		if (character1 == '(' && character2 == ')') 
			return true; 
		else if (character1 == '{' && character2 == '}') 
			return true; 
		else if (character1 == '[' && character2 == ']') 
			return true; 
		else
			return false; 
	} 

	public static boolean areParenthesisBalanced(char exp[], String text) 
	{ 
		stack st = new stack(); 
		Stack<Integer> index = new Stack<Integer>();

		for(int i=0;i<exp.length;i++) 
		{ 
			if (exp[i] == '{' || exp[i] == '(' || exp[i] == '['){ 
				st.push(exp[i]); 
				index.push(i);
			}
			if (exp[i] == '}' || exp[i] == ')' || exp[i] == ']') 
			{ 
				if (st.isEmpty()) 
				{ 
					return false; 
				}  
				else if (!isMatchingPair(st.pop(), exp[i]) ) 
				{ 
					return false;
				} 

				else{
					int j =  index.pop();
					String expression = text.substring(j, i+1);

					if((i+2) < exp.length && exp[i+1] == '*')
						expression = text.substring(j, i+2);

					if(expression.startsWith("(")||expression.startsWith("((")) {  
						//System.out.println(expression);

						evaluate_expression(expression);
						// System.out.println("-----------");
						//  System.out.println("-----------");

					}
				}
			} 

		} 
		// System.out.println(expressionsHashmap);

		if (st.isEmpty()) 
			return true;
		else
		{   
			return false; 
		} 

	}  

	public static void evaluate_expression(String str){
		int bracketCounter =0;
		Pattern p = Pattern.compile("[^A-Za-z]");
		Matcher m = p.matcher(str);
		if(m.find()) {
			generate_xxx(str.replace("(", "").replace(")", ""), "", '$'); 
		}
		if(str.contains("*")){
			str = str.substring(0, str.length()-1);
			generate_xxx(str, "", '*');           
		}
		str = str.substring(1, str.length() - 1);
		char[] ch = str.toCharArray();

		for(int i = 0; i < ch.length; i++){
			if(ch[i]== '(') {
				bracketCounter++;
			}
			if(ch[i]== ')') {
				bracketCounter--;
			}
			if(ch[i] == '+' && bracketCounter<1){
				generate_xxx(str.substring(0, i), str.substring(i+1, ch.length), ch[i]);
				break;
			}
			if(ch[i] == '/' && bracketCounter<1){
				generate_xxx(str.substring(0, i), str.substring(i+1, ch.length), ch[i]);
				break;
			}
			if(str.contains("*")&& bracketCounter==0){
				str = str.substring(1, str.length()-1);
				generate_xxx(str, "", '*');           
			}
			
		}
	}

	public static void generateDatalogRules() {
		HashMap<String, String> hashmap = new HashMap<String, String>();
		
		Pattern p = Pattern.compile("[^A-Za-z0-9:]");
		//System.out.println(result.size());
		for (int k=0;k < result.size() ; k++) {

			PathExpression obj = result.elementAt(k);
			Matcher m = p.matcher(obj.first_operand);
			if(!m.find()) {
				final Predicate queryPredicatea1 = Expressions.makePredicate(obj.first_operand, 2);
				//final Predicate queryPredicatea2 = Expressions.makePredicate(str.substring(i+1, ch.length), 2);

				final PositiveLiteral Q_A1 = Expressions.makePositiveLiteral(queryPredicatea1,xVariable, yVariable);
				//final PositiveLiteral Q_A2 = Expressions.makePositiveLiteral(queryPredicatea2,xVariable, yVariable);
				if(!hashmap.containsKey(obj.first_operand))
					hashmap.put(obj.first_operand, "Pred"+counter++);
				final Predicate queryPredicateQM = Expressions.makePredicate(hashmap.get(obj.first_operand),2);
				//final Predicate queryPredicateQN = Expressions.makePredicate(hashmap.get(str.substring(i+1, ch.length)), 2);

				final PositiveLiteral Q_M = Expressions.makePositiveLiteral(queryPredicateQM,xVariable, yVariable);
				//final PositiveLiteral Q_N = Expressions.makePositiveLiteral(queryPredicateQN,xVariable, yVariable);


				Rule rule3 = Expressions.makeRule(Q_M, Q_A1);
				rules.append("\n"+rule3.toString());
				//   Rule rule4 = Expressions.makeRule(Q_N, Q_A2);
				//	rules.append("\n"+rule4.toString());
			}

			Matcher m1 = p.matcher(obj.second_operand);
			if(!m1.find()&& !obj.second_operand.isEmpty()) {
				final Predicate queryPredicatea1 = Expressions.makePredicate(obj.second_operand, 2);
				//final Predicate queryPredicatea2 = Expressions.makePredicate(str.substring(i+1, ch.length), 2);

				final PositiveLiteral Q_A1 = Expressions.makePositiveLiteral(queryPredicatea1,xVariable, yVariable);
				//final PositiveLiteral Q_A2 = Expressions.makePositiveLiteral(queryPredicatea2,xVariable, yVariable);
				if(!hashmap.containsKey(obj.second_operand))
					hashmap.put(obj.second_operand, "Pred"+counter++);
				final Predicate queryPredicateQM = Expressions.makePredicate(hashmap.get(obj.second_operand),2);
				//final Predicate queryPredicateQN = Expressions.makePredicate(hashmap.get(str.substring(i+1, ch.length)), 2);

				final PositiveLiteral Q_M = Expressions.makePositiveLiteral(queryPredicateQM,xVariable, yVariable);
				//final PositiveLiteral Q_N = Expressions.makePositiveLiteral(queryPredicateQN,xVariable, yVariable);


				Rule rule3 = Expressions.makeRule(Q_M, Q_A1);
				rules.append("\n"+rule3.toString());
				//   Rule rule4 = Expressions.makeRule(Q_N, Q_A2);
				//	rules.append("\n"+rule4.toString());
			}


			switch(obj.operator) {

			//for the case *
			case '*':
				String fullExpression1 = "("+obj.first_operand+"*)";

				if(!hashmap.containsKey(fullExpression1))
					hashmap.put(fullExpression1, "Pred"+counter++);

				Predicate queryPredicateQE1 = Expressions.makePredicate(hashmap.get(fullExpression1), 2);	
				//  final PositiveLiteral Q_E1 = Expressions.makePositiveLiteral(queryPredicateQE1,xVariable, xVariable);

				//  Rule rule0 = Expressions.makeRule(Q_E1);
				//  System.out.println("RULE : "+rule0.toString());

				if(!hashmap.containsKey(obj.first_operand))
					hashmap.put(obj.first_operand, "Pred"+counter++);


				//Generate rules 
				final Predicate queryPredicateQX = Expressions.makePredicate(hashmap.get(obj.first_operand), 2);

				final PositiveLiteral Q_X = Expressions.makePositiveLiteral(queryPredicateQX,yVariable, zVariable);
				final PositiveLiteral Q_E12 = Expressions.makePositiveLiteral(queryPredicateQE1,xVariable, yVariable);
				final PositiveLiteral Q_E11 = Expressions.makePositiveLiteral(queryPredicateQE1,xVariable, zVariable);
				final PositiveLiteral Q_E111 = Expressions.makePositiveLiteral(queryPredicateQX,xVariable, yVariable);

				Rule rule = Expressions.makeRule(Q_E11,Q_E12,Q_X);
				rules.append("\n"+rule.toString());
				Rule rule11 = Expressions.makeRule(Q_E12,Q_E111);
				rules.append("\n"+rule11.toString());
				break; 


				// for the case +
			case '+':
				String fullExpression2 = "("+obj.first_operand+ obj.operator+ obj.second_operand+")";

				if(!hashmap.containsKey(fullExpression2))
					hashmap.put(fullExpression2, "Pred"+counter++);

				Predicate queryPredicateQE2 = Expressions.makePredicate(hashmap.get(fullExpression2), 2);	
				PositiveLiteral Q_E2 = Expressions.makePositiveLiteral(queryPredicateQE2, xVariable,yVariable);

				if(!hashmap.containsKey(obj.first_operand))
					hashmap.put(obj.first_operand, "Pred"+counter++);
				if(!hashmap.containsKey(obj.second_operand))
					hashmap.put(obj.second_operand, "Pred"+counter++);

				//Generate rules 
				final Predicate queryPredicateQM = Expressions.makePredicate(hashmap.get(obj.first_operand), 2);
				final Predicate queryPredicateQN = Expressions.makePredicate(hashmap.get(obj.second_operand), 2);

				final PositiveLiteral Q_M = Expressions.makePositiveLiteral(queryPredicateQM,xVariable, yVariable);
				final PositiveLiteral Q_N = Expressions.makePositiveLiteral(queryPredicateQN,xVariable, yVariable);

				Rule rule1 = Expressions.makeRule(Q_E2, Q_M);
				rules.append("\n"+rule1.toString());
				Rule rule2 = Expressions.makeRule(Q_E2, Q_N);
				rules.append("\n"+rule2.toString());

		

				break;


				// for the case /	 
			case '/':

				String fullExpression3 = "("+obj.first_operand+ obj.operator+ obj.second_operand+")";

				if(!hashmap.containsKey(fullExpression3))
					hashmap.put(fullExpression3, "Pred"+counter++);

				Predicate queryPredicateQE3 = Expressions.makePredicate(hashmap.get(fullExpression3), 2);	
				final PositiveLiteral Q_E3 = Expressions.makePositiveLiteral(queryPredicateQE3,xVariable, zVariable);
				if(!hashmap.containsKey(obj.first_operand))
					hashmap.put(obj.first_operand, "Pred"+counter++);
				if(!hashmap.containsKey(obj.second_operand))
					hashmap.put(obj.second_operand, "Pred"+counter++);

				//Generate rules 
				final Predicate queryPredicateQA = Expressions.makePredicate(hashmap.get(obj.first_operand), 2);
				final Predicate queryPredicateQB = Expressions.makePredicate(hashmap.get(obj.second_operand), 2);

				final PositiveLiteral Q_A = Expressions.makePositiveLiteral(queryPredicateQA,xVariable, yVariable);
				final PositiveLiteral Q_B = Expressions.makePositiveLiteral(queryPredicateQB,yVariable, zVariable);				

				Rule rule5 = Expressions.makeRule(Q_E3,Q_A,Q_B);                  
				rules.append("\n"+rule5.toString());
			

				break;   
				
				// for single predicate
			case'$':
				String fullExpression4 = obj.first_operand;
				if(!hashmap.containsKey(fullExpression4))
					hashmap.put(fullExpression4, "Pred"+counter++);
				
				Predicate queryPredicateQE4 = Expressions.makePredicate(hashmap.get(fullExpression4), 2);	
				PositiveLiteral Q_E4 = Expressions.makePositiveLiteral(queryPredicateQE4, xVariable,yVariable);

				if(!hashmap.containsKey(obj.first_operand))
					hashmap.put(obj.first_operand, "Pred"+counter++);
				
				//Generate rules 
				final Predicate queryPredicateQD = Expressions.makePredicate(hashmap.get(obj.first_operand), 2);
				
				final PositiveLiteral Q_D = Expressions.makePositiveLiteral(queryPredicateQD,xVariable, yVariable);
				
				Rule rule4 = Expressions.makeRule(Q_E4, Q_D);
				rules.append("\n"+rule4.toString());
				break;
			}       
		}
		//System.out.println(hashmap);
		hashmap.clear();
	}

	public static void generate_xxx(String F_OP,String S_OP, char op){
		PathExpression object = new PathExpression();
		object.first_operand = F_OP;
		object.second_operand = S_OP;
		object.operator = op;

		result.push(object);

	}
	static boolean findFile(Path targetDir, String fileName) throws IOException {
		return Files.list(targetDir).filter( (p) -> {
			if (Files.isRegularFile(p)) {
				return p.getFileName().toString().equals(fileName);
			} else {
				return false;
			}
		}).findFirst().orElse(null) != null;
	}
	public static void main(String []args) throws MalformedURLException{

		int resultCount=0;
		ExamplesUtils.configureLogging(); 
		//read query from file 
		
		String homeFolder = "/Users/vishwanathlhugar/Desktop/Project/script.txt";
		File outputfiles = new File("/Users/vishwanathlhugar/Desktop/Project/outputFiles/");
		File script = new File(homeFolder);
		List<String> allLines;
		try {
			allLines = Files.readAllLines(Paths.get(script.getPath()));
			String[] predicates = null;
			for (String line : allLines) {

				String[] eachLine= line.split(",");
				for (String Line : eachLine) {
				
			 predicates= Line.split(" ");
				String s = predicates[1].replace("+", " ").replace("/", " ").replace("*", "").replace("(", "").replace(")", "");
				String[] allPred=s.split(" ");
				// String str =  "?x (isMarriedTo+livesIn) Venice";

				for (String predName : allPred) {
					File[] listOfFiles = outputfiles.listFiles();
					for (File file : listOfFiles) {
						if (file.getName().equals(predName+".csv")){	
							//System.out.println("Found:"+file.getName());
							final String initialFactsHasPart = ""// a file input:
									+ "@source "+predName+"[2] : load-csv(\"" + file.getAbsolutePath()+"\") .";											
							rules.append(initialFactsHasPart);
						}
					}
				}
				}
				char[] ch = line.toCharArray();
				areParenthesisBalanced(ch, line);
				
				generateDatalogRules();

			rules.append("Pred"+--counter+"(?x,?z) :- "+"Pred"+counter+"(?x,?y), "+"Pred"+--counter+"(?y, ?z).");
				//System.out.println("All rules:"+rules);

				//counter++;
				//counter++;
				//counter++; 

				// use simple logger for the example

				KnowledgeBase kb = null;
				try {

					kb = RuleParser.parse(rules.toString());

				} catch (final ParsingException e) {
					System.out.println("Failed to parse rules: " + e.getMessage());
					return; 
				}
				//System.out.println(kb.getRules());

				long startTime = System.nanoTime(); //start time of the query
				try (final Reasoner reasoner = new VLogReasoner(kb)) {
					
					reasoner.reason();
					long endTime   = System.nanoTime(); // endtime of the task
					// Execute some queries 	
					ExamplesUtils.printOutQueryAnswers("Pred"+ ++counter+"(?a, "+ predicates[2]+")", reasoner);
					//resultCount = ExamplesUtils.getQueryAnswerCount("Pred"+--counter+"(?Y, "+ predicates[2]+")", reasoner);
					
					
					rules.setLength(0);
					result.clear();
					counter=0;
					//NumberFormat formatter = new DecimalFormat("#0.00000");
					System.out.print("Execution time is " + ((endTime - startTime) /1000000) + " milliseconds");
					System.out.print("\n-----------------------------------------------------------------------\n");
					//System.out.println("Result count:"+resultCount);
					resultCount=0;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} 
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}