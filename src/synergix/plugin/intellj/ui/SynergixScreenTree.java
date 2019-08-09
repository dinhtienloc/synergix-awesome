package synergix.plugin.intellj.ui;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import com.intellij.icons.AllIcons;
import com.intellij.ui.treeStructure.SimpleTree;

public class SynergixScreenTree extends SimpleTree {
	private final JTextPane myPane = new JTextPane();

	{
		this.myPane.setOpaque(false);
		String addIconText = "'+'";
		String refreshIconText = "'Reimport'";
		String message = "nothing to display";
		int firstEol = message.indexOf("\n");
		int addIconMarkerIndex = message.indexOf(addIconText);
		this.myPane.replaceSelection(message.substring(0, addIconMarkerIndex));
		this.myPane.insertIcon(AllIcons.General.Add);
		int refreshIconMarkerIndex = message.indexOf(refreshIconText);
		this.myPane.replaceSelection(message.substring(addIconMarkerIndex + addIconText.length(), refreshIconMarkerIndex));
		this.myPane.insertIcon(AllIcons.Actions.Refresh);
		this.myPane.replaceSelection(message.substring(refreshIconMarkerIndex + refreshIconText.length()));

		StyledDocument document = this.myPane.getStyledDocument();
		SimpleAttributeSet centerAlignment = new SimpleAttributeSet();
		StyleConstants.setAlignment(centerAlignment, StyleConstants.ALIGN_CENTER);
		SimpleAttributeSet justifiedAlignment = new SimpleAttributeSet();
		StyleConstants.setAlignment(justifiedAlignment, StyleConstants.ALIGN_JUSTIFIED);

		document.setParagraphAttributes(0, firstEol, centerAlignment, false);
		document.setParagraphAttributes(firstEol + 2, document.getLength(), justifiedAlignment, false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.myPane.setFont(this.getFont());
		this.myPane.setBackground(this.getBackground());
		this.myPane.setForeground(this.getForeground());
		Rectangle bounds = this.getBounds();
		this.myPane.setBounds(0, 0, bounds.width - 10, bounds.height);

		Graphics g2 = g.create(bounds.x + 10, bounds.y + 20, bounds.width, bounds.height);
		try {
			this.myPane.paint(g2);
		} finally {
			g2.dispose();
		}
	}
}
