package org.semanticweb.vlog4j.examples.core;

import java.util.*;
import java.util.regex.*;

public class QueueString{
    
    static class Expression{
        String first_operand, second_operand;
        char operator;
    }
    
    static Stack<Expression> result = new Stack<Expression>();

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
                        
                    if(expression.startsWith("(wdt")) {  
                        evaluate_expression(expression);
                    }
               }
          } 
            
       } 
        
       if (st.isEmpty()) 
         return true;
       else
         {   
             return false; 
         }  
    }  
    
    public static void evaluate_expression(String str){
        if(str.contains("*")){
             str = str.substring(0, str.length()-1);
             generate_xxx(str, "", '*');
        }
        
        str = str.substring(1, str.length() - 1);
        char[] ch = str.toCharArray();
        
        for(int i = 0; i < ch.length; i++){
            if(ch[i] == '+' || ch[i] == 'o' || ch[i] == '>'){
                generate_xxx(str.substring(0, i), str.substring(i+1, ch.length), ch[i]);
                break;
            }
        }
    }
    
    public static void generate_xxx(String F_OP,String S_OP, char op){
        Expression object = new Expression();
        object.first_operand = F_OP;
        object.second_operand = S_OP;
        object.operator = op;
        
        result.push(object);
    }
    
    public static void print_result(){
         for (int i = 0; i <= result.size() + 1; i++) {
             Expression obj = result.pop();
             System.out.println(obj.first_operand + "  " + obj.second_operand + "  " + obj.operator);
         }
    }
    
     public static void main(String []args){
        String str = "SELECT * WHERE {SERVICE wikibase:box { ?place wdt:P625 ?location .bd:serviceParam "
        		+ "wikibase:cornerWest \"Point(-121.872777777 37.304166666)\"^^geo:wktLiteral .bd:serviceParam wikibase:cornerEast \"Point(-121.486111111 38.575277777)\"^^geo:wktLiteral . }"
        		+ " ?place (wdt:p11o(wdt:p12+(wdt:14+wdt:13)))  wd:Q3914. }";
        char[] ch = str.toCharArray();
         
        System.out.println(areParenthesisBalanced(ch, str));
        print_result();
     }
}