package com.udojava.evalex;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestVariableCharacters {

	@Test
	public void testBadVarChar() {
		String err = "";
		try {
			Expression expression = new Expression("a.b/2*PI+MIN(e,b)");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator '.' at position 2", err);
	}

	@Test
	public void testAddedVarChar() {
		String err = "";
		Expression expression;

		try {
			expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator '.' at position 2", err);

		try {
			expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator or function: a.b", err);

		expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.");
		Object result = expression.with("a.b", "2").and("b", "3").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("5.859875", ((BigDecimal)result).toPlainString());

		try {
			expression = new Expression(".a.b/2*PI+MIN(e,b)").setVariableCharacters("_.");
			expression.eval();
		} catch (Expression.ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Unknown operator '.' at position 1", err);

		expression = new Expression("a.b/2*PI+MIN(e,b)").setVariableCharacters("_.").setFirstVariableCharacters(".");
		result = expression.with("a.b", "2").and("b", "3").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("5.859875", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testFirstVarChar() {
		Expression expression = new Expression("a.b*$PI").setVariableCharacters(".").setFirstVariableCharacters("$");
		Object result = expression.with("a.b", "2").and("$PI", "3").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("6", ((BigDecimal)result).toPlainString());

	}
}
