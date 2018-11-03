package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import synergix.plugin.intellj.utils.SynUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataTypeCompletition extends SynCompletion {
    private static String ENUM_TYPE_FILE = "enum-types.xml";
    private static String DATA_TYPE_FILE = "data-types.xml";
    private static String TH6_MODULE = "TH6";

    public DataTypeCompletition(CompletionParameters parameters) {
        super(parameters);
    }

    @Override
    public List<LookupElement> createLookupElementList() {
        List<LookupElement> lookupElements = new ArrayList<>();
        lookupElements.addAll(this.getDefaultDataType());
        lookupElements.addAll(this.getEnumDataType());
        return lookupElements;
    }

    private List<LookupElement> getEnumDataType() {
        Project project = this.getParameters().getOriginalFile().getProject();
        PsiFile[] enumPsiFiles = SynUtil.getFilesByName(project, ENUM_TYPE_FILE, TH6_MODULE);
        if (enumPsiFiles.length == 0) {
            return new ArrayList<>();
        }

        return this.createLookupElementFromXmlFile((XmlFile)enumPsiFiles[0]);
    }

    protected List<LookupElement> getDefaultDataType() {
        Project project = this.getParameters().getOriginalFile().getProject();
        PsiFile[] dataTypePsiFiles = SynUtil.getFilesByName(project, DATA_TYPE_FILE, TH6_MODULE);
        if (dataTypePsiFiles.length == 0) {
            return new ArrayList<>();
        }

        return this.createLookupElementFromXmlFile((XmlFile)dataTypePsiFiles[0]);
    }


    private List<LookupElement> createLookupElementFromXmlFile(XmlFile xmlFile) {
        List<LookupElement> elms = new ArrayList<>();
        XmlTag[] typeTags = xmlFile.getRootTag().getSubTags();
        Arrays.stream(typeTags).forEach(tag -> {
            String val = tag.getAttribute("name").getDisplayValue();
            elms.add(LookupElementBuilder.create(val));
        });
        return elms;
    }
}
