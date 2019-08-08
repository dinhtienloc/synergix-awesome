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

	public SyncDbConfiguration(Project project, ConfigurationFactory factory, String name) {
		super(project, factory, name);
	}

	public SyncDbConfigurationOptions getSyncDbOptions() {
		return this.syncDbOptions;
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
		return new SyncDbConfigurable(this.getProject());
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
	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
		return new SyncDbRunnerState(environment, this);
	}

	public String getSuperModelDistDirectory() {
		return this.syncDbOptions.superModelDistDirectory;
	}

	public void setSuperModelDistDirectory(String superModelDistDirectory) {
		this.syncDbOptions.superModelDistDirectory = superModelDistDirectory;
	}

	public String getSuperModelStableDirectory() {
		return this.syncDbOptions.superModelStableDirectory;
	}

	public void setSuperModelStableDirectory(String superModelStableDirectory) {
		this.syncDbOptions.superModelStableDirectory = superModelStableDirectory;
	}

	public String getDbCommand() {
		return this.syncDbOptions.dbCommand;
	}

	public void setDbCommand(String dbCommand) {
		this.syncDbOptions.dbCommand = dbCommand;
	}

	public String getDbSchema() {
		return this.syncDbOptions.dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.syncDbOptions.dbSchema = dbSchema;
	}

	public String getSvnUser() {
		return this.syncDbOptions.svnUser;
	}

	public void setSvnUser(String svnUser) {
		this.syncDbOptions.svnUser = svnUser;
	}

	public String getSvnPass() {
		return this.syncDbOptions.svnPass;
	}

	public void setSvnPass(String svnPass) {
		this.syncDbOptions.svnPass = svnPass;
	}

	public boolean isiAmHacker() {
		return this.syncDbOptions.iAmHacker;
	}

	public void setiAmHacker(boolean iAmHacker) {
		this.syncDbOptions.iAmHacker = iAmHacker;
	}

	public String getDbList() {
		return this.syncDbOptions.dbList;
	}

	public void setDbList(String dbList) {
		this.syncDbOptions.dbList = dbList;
	}
}
