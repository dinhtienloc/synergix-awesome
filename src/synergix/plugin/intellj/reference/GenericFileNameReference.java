package synergix.plugin.intellj.reference;

import com.google.common.collect.ComparisonChain;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.ArrayUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;

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
        final String finalCleanFileName = computeStringValue();
        final String finalCleanFileNameWithoutExtension = getNameWithoutExtension(finalCleanFileName);
        Project project = getElement().getProject();
        final Set<Pair<Integer, VirtualFile>> sortedResults = new TreeSet<>((o1, o2) -> ComparisonChain.start().
                compare(o1.getFirst(), o2.getFirst()).
                compare(o1.getSecond(), o2.getSecond(), (o11, o21) -> {
                    String o1CanonicalPath = o11.getCanonicalPath();
                    String o2CanonicalPath = o21.getCanonicalPath();
                    if (o1CanonicalPath != null && o2CanonicalPath != null) {
                        return o1CanonicalPath.compareTo(o2CanonicalPath);
                    } else {
                        return 0;
                    }
                }).
                compare(o1.getSecond().getName(), o2.getSecond().getName()).
                result());

        if (!StringUtils.isEmpty(finalCleanFileNameWithoutExtension)) {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

            fileIndex.iterateContent(fileOrDir -> {
                if (!fileOrDir.isDirectory()) {
                    if (fileOrDir.getName().equalsIgnoreCase(finalCleanFileName)) {
                        sortedResults.add(new Pair<>(FIRST_ORDER, fileOrDir));

                    } else if (fileOrDir.getNameWithoutExtension().equalsIgnoreCase(finalCleanFileName)
                            || fileOrDir.getNameWithoutExtension().equalsIgnoreCase(finalCleanFileNameWithoutExtension)) {
                        if (fileOrDir.getFileType().equals(getElement().getContainingFile().getFileType())) {
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
