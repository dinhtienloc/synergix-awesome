package synergix.plugin.intellj.structure.node;

import java.util.List;

import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class ProjectNode extends SynergixTreeNode {
	public ProjectNode(Project project, SynergixScreensBuilder builder) {
		super(null, project, builder);
	}
}
