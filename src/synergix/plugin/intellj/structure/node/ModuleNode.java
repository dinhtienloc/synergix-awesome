package synergix.plugin.intellj.structure.node;

import java.util.ArrayList;

import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class ModuleNode extends SynergixTreeNode {
	public ModuleNode(SynergixTreeNode parent, Project project, SynergixScreensBuilder builder) {
		super(parent, project, builder);
	}

}
