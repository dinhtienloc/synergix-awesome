package synergix.plugin.intellj.reference;

import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

public class XmlAttributeGenericFileNameReference extends GenericFileNameReference<XmlAttributeValue> {

    public XmlAttributeGenericFileNameReference(XmlAttributeValue psiElement) {
        super(psiElement);
    }

    @NotNull
    @Override
    protected String computeStringValue() {
        return getElement().getValue();
    }

}
