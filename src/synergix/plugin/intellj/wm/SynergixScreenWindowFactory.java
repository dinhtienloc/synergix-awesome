package synergix.plugin.intellj.wm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class SynergixScreenWindowFactory implements ToolWindowFactory {
	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		SynergixScreensPanel panel = new SynergixScreensPanel(project, toolWindow);
		final ContentManager contentManager = toolWindow.getContentManager();
		final Content content = contentManager.getFactory().createContent(panel, null, true);
		content.setCloseable(false);
		contentManager.addContent(content);
		Disposer.register(contentManager, panel);
	}
}
