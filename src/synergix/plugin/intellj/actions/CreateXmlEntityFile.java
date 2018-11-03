package synergix.plugin.intellj.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplateDialog;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;
import synergix.plugin.intellj.utils.SynergixIcons;

import java.util.Properties;

public class CreateXmlEntityFile extends CreateFileFromTemplateAction implements DumbAware {


    public static final String SYNERGIX_CONFIGURATION = "Create Synergix XML Entity File";

    public CreateXmlEntityFile() {
        super("Synergix XML Entity File", "Create New Synergix XML Entity File.", SynergixIcons.SynergixIcon);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle(SYNERGIX_CONFIGURATION)
                .addKind("Synergix XML Entity File", StdFileTypes.XML.getIcon(), "XML Entity");
    }

    @Override
    protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir) {
        String fileName = FileUtilRt.getNameWithoutExtension(name);
        Project project = dir.getProject();
        Properties props = createProperties(project, fileName);
        try {
            return FileTemplateUtil.createFromTemplate(template, StringUtils.capitalize(name.toLowerCase()), props, dir).getContainingFile();
        } catch (Exception e) {
            LOG.error("Error while creating new XML Entity file", e);
            return null;
        }
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "Create " + newName;
    }

    private Properties createProperties(Project project, String fileName) {
        Properties props = FileTemplateManager.getInstance(project).getDefaultProperties();
        props.setProperty("NAME", fileName.toUpperCase());
        return props;
    }
}
