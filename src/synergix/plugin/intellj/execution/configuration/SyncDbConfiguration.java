package synergix.plugin.intellj.execution.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.runner.SyncDbRunnerState;

import java.util.ArrayList;
import java.util.List;

public class SyncDbConfiguration extends RunConfigurationBase {
    private String superModelDistDirectory;
    private String superModelStableDirectory;
    private String dbCommand;
    private String dbSchema;
    private String svnUser;
    private String svnPass;
    private boolean iAmHacker;
    private List<String> dbNames;

    public SyncDbConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        this.dbNames = new ArrayList<>();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SyncDbConfigurable(this.getProject());
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new SyncDbRunnerState(environment, this);
    }

    public String getSuperModelDistDirectory() {
        return this.superModelDistDirectory;
    }

    public void setSuperModelDistDirectory(String superModelDistDirectory) {
        this.superModelDistDirectory = superModelDistDirectory;
    }

    public String getSuperModelStableDirectory() {
        return this.superModelStableDirectory;
    }

    public void setSuperModelStableDirectory(String superModelStableDirectory) {
        this.superModelStableDirectory = superModelStableDirectory;
    }

    public String getDbCommand() {
        return this.dbCommand;
    }

    public void setDbCommand(String dbCommand) {
        this.dbCommand = dbCommand;
    }

    public String getDbSchema() {
        return this.dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public List<String> getDbNames() {
        return this.dbNames;
    }

    public void setDbNames(List<String> dbNames) {
        this.dbNames = dbNames;
    }

    public String getSvnUser() {
        return this.svnUser;
    }

    public void setSvnUser(String svnUser) {
        this.svnUser = svnUser;
    }

    public String getSvnPass() {
        return this.svnPass;
    }

    public void setSvnPass(String svnPass) {
        this.svnPass = svnPass;
    }

    public boolean isiAmHacker() {
        return this.iAmHacker;
    }

    public void setiAmHacker(boolean iAmHacker) {
        this.iAmHacker = iAmHacker;
    }
}
