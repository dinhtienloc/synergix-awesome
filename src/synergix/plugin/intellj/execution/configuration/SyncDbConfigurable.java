package synergix.plugin.intellj.execution.configuration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.ui.components.JBLabel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyncDbConfigurable extends SettingsEditor<SyncDbConfiguration> implements PanelWithAnchor {
	private JPanel myWholePanel;

	private JBLabel mySuperModelDistDirectoryLabel;
	private TextFieldWithBrowseButton mySuperModelDistDirectory;

	private JBLabel mySuperModelStableDirectoryLabel;
	private TextFieldWithBrowseButton mySuperModelStableDirectory;

	private LabeledComponent<JTextField> myCommandOption;
	private LabeledComponent<JTextField> myDbListLabeledComponent;
	private LabeledComponent<JTextField> mySchemaOption;
	private LabeledComponent<JTextField> mySvnUser;
	private LabeledComponent<JTextField> mySvnPass;
	private JCheckBox myIamHackerCheckbox;
	private JCheckBox runWithoutExportingSchemaCheckBox;

	SyncDbConfigurable() {
		this.mySuperModelDistDirectoryLabel.setLabelFor(this.mySuperModelDistDirectory.getTextField());
		this.mySuperModelStableDirectoryLabel.setLabelFor(this.mySuperModelStableDirectory.getTextField());

		this.mySuperModelDistDirectory.addBrowseFolderListener("Choose Super Model's Dist Directory", null, null,
				FileChooserDescriptorFactory.createSingleFolderDescriptor());

		this.mySuperModelStableDirectory.addBrowseFolderListener("Choose Super Model's Stable Directory", null, null,
				FileChooserDescriptorFactory.createSingleFolderDescriptor());

		this.myCommandOption.getComponent().setText("sync");
		this.mySchemaOption.getComponent().setText("modmain");
		this.myIamHackerCheckbox.setSelected(true);

		this.mySuperModelDistDirectory.getTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SyncDbConfigurable.this.updateStableDirectory();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				SyncDbConfigurable.this.updateStableDirectory();
			}
		});

		this.mySuperModelStableDirectory.getTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SyncDbConfigurable.this.updateDistDirectory();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				SyncDbConfigurable.this.updateDistDirectory();
			}
		});
	}

	@Override
	protected void resetEditorFrom(@NotNull SyncDbConfiguration s) {
		this.myCommandOption.getComponent().setText(s.getDbCommand());
		this.mySchemaOption.getComponent().setText(s.getDbSchema());
		this.myDbListLabeledComponent.getComponent().setText(s.getDbList());
		this.mySuperModelDistDirectory.setText(s.getSuperModelDistDirectory());
		this.mySuperModelStableDirectory.setText(s.getSuperModelStableDirectory());
		this.myIamHackerCheckbox.setSelected(s.isiAmHacker());
		this.runWithoutExportingSchemaCheckBox.setSelected(s.isRunWithoutExportingSchema());
	}

	@Override
	protected void applyEditorTo(@NotNull SyncDbConfiguration s) {
		s.setSuperModelDistDirectory(this.mySuperModelDistDirectory.getText());
		s.setSuperModelStableDirectory(this.mySuperModelStableDirectory.getText());
		s.setDbCommand(this.myCommandOption.getComponent().getText());
		s.setDbSchema(this.mySchemaOption.getComponent().getText());
		s.setSvnUser(this.mySvnUser.getComponent().getText());
		s.setSvnPass(this.mySvnPass.getComponent().getText());
		s.setDbList(this.myDbListLabeledComponent.getComponent().getText());
		s.setiAmHacker(this.myIamHackerCheckbox.isSelected());
		s.setRunWithoutExportingSchema(this.runWithoutExportingSchemaCheckBox.isSelected());
	}

	@NotNull
	@Override
	protected JComponent createEditor() {
		return this.myWholePanel;
	}

	@Override
	public JComponent getAnchor() {
		return null;
	}

	@Override
	public void setAnchor(@Nullable JComponent anchor) {
		this.mySuperModelDistDirectoryLabel.setAnchor(anchor);
		this.mySuperModelStableDirectoryLabel.setAnchor(anchor);
	}

	private void updateStableDirectory() {
		if (StringUtils.isEmpty(this.mySuperModelStableDirectory.getText())) {
			String distPath = this.mySuperModelDistDirectory.getText();
			int index = FilenameUtils.indexOfLastSeparator(distPath);
			String stableDirectory = distPath.substring(0, index + 1) + "stable";
			this.mySuperModelStableDirectory.setText(stableDirectory);
			try (FileInputStream inputStream = new FileInputStream(stableDirectory + File.separator + "settings.ini")) {
				Properties prop = new Properties();
				prop.load(inputStream);
				this.mySvnUser.getComponent().setText(prop.getProperty("svn.user"));
				this.mySvnPass.getComponent().setText(prop.getProperty("svn.pass"));
				this.myIamHackerCheckbox.setSelected("Y".equals(prop.getProperty("iamahacker")));

			} catch (Exception ignored) {
			}

		}
	}

	private void updateDistDirectory() {
		if (StringUtils.isEmpty(this.mySuperModelDistDirectory.getText())) {
			String stablePath = this.mySuperModelStableDirectory.getText();
			int index = FilenameUtils.indexOfLastSeparator(stablePath);
			this.mySuperModelDistDirectory.setText(stablePath.substring(0, index + 1) + "dist");
		}
	}

	private void createUIComponents() {
	}
}
