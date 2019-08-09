package synergix.plugin.intellj.structure.descriptor;

import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.structure.node.ProjectNode;

public class ProjectNodeDescriptor extends SynergixNodeDescriptor {
	public ProjectNodeDescriptor(@Nullable Project project, @Nullable NodeDescriptor parentDescriptor, ProjectNode node) {
		super(project, parentDescriptor, node);
		this.myColor = JBColor.blue;
	}
}
