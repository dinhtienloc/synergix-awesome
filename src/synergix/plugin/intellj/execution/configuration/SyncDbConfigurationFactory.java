package synergix.plugin.intellj.execution.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SyncDbConfigurationFactory extends ConfigurationFactory {
	private static final String FACTORY_NAME = "Sync configuration factory";

	SyncDbConfigurationFactory(@NotNull ConfigurationType type) {
		super(type);
	}

	@NotNull
	@Override
	public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
		return new SyncDbConfiguration(project, this, "Sync DB");
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}
}
