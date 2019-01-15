package synergix.plugin.intellj;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.jsp.ELElementType;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.completion.*;
import synergix.plugin.intellj.constants.Constants;
import synergix.plugin.intellj.utils.SynUtil;

public class SynCompletionContributor extends CompletionContributor {
    public SynCompletionContributor() {
        this.extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(XmlElementType.XML_ATTRIBUTE_VALUE_TOKEN), new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        SynCompletion completion = SynCompletionContributor.this.getCompletion(parameters);
                        resultSet.addAllElements(completion.createLookupElementList());
                    }
                }
        );

        this.extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(XmlElementType.XML_ATTRIBUTE_DECL), new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addAllElements(new XmlEntityCompletion(parameters).createLookupElementList());
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
                case Constants.XmlAttribute.DATA_TYPE:
                    return new DataTypeCompletition(parameters);
            }
        }

        return new EmptyCompletion(parameters);
    }
}
