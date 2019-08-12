package synergix.plugin.intellj.structure.node;

import com.intellij.icons.AllIcons.Nodes;
import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class BeanHeaderNode extends SynergixTreeNode {
	public BeanHeaderNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder) {
		super(parent, project, myBuilder);
		this.icon = Nodes.Class;
	}
}
