package synergix.plugin.intellj.structure.node;

import javax.swing.*;

import com.intellij.icons.AllIcons;
import com.intellij.icons.AllIcons.Nodes;
import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.dom.element.MenuGroupElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class MenuGroupNode extends SynergixTreeNode {
	private int moduleLevel;

	public MenuGroupNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, MenuGroupElement elem) {
		super(parent, project, myBuilder, elem);
		if (parent instanceof MenuGroupNode) {
			this.moduleLevel = ((MenuGroupNode) parent).getModuleLevel() + 1;
		} else if (parent instanceof ProjectNode) {
			this.moduleLevel = 1;
		}

		this.icon = this.resolveIconByLevel();
	}

	private int getModuleLevel() {
		return this.moduleLevel;
	}

	private Icon resolveIconByLevel() {
		switch (this.moduleLevel) {
			case 1:
			case 2:
				return AllIcons.Actions.GroupByModule;
			case 3:
				return Nodes.JavaModule;
			case 4:
				return Nodes.NodePlaceholder;
			default:
				return Nodes.EmptyNode;
		}
	}
}
