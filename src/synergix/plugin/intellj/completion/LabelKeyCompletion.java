package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LabelKeyCompletion extends SynCompletion {
	private static String LABEL_PROP_FILE = "labels.properties";
	private static String INTELLIJ_CURSOR_SYMBOL = "IntellijIdeaRulezzz";

	public LabelKeyCompletion(CompletionParameters parameters) {
		super(parameters);
	}

	@Override
	public List<LookupElement> createLookupElementList() {
		List<LookupElement> lookupElements = new ArrayList<>();

		Project project = this.getParameters().getOriginalFile().getProject();
		PsiFile[] labelsFile = FilenameIndex.getFilesByName(project, LABEL_PROP_FILE, GlobalSearchScope.allScope(project));
		if (labelsFile == null || labelsFile.length == 0) {
			return lookupElements;
		}

		try (InputStream input = labelsFile[0].getVirtualFile().getInputStream()) {
			Properties prop = new Properties();
			prop.load(input);
			prop.forEach((key, value) -> {
				String labelKey = key.toString();
				String intellijInputValue = this.getParameters().getPosition().getText();
				String realInputValue = intellijInputValue.replaceAll(INTELLIJ_CURSOR_SYMBOL, "")
						.replaceAll(INTELLIJ_CURSOR_SYMBOL, "");
				if (labelKey.startsWith(realInputValue)) {
					lookupElements.add(LookupElementBuilder.create(labelKey));
				}
			});
			return lookupElements;
		} catch (IOException e) {
			return lookupElements;
		}
	}
}
