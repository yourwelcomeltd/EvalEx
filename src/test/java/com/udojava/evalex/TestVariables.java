package com.udojava.evalex;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class TestVariables {

	@Test
	public void testVars() {
		assertEquals("3.141593", new Expression("PI").eval().toString());
		assertEquals("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679", new Expression("PI").setPrecision(MathContext.UNLIMITED.getPrecision()).eval().toString());
		assertEquals("3.141592653589793238462643383279503", new Expression("PI").setPrecision(MathContext.DECIMAL128.getPrecision()).eval().toString());
		assertEquals("3.141592653589793", new Expression("PI").setPrecision(MathContext.DECIMAL64.getPrecision()).eval().toString());
		assertEquals("3.141593", new Expression("PI").setPrecision(MathContext.DECIMAL32.getPrecision()).eval().toString());
		assertEquals("6.283186", new Expression("PI*2.0").eval().toString());
		assertEquals("21",
				new Expression("3*x").setVariable("x", new BigDecimal("7"))
						.eval().toString());

		Object result = new Expression("(a^2)+(b^2)")
				.setVariable("a", new BigDecimal("2"))
				.setVariable("b", new BigDecimal("4")).eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("20", ((BigDecimal)result).toPlainString());

		result = new Expression("a^(2+b)^2")
				.setVariable("a", "2")
				.setVariable("b", "4").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("68719480000", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testSubstitution() {
		Expression e = new Expression("x+y");

		Object result = e.with("x", "1").and("y", "1").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2", ((BigDecimal)result).toPlainString());
		result = e.with("y", "0").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1", ((BigDecimal)result).toPlainString());
		result = e.with("x", "0").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testWith() {
		assertEquals("21",
				new Expression("3*x").with("x", new BigDecimal("7"))
						.eval().toString());

		Object result = new Expression("(a^2)+(b^2)")
				.with("a", new BigDecimal("2"))
				.with("b", new BigDecimal("4")).eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("20", ((BigDecimal)result).toPlainString());

		result = new Expression("a^(2+b)^2")
				.with("a", "2")
				.with("b", "4").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("68719480000", ((BigDecimal)result).toPlainString());

		result = new Expression("_a^(2+_b)^2")
				.with("_a", "2")
				.with("_b", "4").eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("68719480000", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testNames() {
		assertEquals("21",
				new Expression("3*longname").with("longname", new BigDecimal("7"))
						.eval().toString());
		
		assertEquals("21",
				new Expression("3*longname1").with("longname1", new BigDecimal("7"))
						.eval().toString());
		
		assertEquals("21",
				new Expression("3*_longname1").with("_longname1", new BigDecimal("7"))
						.eval().toString());		
	}
}
