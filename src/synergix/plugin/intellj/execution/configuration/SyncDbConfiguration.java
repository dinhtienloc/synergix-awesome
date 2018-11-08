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

public class SyncDbConfiguration extends RunConfigurationBase {
    private String superModelDistDirectory;
    private String superModelStableDirectory;

    public SyncDbConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SyncDbConfigurable(getProject());
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
        return superModelDistDirectory;
    }

    public void setSuperModelDistDirectory(String superModelDistDirectory) {
        this.superModelDistDirectory = superModelDistDirectory;
    }

    public String getSuperModelStableDirectory() {
        return superModelStableDirectory;
    }

    public void setSuperModelStableDirectory(String superModelStableDirectory) {
        this.superModelStableDirectory = superModelStableDirectory;
    }
}
