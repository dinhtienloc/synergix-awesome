package synergix.plugin.intellj.structure.node;

import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.dom.element.MenuElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class MenuItemNode extends SynergixTreeNode {
	public MenuItemNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, MenuElement menuElement) {
		super(parent, project, myBuilder, menuElement);
	}
}
