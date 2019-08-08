package synergix.plugin.intellj.reference;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.ArrayUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class GenericFileNameReference<T extends PsiElement> extends PsiPolyVariantReferenceBase<T> {

	private final static int FIRST_ORDER = 1;
	private final static int SECOND_ORDER = 2;
	private final static int THIRD_ORDER = 3;

	public GenericFileNameReference(T psiElement) {
		super(psiElement, true);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode) {
		final String finalCleanFileName = this.computeStringValue();
		final String finalCleanFileNameWithoutExtension = this.getNameWithoutExtension(finalCleanFileName);
		Project project = this.getElement().getProject();

		Comparator<Pair<Integer, VirtualFile>> chainedComparator = Comparator.<Pair<Integer, VirtualFile>>comparingInt(p -> p.getFirst())
				.thenComparing(p -> p.getSecond().getCanonicalPath())
				.thenComparing(p -> p.getSecond().getName());

		final Set<Pair<Integer, VirtualFile>> sortedResults = new TreeSet<>(chainedComparator);

		if (!StringUtils.isEmpty(finalCleanFileNameWithoutExtension)) {
			ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

			fileIndex.iterateContent(fileOrDir -> {
				if (!fileOrDir.isDirectory()) {
					if (fileOrDir.getName().equalsIgnoreCase(finalCleanFileName)) {
						sortedResults.add(new Pair<>(FIRST_ORDER, fileOrDir));

					} else if (fileOrDir.getNameWithoutExtension().equalsIgnoreCase(finalCleanFileName)
							|| fileOrDir.getNameWithoutExtension().equalsIgnoreCase(finalCleanFileNameWithoutExtension)) {
						if (fileOrDir.getFileType().equals(this.getElement().getContainingFile().getFileType())) {
							sortedResults.add(new Pair<>(SECOND_ORDER, fileOrDir));
						} else {
							sortedResults.add(new Pair<>(THIRD_ORDER, fileOrDir));
						}
					}
				}
				return true;
			});
		}

		PsiManager psiManager = PsiManager.getInstance(project);
		ResolveResult[] result = new ResolveResult[sortedResults.size()];
		int i = 0;
		for (Pair<Integer, VirtualFile> pair : sortedResults) {
			PsiFile psiFile = psiManager.findFile(pair.getSecond());
			if (psiFile != null) {
				result[i++] = new PsiElementResolveResult(psiFile);
			}
		}
		return result;
	}

	private String getNameWithoutExtension(String elementText) {
		return FileUtilRt.getNameWithoutExtension(FilenameUtils.getName(elementText));
	}

	@NotNull
	@Override
	public Object[] getVariants() {
//        FileReferenceCompletion completion = FileReferenceCompletion.getInstance();
//        if (completion != null) {
//            PsiReference[] test = myElement.getReferences();
//            System.out.println(test);
////            return completion.getFileReferenceCompletionVariants(this);
//        }
		return ArrayUtil.EMPTY_OBJECT_ARRAY;
	}

	@Override
	public boolean isReferenceTo(PsiElement element) {
		return false;
	}

	@NotNull
	protected abstract String computeStringValue();

}
