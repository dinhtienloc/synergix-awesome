package synergix.plugin.intellj.utils;

public class ElUtils {

	public static boolean isElBlock(final String str) {
		return str.startsWith("#{") && str.endsWith("}");
	}
	public static String removeELBraces(final String elExpression) {
		return elExpression.substring(2, elExpression.length() - 1);
	}
}
