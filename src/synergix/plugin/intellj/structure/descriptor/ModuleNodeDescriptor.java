package synergix.plugin.intellj.structure.descriptor;

import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.structure.node.ModuleNode;

public class ModuleNodeDescriptor extends SynergixNodeDescriptor {
	public ModuleNodeDescriptor(@Nullable Project project, @Nullable NodeDescriptor parentDescriptor, ModuleNode node) {
		super(project, parentDescriptor, node);
		this.myColor = JBColor.blue;
	}
	
}
