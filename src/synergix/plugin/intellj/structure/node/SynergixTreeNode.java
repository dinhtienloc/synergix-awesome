package synergix.plugin.intellj.structure.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleNode;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public abstract class SynergixTreeNode {
	Project myProject;
	SynergixTreeNode myParent;
	List<SynergixTreeNode> myChildren;
	String myName;
	SynergixScreensBuilder myBuilder;


	public SynergixTreeNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder myBuilder) {
		this.myParent = parent;
		this.myProject = project;
		this.myBuilder = myBuilder;
		this.myChildren = new ArrayList<>();
	}

	public SynergixTreeNode addChild(SynergixTreeNode node) {
		this.myChildren.add(node);
		return node;
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
	}

	public List<SynergixTreeNode> getMyChildren() {
		return this.myChildren;
	}

	public String getMyName() {
		return this.myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}
}
