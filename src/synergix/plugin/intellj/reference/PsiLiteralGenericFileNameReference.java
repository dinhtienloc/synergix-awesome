package synergix.plugin.intellj.reference;

import com.intellij.psi.PsiLiteral;
import org.jetbrains.annotations.NotNull;

public class PsiLiteralGenericFileNameReference extends GenericFileNameReference<PsiLiteral> {
    public PsiLiteralGenericFileNameReference(PsiLiteral psiElement) {
        super(psiElement);
    }

    @NotNull
    @Override
    protected String computeStringValue() {
        String computedStringValue = "";

        Object value = getElement().getValue();
        if (value instanceof String) {
            computedStringValue = (String) value;
        }

        return computedStringValue;
    }
}
