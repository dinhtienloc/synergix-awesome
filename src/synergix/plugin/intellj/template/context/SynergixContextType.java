package synergix.plugin.intellj.template.context;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class SynergixContextType extends TemplateContextType {
    protected SynergixContextType() {

        super("SYNERGIX", "Synergix");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        return file.getName().endsWith(".xml");
    }
}