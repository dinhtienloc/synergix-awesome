package synergix.plugin.intellj.structure.node;

import com.intellij.icons.AllIcons.Nodes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class BeanNode extends NavigatableFileNode {
	public BeanNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, NavigatablePsiElement psiElement) {
		super(parent, project, myBuilder, psiElement);
		this.icon = Nodes.Class;
	}
}
