package synergix.plugin.intellj.wm;

import javax.swing.*;

import com.intellij.ide.TreeExpander;
import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;
import synergix.plugin.intellj.structure.SynergixScreensStructure;

public class SynergixScreensPanel extends SimpleToolWindowPanel implements DataProvider, Disposable {
	private Project myProject;
	private Tree myTree;
	private final SynergixScreensStructure myTreeStructure;
	private final SynergixScreensBuilder myTreeBuilder;
	private StructureTreeModel myTreeModel;

	private final TreeExpander myTreeExpander = new TreeExpander() {
		@Override
		public void expandAll() {
			TreeUtil.expandAll(SynergixScreensPanel.this.myTree);
		}

		@Override
		public void collapseAll() {
			TreeUtil.collapseAll(SynergixScreensPanel.this.myTree, 1);
		}

		@Override
		public boolean canExpand() {
			return true;
		}

		@Override
		public boolean canCollapse() {
			return true;
		}
	};

	public SynergixScreensPanel(@NotNull Project project) {
		super(true, true);
		this.myProject = project;
		this.myTreeBuilder = new SynergixScreensBuilder(this.myTree, this.myProject);
		this.myTreeStructure = new SynergixScreensStructure(project, this.myTreeBuilder);

		final StructureTreeModel treeModel = new StructureTreeModel<>(this.myTreeStructure);

		this.myTreeModel = treeModel;
		this.myTree = new Tree(new AsyncTreeModel(treeModel, this));

		this.myTree.setRootVisible(false);
		this.myTree.setShowsRootHandles(true);
		this.myTree.setCellRenderer(new NodeRenderer());

		final ActionManager actionManager = ActionManager.getInstance();
		ActionToolbar actionToolbar = actionManager.createActionToolbar("Synergix Screen Navigator Toolbar",
				(DefaultActionGroup)actionManager
						.getAction("Synergix.ScreenNavigtorToolbar"),
				true);

		actionToolbar.setTargetComponent(this.myTree);
		this.setToolbar(actionToolbar.getComponent());
		this.setContent(ScrollPaneFactory.createScrollPane(this.myTree));
	}

	@Override
	public void dispose() {
		this.myTree = null;
		this.myTreeModel = null;
		this.myProject = null;
	}
}
