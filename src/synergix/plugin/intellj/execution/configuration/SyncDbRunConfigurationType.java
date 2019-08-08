package synergix.plugin.intellj.execution.configuration;

import javax.swing.*;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.utils.SynergixIcons;

public class SyncDbRunConfigurationType implements ConfigurationType {

	@NotNull
	@Override
	public String getDisplayName() {
		return "Sync DB";
	}

	@Nls
	@Override
	public String getConfigurationTypeDescription() {
		return "Sync Synergix DB";
	}

	@Override
	public Icon getIcon() {
		return SynergixIcons.SynergixIcon;
	}

	@NotNull
	@Override
	public String getId() {
		return "SyncDB";
	}

	@Override
	public ConfigurationFactory[] getConfigurationFactories() {
		return new ConfigurationFactory[]{new SyncDbConfigurationFactory(this)};
	}
}
