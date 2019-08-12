package synergix.plugin.intellj.structure.node;

import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;
import synergix.plugin.intellj.utils.SynergixIcons;

public class ProjectNode extends SynergixTreeNode {
	public ProjectNode(Project project, SynergixScreensBuilder builder) {
		super(null, project, builder, null);
		this.icon = SynergixIcons.SynergixIcon;
	}
}
