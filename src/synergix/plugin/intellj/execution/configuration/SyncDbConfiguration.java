package synergix.plugin.intellj.execution.configuration;

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.runner.SyncDbRunnerState;

public class SyncDbConfiguration extends RunConfigurationBase {

	private SyncDbConfigurationOptions syncDbOptions;

	SyncDbConfiguration(Project project, ConfigurationFactory factory, String name) {
		super(project, factory, name);
		this.syncDbOptions = new SyncDbConfigurationOptions();
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
		return new SyncDbConfigurable();
	}

	@Override
	public void checkConfiguration() {
	}

	@Override
	public void readExternal(@NotNull Element element) throws InvalidDataException {
		this.syncDbOptions = XmlSerializer.deserialize(element, SyncDbConfigurationOptions.class);
	}

	@Override
	public void writeExternal(@NotNull Element element) {
		XmlSerializer.serializeObjectInto(this.syncDbOptions, element);
	}

	@Nullable
	@Override
	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
		return new SyncDbRunnerState(environment, this);
	}

	public String getSuperModelDistDirectory() {
		return this.syncDbOptions.superModelDistDirectory;
	}

	void setSuperModelDistDirectory(String superModelDistDirectory) {
		this.syncDbOptions.superModelDistDirectory = superModelDistDirectory;
	}

	public String getSuperModelStableDirectory() {
		return this.syncDbOptions.superModelStableDirectory;
	}

	void setSuperModelStableDirectory(String superModelStableDirectory) {
		this.syncDbOptions.superModelStableDirectory = superModelStableDirectory;
	}

	public String getDbCommand() {
		return this.syncDbOptions.dbCommand;
	}

	void setDbCommand(String dbCommand) {
		this.syncDbOptions.dbCommand = dbCommand;
	}

	public String getDbSchema() {
		return this.syncDbOptions.dbSchema;
	}

	void setDbSchema(String dbSchema) {
		this.syncDbOptions.dbSchema = dbSchema;
	}

	public String getSvnUser() {
		return this.syncDbOptions.svnUser;
	}

	void setSvnUser(String svnUser) {
		this.syncDbOptions.svnUser = svnUser;
	}

	public String getSvnPass() {
		return this.syncDbOptions.svnPass;
	}

	void setSvnPass(String svnPass) {
		this.syncDbOptions.svnPass = svnPass;
	}

	public boolean isiAmHacker() {
		return this.syncDbOptions.iAmHacker == null ? false : this.syncDbOptions.iAmHacker;
	}

	void setiAmHacker(boolean iAmHacker) {
		this.syncDbOptions.iAmHacker = iAmHacker;
	}

	public String getDbList() {
		return this.syncDbOptions.dbList;
	}

	void setDbList(String dbList) {
		this.syncDbOptions.dbList = dbList;
	}

	public String getRunType() {
		return this.syncDbOptions.runType;
	}

	void setRunType(String runType) {
		this.syncDbOptions.runType = runType;
	}
}
