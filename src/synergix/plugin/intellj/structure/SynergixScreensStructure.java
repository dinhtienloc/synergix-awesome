package synergix.plugin.intellj.structure;

import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.util.ArrayUtilRt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.structure.descriptor.SynergixNodeDescriptor;
import synergix.plugin.intellj.structure.node.ProjectNode;
import synergix.plugin.intellj.structure.node.SynergixTreeNode;

public class SynergixScreensStructure extends AbstractTreeStructure {
	private static final Logger LOG = Logger.getInstance(SynergixScreensStructure.class);

	private final Project myProject;
	private SynergixTreeNode myRoot;


	public SynergixScreensStructure(Project project, SynergixScreensBuilder builder) {
		this.myProject = project;
		this.myRoot = builder != null ? builder.getRootNode() : null;
	}

	@NotNull
	@Override
	public Object getRootElement() {
		return this.myRoot;
	}

	@NotNull
	@Override
	public Object[] getChildElements(@NotNull Object element) {
		if (element instanceof String) {
			System.out.println(element);
			return ArrayUtilRt.EMPTY_OBJECT_ARRAY;
		} else return ((SynergixTreeNode) element).getMyChildren().toArray(new SynergixTreeNode[0]);
	}

	@Nullable
	@Override
	public Object getParentElement(@NotNull Object element) {
		return ((SynergixTreeNode) element).getMyParent();
	}

	@NotNull
	@Override
	public NodeDescriptor createDescriptor(@NotNull Object element, @Nullable NodeDescriptor parentDescriptor) {
//		if (element == this.myRoot) {
//			return new ProjectNodeDescriptor(this.myProject, parentDescriptor, (ProjectNode) element);
//		}
//
//		if (element instanceof MenuGroupNode) {
//			return new MenuGroupDescriptor(this.myProject, parentDescriptor, (MenuGroupNode) element);
//		}
//		if (element instanceof ModuleNode) {
//			return new ModuleNodeDescriptor(this.myProject, parentDescriptor, (ModuleNode) element);
//		}


		if (element instanceof SynergixTreeNode) {
			return new SynergixNodeDescriptor(this.myProject, parentDescriptor, (SynergixTreeNode) element);
		}

		LOG.error("Unknown element for this tree structure " + element);
		return null;
	}

	@Override
	public void commit() {
		PsiDocumentManager.getInstance(this.myProject).commitAllDocuments();
	}

	@Override
	public boolean hasSomethingToCommit() {
		return PsiDocumentManager.getInstance(this.myProject).hasUncommitedDocuments();
	}
}
