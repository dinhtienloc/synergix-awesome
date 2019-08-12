package synergix.plugin.intellj.actions;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import com.intellij.ide.hierarchy.HierarchyBrowserBase;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.utils.SynUtil;
import synergix.plugin.intellj.wm.SynergixScreensPanel;

public class ScreenReloadAction extends AnAction implements DumbAware {
	@Override
	public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
		FileDocumentManager.getInstance().saveAllDocuments();
		Project project = ProjectManager.getInstance().getOpenProjects()[0];

		ToolWindow tw = ToolWindowManager.getInstance(project).getToolWindow("Synergix Screen");
		ContentManager cm = tw.getContentManager();
		Content content = tw.getContentManager().getSelectedContent();


		if (content != null) {

			// dispose current panel component
			Component component = content.getComponent();
			Disposer.dispose((Disposable)component);

			// create new panel
			SynergixScreensPanel newPanel = new SynergixScreensPanel(project, tw);

			// reset new panel to current content
			content.setComponent(newPanel);
			cm.setSelectedContent(content);

			Disposer.register(cm, newPanel);
		}
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		Presentation p = e.getPresentation();
		p.setEnabled(this.isAvailable(e));
		p.setVisible(this.isVisible(e));
	}

	protected boolean isAvailable(@NotNull AnActionEvent e) {
		return SynUtil.hasTH6Project(e.getDataContext());
	}

	protected boolean isVisible(@NotNull AnActionEvent e) {
		return true;
	}
}
