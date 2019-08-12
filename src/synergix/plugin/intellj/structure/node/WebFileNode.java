package synergix.plugin.intellj.structure.node;

import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class WebFileNode extends NavigatableFileNode {
	public WebFileNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, NavigatablePsiElement psiElement) {
		super(parent, project, myBuilder, psiElement);
	}
}
