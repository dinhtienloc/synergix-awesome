package synergix.plugin.intellj.wm;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class SynergixScreenWindowFactory implements ToolWindowFactory, DumbAware {
	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		SynergixScreensPanel panel = new SynergixScreensPanel(project);
		final ContentManager contentManager = toolWindow.getContentManager();
		final Content content = contentManager.getFactory().createContent(panel, null, false);
		contentManager.addContent(content);
//		toolWindow.setHelpId(HelpID.ANT);
		Disposer.register(project, panel);
	}
}
