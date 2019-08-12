package synergix.plugin.intellj.structure.node;

import com.intellij.icons.AllIcons.FileTypes;
import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class XHTMLHeaderNode extends SynergixTreeNode {
	public XHTMLHeaderNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder) {
		super(parent, project, myBuilder);
		this.icon = FileTypes.Xhtml;
	}
}
