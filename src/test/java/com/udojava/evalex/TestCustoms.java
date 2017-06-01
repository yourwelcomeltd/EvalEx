package com.udojava.evalex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

public class TestCustoms {

	@Test
	public void testCustomOperator() {
		Expression e = new Expression("2.1234 >> 2");
		
		e.addOperator(e.new Operator(">>", 30, true) {
			@Override
			public Object eval(Object v1, Object v2) {
				Assert.assertTrue(v1 instanceof BigDecimal);
				Assert.assertTrue(v2 instanceof BigDecimal);
				return ((BigDecimal)v1).movePointRight(((BigDecimal)v2).toBigInteger().intValue());
			}
		});

		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("212.34", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testCustomFunction() {
		Expression e = new Expression("2 * average(12,4,8)");
		e.addFunction(e.new Function("average", 3) {
			@Override
			public Object eval(List<Object> parameters) {
				Object param0 = parameters.get(0);
				Object param1 = parameters.get(1);
				Object param2 = parameters.get(2);
				assertTrue(param0 instanceof BigDecimal);
				assertTrue(param1 instanceof BigDecimal);
				assertTrue(param2 instanceof BigDecimal);
				BigDecimal sum = ((BigDecimal)param0).add((BigDecimal) param1).add((BigDecimal) param2);
				return sum.divide(new BigDecimal(3));
			}
		});

		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("16", ((BigDecimal)result).toPlainString());
	}
	
	@Test
	public void testCustomFunctionVariableParameters() {
		Expression e = new Expression("2 * average(12,4,8,2,9)");
		e.addFunction(e.new Function("average", -1) {
			@Override
			public Object eval(List<Object> parameters) {
				BigDecimal sum = new BigDecimal(0);
				for (Object parameter : parameters) {
					assertTrue(parameter instanceof BigDecimal);
					sum = sum.add((BigDecimal) parameter);
				}
				return sum.divide(new BigDecimal(parameters.size()));
			}
		});

		Object result = e.eval();
		assertTrue(result instanceof BigDecimal);
		assertEquals("14", ((BigDecimal)result).toPlainString());
	}

}
