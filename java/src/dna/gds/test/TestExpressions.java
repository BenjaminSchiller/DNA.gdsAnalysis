package dna.gds.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestExpressions {

	public static void main(String[] args) throws ScriptException {
		double x = 100;
		System.out.println(eval("185.654", x));
		System.out.println(eval("101.489 + -0.873641 * x + 0.0116703 * x**2", x));
		System.out.println(eval("-212.07 + 154.818 * log(x)", x));
		System.out.println(eval("83.8813 + 4.29749 * x + -0.0190796 * x**2", x));
		System.out.println(eval("64.8941 + 6.30081 * x", x));
	}

	public static double eval(String expression, double x)
			throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		return (double) engine.eval(process(expression, x));
	}

	public static String process(String expr, double x) {
		if (expr.contains("log(x)")) {
			expr = expr.replace("log(x)", Double.toString(Math.log(x)));
		}
		if (expr.contains("x**2")) {
			expr = expr.replace("x**2", "x*x");
		}
		if (expr.contains("x")) {
			expr = expr.replace("x", Double.toString(x));
		}
		return expr;
	}
}
