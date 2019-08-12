package synergix.plugin.intellj.structure.node;

import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class NavigatableFileNode extends SynergixTreeNode {
	private NavigatablePsiElement navigatablePsiElement;

	public NavigatableFileNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, NavigatablePsiElement psiElement) {
		super(parent, project, myBuilder);
		this.navigatablePsiElement = psiElement;
	}

	public NavigatablePsiElement getNavigatablePsiElement() {
		return this.navigatablePsiElement;
	}
}
