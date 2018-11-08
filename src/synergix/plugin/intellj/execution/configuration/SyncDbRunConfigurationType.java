package synergix.plugin.intellj.execution.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import synergix.plugin.intellj.utils.SynergixIcons;

public class SyncDbRunConfigurationType extends ConfigurationTypeBase {
    public SyncDbRunConfigurationType() {
        super("SyncDB", "Sync DB", "Sync Synergix DB", SynergixIcons.SynergixIcon);
        addFactory(new ConfigurationFactory(this) {
            @Override
            public RunConfiguration createTemplateConfiguration(Project project) {
                return new SyncDbConfiguration(project, this, "Sync DB");
            }
        });
    }
}
