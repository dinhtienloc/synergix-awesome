package synergix.plugin.intellj.structure.node;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.dom.element.MenuElement;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class SynergixTreeNode {
	private static final Logger LOG = Logger.getInstance(SynergixTreeNode.class);

	Project myProject;
	SynergixTreeNode myParent;
	List<SynergixTreeNode> myChildren;
	String myName;
	SynergixScreensBuilder myBuilder;
	MenuElement menuElement;
	Icon icon;

	public SynergixTreeNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder) {
		this(parent, project, myBuilder, null);
	}

	public SynergixTreeNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder, MenuElement menuElement) {
		this.myParent = parent;
		this.myProject = project;
		this.myBuilder = myBuilder;
		this.menuElement = menuElement;
		this.myChildren = new ArrayList<>();
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public void addChild(SynergixTreeNode node) {
		this.myChildren.add(node);
	}

	public void addChildren(Collection<? extends SynergixTreeNode> nodes) {
		for (SynergixTreeNode node : nodes) {
			node.setMyParent(this);
		}
	}

	public Project getMyProject() {
		return this.myProject;
	}

	public void setMyProject(Project myProject) {
		this.myProject = myProject;
	}

	public SynergixTreeNode getMyParent() {
		return this.myParent;
	}

	public void setMyParent(SynergixTreeNode myParent) {
		this.myParent = myParent;
		myParent.addChild(this);
	}

	public List<SynergixTreeNode> getMyChildren() {
		return this.myChildren;
	}

	public String getMyName() {
		return this.myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
		LOG.info("create " + this.getClass().getSimpleName() + " with name " + myName);
	}

	public MenuElement getMenuElement() {
		return this.menuElement;
	}

	public Icon getIcon() {
		return this.icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}
}
