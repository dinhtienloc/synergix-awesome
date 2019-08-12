package synergix.plugin.intellj.structure.node;

import com.intellij.icons.AllIcons.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class XHTMLNode extends WebFileNode {
	public XHTMLNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, NavigatablePsiElement psiElement) {
		super(parent, project, myBuilder, psiElement);
		this.icon = FileTypes.Xhtml;
	}
}
