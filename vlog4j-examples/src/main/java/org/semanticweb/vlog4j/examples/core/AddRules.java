package org.semanticweb.vlog4j.examples.core;

/*-
 * #%L
 * VLog4j Examples
 * %%
 * Copyright (C) 2018 VLog4j Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.semanticweb.vlog4j.core.model.api.Conjunction;
import org.semanticweb.vlog4j.core.model.api.DataSource;
import org.semanticweb.vlog4j.core.model.api.PositiveLiteral;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Term;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.DataSourceDeclarationImpl;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.KnowledgeBase;
import org.semanticweb.vlog4j.core.reasoner.QueryResultIterator;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.implementation.SparqlQueryResultDataSource;
import org.semanticweb.vlog4j.examples.ExamplesUtils;

/**
 * This is a simple example of adding data from the result of a SPARQL query on
 * a remote database endpoint, using {@link SparqlQueryResultDataSource}. In
 * this example, we will query Wikidata for titles of publications that have
 * authors who have children together.
 * 
 * @author Irina Dragoste
 *
 */
public class AddRules {

	/**
	 * <a href="https://www.wikidata.org/wiki/Property:P50"> WikiData author
	 * property id.</a>
	 */
	private static final String WIKIDATA_AUTHOR_PROPERTY = "wdt:P50";
	/**
	 * <a href="https://www.wikidata.org/wiki/Property:P1476"> WikiData title
	 * property id.</a> Published title of a work, such as a newspaper article, a
	 * literary work, a website, or a performance work
	 */
	private static final String WIKIDATA_TITLE_PROPERTY = "wdt:P1476";
	/**
	 * <a href="https://www.wikidata.org/wiki/Property:P25"> WikiData mother
	 * property id.</a>
	 */
	private static final String WIKIDATA_MOTHER_PROPERTY = "wdt:P25";
	/**
	 * <a href="https://www.wikidata.org/wiki/Property:P22"> WikiData father
	 * property id.</a>
	 */
	private static final String WIKIDATA_FATHER_PROPERTY = "wdt:P22";

	public static void main(final String[] args) throws IOException {

		ExamplesUtils.configureLogging();

		/*
		 * The WikiData SPARQL query endpoint.
		 */
		final URL wikidataSparqlEndpoint = new URL("https://query.wikidata.org/sparql");

		/*
		 * SPARQL query body that looks for publications where two authors of the
		 * publication are the mother, respectively father of the same child.
		 */
		final String queryBody = " ?publication " + WIKIDATA_TITLE_PROPERTY + " ?title ." + "?publication "
				+ WIKIDATA_AUTHOR_PROPERTY + " ?mother ." + " ?publication " + WIKIDATA_AUTHOR_PROPERTY + " ?father ."
				+ " ?child " + WIKIDATA_MOTHER_PROPERTY + " ?mother ." + " ?child " + WIKIDATA_FATHER_PROPERTY
				+ " ?father .";


		//Vidya
		String s ="(a o(d+(b+C)))";
		ArrayList<Integer> bracketArray = new ArrayList<Integer>();
		ArrayList<String> expressionArray = new ArrayList<String>();

		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i)== '(' || s.charAt(i)== ')') {
				bracketArray.add(i);
			}
		}
		System.out.println(bracketArray);

		//contains all nested expressions
		for (int i=0; i<bracketArray.size()/2; i++) 
			expressionArray.add(s.substring(bracketArray.get(i)+1,bracketArray.get((bracketArray.size()-i-1)))); 

		System.out.println("Expression array "+expressionArray);
		//traverse in reverse direction
		for (int j = expressionArray.size() - 1; j >= 0; j--){
			//atomic case
			//check for last element
			//if(j == expressionArray.size() - 1) {
		
			final Variable xVariable = Expressions.makeUniversalVariable("x");
			final Variable yVariable = Expressions.makeUniversalVariable("y");
			final Variable zVariable = Expressions.makeUniversalVariable("z");
			
			final Predicate queryPredicateQM = Expressions.makePredicate("Q_M_"+j, 2);
			final Predicate queryPredicateQN = Expressions.makePredicate("Q_N_"+j, 2);

			final PositiveLiteral Q_M = Expressions.makePositiveLiteral(queryPredicateQM,xVariable, yVariable);
			final PositiveLiteral Q_N = Expressions.makePositiveLiteral(queryPredicateQN,xVariable, yVariable);
			final PositiveLiteral M = Expressions.makePositiveLiteral("M_"+j, xVariable,yVariable);
			final PositiveLiteral N = Expressions.makePositiveLiteral("N_"+j, xVariable,yVariable);

			final Rule rule3 = Expressions.makeRule(Q_M, M);
			final Rule rule4 = Expressions.makeRule(Q_N, N);
		//	}
			//case "+"
			if(expressionArray.get(j).contains("+")) {

				
				final Predicate queryPredicateQE = Expressions.makePredicate("Q_E_"+j, 2);
				
				final PositiveLiteral Q_E = Expressions.makePositiveLiteral(queryPredicateQE, xVariable,yVariable);
				
				final Rule rule1 = Expressions.makeRule(Q_E, Q_M);
				final Rule rule2 = Expressions.makeRule(Q_E, Q_N);
				System.out.println("RULE : "+rule2.toString());
				
			}
			
			//case "o"
			if(expressionArray.get(j).contains("o")) {

				final Predicate queryPredicateQE = Expressions.makePredicate("Q_E_"+j, 2);
				
				final PositiveLiteral Q_E = Expressions.makePositiveLiteral(queryPredicateQE, xVariable,zVariable);
				final PositiveLiteral Q_A = Expressions.makePositiveLiteral(queryPredicateQM,xVariable, yVariable);
				final PositiveLiteral Q_B = Expressions.makePositiveLiteral(queryPredicateQN,yVariable, zVariable);
				
				final Conjunction<PositiveLiteral> ruleHeadConjunction = Expressions
						.makePositiveConjunction(Q_A, Q_B);

			}
			//case "*"
			if(expressionArray.get(j).contains("*")) {
				
				final Predicate queryPredicateQE = Expressions.makePredicate("Q_E_"+j, 2);
				final Predicate queryPredicateQE2 = Expressions.makePredicate("Q_E2_"+j, 2);
				
				final PositiveLiteral Q_E = Expressions.makePositiveLiteral(queryPredicateQE, xVariable,xVariable);
				final PositiveLiteral Q_Ex = Expressions.makePositiveLiteral(queryPredicateQE, xVariable,zVariable);
				final PositiveLiteral Q_Ex1 = Expressions.makePositiveLiteral(queryPredicateQE, xVariable,yVariable);
				final PositiveLiteral Q_Ex2 = Expressions.makePositiveLiteral(queryPredicateQE2, yVariable,zVariable);
				
				final Conjunction<PositiveLiteral> ruleHeadConjunction = Expressions
						.makePositiveConjunction(Q_Ex1, Q_Ex2);
			}
			
		}
		

	}

}
