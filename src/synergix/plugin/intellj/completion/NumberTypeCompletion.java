package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.InheritanceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NumberTypeCompletion extends SynCompletion {
	public NumberTypeCompletion(CompletionParameters parameters) {
		super(parameters);
	}

	@Override
	public List<LookupElement> createLookupElementList() {
		List<LookupElement> lookupElements = new ArrayList<>();

		Project project = this.getParameters().getOriginalFile().getProject();
		Module[] modules = ModuleManager.getInstance(project).getModules();
		Module TH6MetaModule = Arrays.stream(modules).filter(m -> "TH6Meta".equals(m.getName())).findFirst().orElse(null);

//		PsiFile[] synAbstractDataTypeFiles = FilenameIndex.getFilesByName(project, "SynDataType.java", GlobalSearchScope.moduleScope(TH6MetaModule));
//		if (synAbstractDataTypeFiles == null || synAbstractDataTypeFiles.length == 0) {
//			return lookupElements;
//		}
//
//		PsiJavaFile synAbstractDataTypeJavaFile = (PsiJavaFile) synAbstractDataTypeFiles[0];
//		final PsiClass synAbstractDataTypeClass = synAbstractDataTypeJavaFile.getClasses()[0];

		Collection<VirtualFile> synDataTypeVirtualFiles = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.moduleScope(TH6MetaModule));
		if (synDataTypeVirtualFiles == null || synDataTypeVirtualFiles.size() == 0) {
			return lookupElements;
		}

		for (VirtualFile virtualFile : synDataTypeVirtualFiles) {
			PsiFile synDataTypeFile = PsiManager.getInstance(project).findFile(virtualFile);
			if (synDataTypeFile == null)
				continue;

			PsiJavaFile synDataTypeJavaFile = (PsiJavaFile) synDataTypeFile;
			final PsiClass synDataTypeClass = synDataTypeJavaFile.getClasses()[0];

			if (!InheritanceUtil.isInheritor(synDataTypeClass, "synergix.th6.data.meta.datatype.SynDataType")) {
				continue;
			}

			synDataTypeClass.accept(new JavaRecursiveElementVisitor() {
				@Override
				public void visitField(PsiField element) {
					if ("TYPE_NAME".equals(element.getNameIdentifier().getText())) {
						extractTypeNameFieldValue(element, lookupElements);
					}
					super.visitElement(element);
				}
			});
		}
		return lookupElements;
	}

	private void extractTypeNameFieldValue(PsiField element, List<LookupElement> lookupElements) {
		element.accept(new JavaRecursiveElementVisitor() {
			@Override
			public void visitLiteralExpression(PsiLiteralExpression element) {
				lookupElements.add(LookupElementBuilder.create(element.getText().replaceAll("\"", "")));
				super.visitElement(element);
			}
		});
	}
}
