package com.udojava.evalex;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;
import org.junit.Ignore;

public class TestSciNotation {

	@Test
	public void testSimple() {
		Expression e = new Expression("1e10");
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("10000000000", ((BigDecimal)result).toPlainString());
		
		e = new Expression("1E10");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("10000000000", ((BigDecimal)result).toPlainString());
		
		e = new Expression("123.456E3");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("123456", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2.5e0");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.5", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testNegative() {
		Expression e = new Expression("1e-10");
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.0000000001", ((BigDecimal)result).toPlainString());
		
		e = new Expression("1E-10");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.0000000001", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2135E-4");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.2135", ((BigDecimal)result).toPlainString());
	}
		
	@Test //@Ignore("Expected Failures: not implemented yet")
	public void testPositive() {
		Expression e = new Expression("1e+10");
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("10000000000", ((BigDecimal)result).toPlainString());
		
		e = new Expression("1E+10");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("10000000000", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testCombined() {
		Expression e = new Expression("sqrt(152.399025e6)", MathContext.DECIMAL64);
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("12345", ((BigDecimal)result).toPlainString());
		
		e = new Expression("sin(3.e1)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5", ((BigDecimal)result).toPlainString());
		
		e = new Expression("sin( 3.e1)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5", ((BigDecimal)result).toPlainString());
		
		e = new Expression("sin(3.e1 )");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5", ((BigDecimal)result).toPlainString());
		
		e = new Expression("sin( 3.e1 )");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0.5", ((BigDecimal)result).toPlainString());
		
		e = new Expression("2.2e-16 * 10.2");;
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2.244E-15", ((BigDecimal)result).toString());
	}
	
	@Test(expected=NumberFormatException.class)
	public void testError1() {
		Expression e = new Expression("1234e-2.3");
		e.eval();
	}
	
	@Test(expected=NumberFormatException.class)
	public void testError2() {
		Expression e = new Expression("1234e2.3");
		e.eval();
	}
	
	@Test
	public void testError3() {
		String err = "";
		Expression e = new Expression("e2");
		try {
			e.eval();
		} catch (RuntimeException ex) {
			err = ex.getMessage();
		}
		assertEquals("Unknown operator or function: e2", err);
	}
}
