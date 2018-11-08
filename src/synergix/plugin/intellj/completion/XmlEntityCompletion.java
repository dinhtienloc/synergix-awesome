package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import synergix.plugin.intellj.utils.SynUtil;

import java.util.ArrayList;
import java.util.List;

public class XmlEntityCompletion extends SynCompletion {
    public XmlEntityCompletion(CompletionParameters parameters) {
        super(parameters);
    }

    @Override
    public List<LookupElement> createLookupElementList() {
        List<LookupElement> rs = new ArrayList<>();
        XmlAttribute xmlAttribute = (XmlAttribute) this.getParameters().getPosition();
        XmlTag crtTag = SynUtil.getTag(xmlAttribute);
        switch (crtTag.getText()) {
            case "table":
                rs.add(LookupElementBuilder.create("abstractTable")); break;
            case "column":
                rs.addAll(this.createLookupElements("name", "data-type", "currency-code", "not-null", "default-value")); break;
            case "foreign-key":
                rs.addAll(this.createLookupElements("parent-table", "columns", "child-mapping", "parent-mapping", "child-mapping-annotations", "one-to-one")); break;
        }

        return rs;
    }

    private List<LookupElement> createLookupElements(String... values) {
        List<LookupElement> rs = new ArrayList<>();
        for(String v : values) {
            rs.add(LookupElementBuilder.create(v));
        }
        return rs;
    }
}
