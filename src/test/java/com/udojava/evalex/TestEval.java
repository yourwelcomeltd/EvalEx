package com.udojava.evalex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import com.udojava.evalex.Expression.ExpressionException;


public class TestEval {
	
	@Test
	public void testInvalidExpressions1() {
		String err = "";
		try {
			Expression expression = new Expression("12 18 2");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many numbers or variables", err);
	}

	@Test
	public void testInvalidExpressions2() {
		String err = "";
		try {
			Expression expression = new Expression("(12)(18)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many numbers or variables", err);
	}

	@Test
	public void testInvalidExpressions3() {
		String err = "";
		try {
			Expression expression = new Expression("12 + * 18");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Missing parameter(s) for operator +", err);
	}

	@Test
	public void testInvalidExpressions4() {
		String err = "";
		try {
			Expression expression = new Expression("");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Empty expression", err);
	}
	
	@Test
	public void testWrongBrackets1() {
		String err = "";
		try {
			Expression expression = new Expression("2*3(5*3)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing operator at character position 4", err);
	}
	
	@Test
	public void testWrongBrackets2() {
		String err = "";
		try {
			Expression expression = new Expression("2*(3((5*3)))");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing operator at character position 5", err);
	}
	
	@Test
	public void testBrackets() {
		Object result = new Expression("(1+2)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		result = new Expression("((1+2))").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		result = new Expression("(((1+2)))").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		result = new Expression("(1+2)*(1+2)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("9", ((BigDecimal)result).toPlainString());
		result = new Expression("(1+2)*(1+2)+1").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("10", ((BigDecimal)result).toPlainString());
		result = new Expression("(1+2)*((1+2)+1)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("12", ((BigDecimal)result).toPlainString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testUnknow1() {
		Object result = new Expression("7#9").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("", ((BigDecimal)result).toPlainString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testUnknow2() {
		Object result = new Expression("123.6*-9.8-7#9").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testSimple() {
		Object result = new Expression("1+2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		result = new Expression("4/2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2", ((BigDecimal)result).toPlainString());
		result = new Expression("3+4/2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("5", ((BigDecimal)result).toPlainString());
		result = new Expression("(3+4)/2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.5", ((BigDecimal)result).toPlainString());
		result = new Expression("4.2*1.9").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("7.98", ((BigDecimal)result).toPlainString());
		result = new Expression("8%3").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2", ((BigDecimal)result).toPlainString());
		result = new Expression("8%2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testPow() {
		Object result = new Expression("2^4").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("16", ((BigDecimal)result).toPlainString());
		result = new Expression("2^8").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("256", ((BigDecimal)result).toPlainString());
		result = new Expression("3^2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("9", ((BigDecimal)result).toPlainString());
		result = new Expression("2.5^2").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("6.25", ((BigDecimal)result).toPlainString());
		result = new Expression("2.6^3.5").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("28.34045", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testSqrt() {
		Object result = new Expression("SQRT(16)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("4", ((BigDecimal)result).toPlainString());
		result = new Expression("SQRT(2)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1.4142135", ((BigDecimal)result).toPlainString());
		result = new Expression("SQRT(2)").setPrecision(128).eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605", ((BigDecimal)result).toPlainString());
		result = new Expression("SQRT(5)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.2360679", ((BigDecimal)result).toPlainString());
		result = new Expression("SQRT(9875)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("99.3730345", ((BigDecimal)result).toPlainString());
		result = new Expression("SQRT(5.55)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.3558437", ((BigDecimal)result).toPlainString());
		result = new Expression("SQRT(0)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testFunctions() {
		Object result = new Expression("Random()").eval();
		assertTrue(result instanceof BigDecimal);
		assertNotSame("1.5", ((BigDecimal)result).toPlainString());
		result = new Expression("SIN(23.6)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.400349", ((BigDecimal)result).toPlainString());
		result = new Expression("MAX(-7,8)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("8", ((BigDecimal)result).toPlainString());
		result = new Expression("MAX(3,max(4,5))").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("5", ((BigDecimal)result).toPlainString());
		result = new Expression("MAX(3,max(MAX(9.6,-4.2),Min(5,9)))").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("9.6", ((BigDecimal)result).toPlainString());
		result = new Expression("LOG(10)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.302585", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testExpectedParameterNumbers() {
		String err = "";
		try {
			Expression expression = new Expression("Random(1)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Function Random expected 0 parameters, got 1", err);

		try {
			Expression expression = new Expression("SIN(1, 6)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Function SIN expected 1 parameters, got 2", err);
	}

	@Test
	public void testVariableParameterNumbers() {
		String err = "";
		try {
			Expression expression = new Expression("min()");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("MIN requires at least one parameter", err);

		Object result = new Expression("min(1)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1", ((BigDecimal)result).toPlainString());
		result = new Expression("min(1, 2)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1", ((BigDecimal)result).toPlainString());
		result = new Expression("min(1, 2, 3)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1", ((BigDecimal)result).toPlainString());
		result = new Expression("max(3, 2, 1)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		result = new Expression("max(3, 2, 1, 4, 5, 6, 7, 8, 9, 0)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("9", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testOrphanedOperators() {
		String err = "";
		try {
			new Expression("/").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator /", err);

		err = "";
		try {
			new Expression("3/").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator /", err);

		err = "";
		try {
			new Expression("/3").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator /", err);

		err = "";
		try {
			new Expression("SIN(MAX(23,45,12))/").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator /", err);

		err = "";
		try {
			new Expression("+SIN(MAX(23,45,12))").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator +", err);
	}

	@Test
	public void testOrphanedOperatorsInFunctionParameters() {
		String err = "";
		try {
			new Expression("min(/)").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator / at character position 4", err);

		err = "";
		try {
			new Expression("min(3/)").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator / at character position 5", err);

		err = "";
		try {
			new Expression("min(/3)").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator / at character position 4", err);

		err = "";
		try {
			new Expression("SIN(MAX(23,45,12,23.6/))").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator / at character position 21", err);

		err = "";
		try {
			new Expression("SIN(MAX(23,45,12/,23.6))").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator / at character position 16", err);

		err = "";
		try {
			new Expression("SIN(MAX(23,45,>=12,23.6))").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator >= at character position 14", err);

		err = "";
		try {
			new Expression("SIN(MAX(>=23,45,12,23.6))").eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing parameter(s) for operator >= at character position 8", err);
	}

	@Test
	public void testExtremeFunctionNesting() {
		Object result = new Expression("Random()").eval();
		assertTrue(result instanceof BigDecimal);
		assertNotSame("1.5", ((BigDecimal)result).toPlainString());
		result = new Expression("SIN(SIN(COS(23.6)))").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.0002791281", ((BigDecimal)result).toPlainString());
		result = new Expression("MIN(0, SIN(SIN(COS(23.6))), 0-MAX(3,4,MAX(0,SIN(1))), 10)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("-4", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testTrigonometry() {
		Object result = new Expression("SIN(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5", ((BigDecimal)result).toPlainString());
		result = new Expression("cos(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.8660254", ((BigDecimal)result).toPlainString());
		result = new Expression("TAN(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5773503", ((BigDecimal)result).toPlainString());
		result = new Expression("SINH(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("5343237000000", ((BigDecimal)result).toPlainString());
		result = new Expression("COSH(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("5343237000000", ((BigDecimal)result).toPlainString());
		result = new Expression("TANH(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1", ((BigDecimal)result).toPlainString());
		result = new Expression("RAD(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5235988", ((BigDecimal)result).toPlainString());
		result = new Expression("DEG(30)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1718.873", ((BigDecimal)result).toPlainString());
		
	}
	
	@Test
	public void testMinMaxAbs() {
		Object result = new Expression("MAX(3.78787,3.78786)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.78787", ((BigDecimal)result).toPlainString());
		result = new Expression("max(3.78786,3.78787)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.78787", ((BigDecimal)result).toPlainString());
		result = new Expression("MIN(3.78787,3.78786)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.78786", ((BigDecimal)result).toPlainString());
		result = new Expression("Min(3.78786,3.78787)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.78786", ((BigDecimal)result).toPlainString());
		result = new Expression("aBs(-2.123)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.123", ((BigDecimal)result).toPlainString());
		result = new Expression("abs(2.123)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.123", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testRounding() {
		Object result = new Expression("round(3.78787,1)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.8", ((BigDecimal)result).toPlainString());
		result = new Expression("round(3.78787,3)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.788", ((BigDecimal)result).toPlainString());
		result = new Expression("round(3.7345,3)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3.734", ((BigDecimal)result).toPlainString());
		result = new Expression("round(-3.7345,3)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("-3.734", ((BigDecimal)result).toPlainString());
		result = new Expression("round(-3.78787,2)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("-3.79", ((BigDecimal)result).toPlainString());
		result = new Expression("round(123.78787,2)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("123.79", ((BigDecimal)result).toPlainString());
		result = new Expression("floor(3.78787)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		result = new Expression("ceiling(3.78787)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("4", ((BigDecimal)result).toPlainString());
		result = new Expression("floor(-2.1)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("-3", ((BigDecimal)result).toPlainString());
		result = new Expression("ceiling(-2.1)").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("-2", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testMathContext() {
		Expression e = null;
		e = new Expression("2.5/3").setPrecision(2);
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.83", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2.5/3").setPrecision(3);
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.833", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2.5/3").setPrecision(8);
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.83333333", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2.5/3").setRoundingMode(RoundingMode.DOWN);
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.8333333", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2.5/3").setRoundingMode(RoundingMode.UP);
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.8333334", ((BigDecimal)result).toPlainString());
	}
	
}
