package synergix.plugin.intellj.utils;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlToken;

import java.util.Arrays;

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

	public static PsiFile[] getFilesByName(Project project, String fileName, String moduleName) {
		GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
		if (moduleName != null) {
			Module module = SynUtil.getModuleByName(project, moduleName);
			if (module != null) searchScope = GlobalSearchScope.moduleScope(module);
		}
		return  FilenameIndex.getFilesByName(project, fileName, searchScope);
	}

	public static Module getModuleByName(Project project, String moduleName) {
		Module[] modules = ModuleManager.getInstance(project).getModules();
		return Arrays.stream(modules).filter(m -> moduleName.equals(m.getName())).findFirst().orElse(null);
	}
}
