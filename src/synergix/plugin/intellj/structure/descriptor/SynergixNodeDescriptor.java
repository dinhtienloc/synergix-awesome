package synergix.plugin.intellj.structure.descriptor;

import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.CellAppearanceEx;
import com.intellij.ui.HtmlListCellRenderer;
import com.intellij.ui.SimpleColoredComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.structure.node.SynergixTreeNode;

public class SynergixNodeDescriptor extends NodeDescriptor implements CellAppearanceEx {
	private SynergixTreeNode node;

	public SynergixNodeDescriptor(@Nullable Project project, @Nullable NodeDescriptor parentDescriptor, SynergixTreeNode node) {
		super(project, parentDescriptor);
		this.node = node;
		this.myName = node.getMyName();
		this.setIcon(node.getIcon());
	}

	@Override
	public boolean update() {
		return false;
	}

	@Override
	public Object getElement() {
		return this.node;
	}

	@NotNull
	@Override
	public String getText() {
		return this.node.getMyName();
	}

	@Override
	public void customize(@NotNull HtmlListCellRenderer htmlListCellRenderer) {

	}

	@Override
	public void customize(@NotNull SimpleColoredComponent simpleColoredComponent) {

	}
}
