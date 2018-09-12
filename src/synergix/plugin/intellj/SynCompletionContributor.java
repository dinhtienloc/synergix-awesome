package synergix.plugin.intellj;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.completion.EmptyCompletion;
import synergix.plugin.intellj.completion.LabelKeyCompletion;
import synergix.plugin.intellj.completion.NumberTypeCompletion;
import synergix.plugin.intellj.completion.SynCompletion;
import synergix.plugin.intellj.constants.Constants;
import synergix.plugin.intellj.syndom.SynUtil;

public class SynCompletionContributor extends CompletionContributor {
	public SynCompletionContributor() {
		extend(CompletionType.BASIC,
			PlatformPatterns.psiElement(XmlElementType.XML_ATTRIBUTE_VALUE_TOKEN), new CompletionProvider<CompletionParameters>() {
				public void addCompletions(@NotNull CompletionParameters parameters,
										   ProcessingContext context,
										   @NotNull CompletionResultSet resultSet) {
					SynCompletion completion = getCompletion(parameters);
					resultSet.addAllElements(completion.createLookupElementList());
				}
			}
		);
	}

	private SynCompletion getCompletion(@NotNull CompletionParameters parameters) {
		String attributeName = SynUtil.getClosestAttributeName(parameters);
		if (attributeName != null) {
			switch (attributeName) {
				case Constants.XmlAttribute.LABEL_KEY:
					return new LabelKeyCompletion(parameters);
				case Constants.XmlAttribute.NUMBER_TYPE:
					return new NumberTypeCompletion(parameters);
			}
		}

		return new EmptyCompletion(parameters);
	}
}
