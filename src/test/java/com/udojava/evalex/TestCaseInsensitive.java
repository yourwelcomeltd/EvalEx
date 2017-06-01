package com.udojava.evalex;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by showdown on 2/28/2016 at 3:29 PM.
 * Project EvalEx
 */
public class TestCaseInsensitive {
    @Test
    public void testVariableIsCaseInsensitive() {

        Expression expression = new Expression("a");
        expression.setVariable("A", new BigDecimal(20));
        Object result = expression.eval();
        Assert.assertTrue(result instanceof BigDecimal);
        Assert.assertEquals(((BigDecimal)result).intValue(), 20);

        expression = new Expression("a + B");
        expression.setVariable("A", new BigDecimal(10));
        expression.setVariable("b", new BigDecimal(10));
        result = expression.eval();
        Assert.assertTrue(result instanceof BigDecimal);
        Assert.assertEquals(((BigDecimal)result).intValue(), 20);

        expression = new Expression("a+B");
        expression.setVariable("A", "c+d");
        expression.setVariable("b", new BigDecimal(10));
        expression.setVariable("C", new BigDecimal(5));
        expression.setVariable("d", new BigDecimal(5));
        result = expression.eval();
        Assert.assertTrue(result instanceof BigDecimal);
        Assert.assertEquals(((BigDecimal)result).intValue(), 20);
    }

    @Test
    public void testFunctionCaseInsensitive() {

        Expression expression = new Expression("a+testsum(1,3)");
        expression.setVariable("A", new BigDecimal(1));
        expression.addFunction(expression.new Function("testSum",-1){
            @Override
            public Object eval(List<Object> parameters) {
                BigDecimal value =null;
                for (Object d : parameters) {
                    Assert.assertTrue(d instanceof BigDecimal);
                    value = value == null ? (BigDecimal)d : value.add((BigDecimal)d);
                }
                return value;
            }
        });

        Assert.assertEquals(expression.eval(), new BigDecimal(5) );

    }
}
