package synergix.plugin.intellj.syndom;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlToken;

public class SynUtil {
	public static String getClosestAttributeName(CompletionParameters parameters) {
		PsiElement element = parameters.getPosition();
		if (element instanceof XmlToken) {
			element = element.getPrevSibling();
			if (element != null) element = element.getParent(); else return null;
			if (element != null) element = element.getPrevSibling(); else return null;
			if (element != null) element = element.getPrevSibling(); else return null;
			if (element != null) return element.getText(); else return null;
		} else {
			return null;
		}
	}
}
