package synergix.plugin.intellj.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.utils.SynUtil;

public class ScreenReloadAction extends AnAction implements DumbAware {
	@Override
	public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
		FileDocumentManager.getInstance().saveAllDocuments();
		// reload all screens
		System.out.println("Reload Synergix Screens");
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
