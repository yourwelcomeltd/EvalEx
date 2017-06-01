package com.udojava.evalex;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.udojava.evalex.Expression.ExpressionException;
import com.udojava.evalex.Expression.Function;

public class TestVarArgs {

	@Test
	public void testSimple() {
		Expression e = new Expression("max(1)");
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("1", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(4,8)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("8", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(12,4,8)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("12", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(12,4,8,16,32)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("32", ((BigDecimal)result).toPlainString());
	}

	@Test
	public void testNested() {
		Expression e = new Expression("max(1,2,max(3,4,5,max(9,10,3,4,5),8),7)");
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("10", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testZero() {
		Expression e = new Expression("max(0)");
		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(0,3)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("3", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(2,0,-3)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("2", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(-2,0,-3)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0", ((BigDecimal)result).toPlainString());
		
		e = new Expression("max(0,0,0,0)");
		result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("0", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testError() {
		String err = "";
		Expression e = new Expression("max()");
		try {
			e.eval();
		} catch (ExpressionException ex) {
			err = ex.getMessage();
		}
		assertEquals("MAX requires at least one parameter", err);
	}

	@Test
	public void testCustomFunction1() {
		Expression e = new Expression("3 * AVG(2,4)");
		e.addFunction(e.new Function("AVG", -1) {
			@Override
			public Object eval(List<Object> parameters) {
				if (parameters.size() == 0) {
					throw new ExpressionException("AVG requires at least one parameter");
				}
				BigDecimal avg = new BigDecimal(0);
				for (Object parameter : parameters) {
						assertTrue(parameter instanceof BigDecimal);
						avg = avg.add((BigDecimal)parameter);
				}
				return avg.divide(new BigDecimal(parameters.size()));
			}
		});

		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("9", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testCustomFunction2() {
		Expression e = new Expression("4 * AVG(2,4,6,8,10,12)");
		e.addFunction(e.new Function("AVG", -1) {
			@Override
			public Object eval(List<Object> parameters) {
				if (parameters.size() == 0) {
					throw new ExpressionException("AVG requires at least one parameter");
				}
				BigDecimal avg = new BigDecimal(0);
				for (Object parameter : parameters) {
						assertTrue(parameter instanceof BigDecimal);
						avg = avg.add((BigDecimal) parameter);
				}
				return avg.divide(new BigDecimal(parameters.size()));
			}
		});

		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("28", ((BigDecimal)result).toPlainString());
	}
}
