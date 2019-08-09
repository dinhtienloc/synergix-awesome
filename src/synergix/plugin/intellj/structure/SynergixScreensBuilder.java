package synergix.plugin.intellj.structure;

import javax.swing.*;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.ui.tree.StructureTreeModel;
import synergix.plugin.intellj.structure.node.ModuleNode;
import synergix.plugin.intellj.structure.node.ProjectNode;
import synergix.plugin.intellj.structure.node.SynergixTreeNode;

public class SynergixScreensBuilder {
	protected final Project myProject;
	private final JTree myTree;
	private SynergixScreensStructure myTreeStructure;
	private StructureTreeModel myModel;
	private boolean myDisposed;

	public SynergixScreensBuilder(JTree tree, Project project) {
		this.myTree = tree;
		this.myProject = project;
		this.myTreeStructure = new SynergixScreensStructure(this.myProject, this);
	}

	ProjectNode getRootNode() {
		ProjectNode root = new ProjectNode(this.myProject, this);
		// just a sample data to test tool window the first time
		root.addChild(new ModuleNode(root, this.myProject, this)).setMyName("Moddule1");
		root.addChild(new ModuleNode(root, this.myProject, this)).setMyName("Moddule2");
		return root;
	}
}
