package synergix.plugin.intellj.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.yourkit.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.cmdline.LogSetup;
import synergix.plugin.intellj.execution.configuration.SyncDbConfiguration;
import synergix.plugin.intellj.utils.RunnerUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyncDbRunnerState extends CommandLineState {
    private SyncDbConfiguration syncDbConfiguration;

    public SyncDbRunnerState(@NotNull ExecutionEnvironment environment, final SyncDbConfiguration syncDbConfiguration) {
        super(environment);
        this.syncDbConfiguration = syncDbConfiguration;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        String supermodelStableDir = this.syncDbConfiguration.getSuperModelStableDirectory();
        List<String> cmds = new ArrayList<>(Arrays.asList("cmd", "/c"));
        List<String> bodies = new ArrayList<>();

        if (RunnerUtil.createNonGUIExportSchemeBat(supermodelStableDir)) {
            bodies.add(RunnerUtil.NON_GUI_EXPORT_SCHEMA_FILE_NAME);
            bodies.add("echo Export schema successful");
            bodies.add("cd " + this.syncDbConfiguration.getSuperModelDistDirectory());
            bodies.add("(echo plugintest & echo. & java -jar SuperModel.jar --sync --schema=modmain.xml --db=testplugin --includeViews)");
            bodies.add("echo Finished! Sync success");
        }

        if (bodies.isEmpty()) {
            bodies.add("echo Error when sync Synergix database using Intellij");
        }


        String finalBody = Strings.join("", bodies.toArray(new String[bodies.size()]), " && ", false);
        cmds.add(finalBody);

        GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
        generalCommandLine.setCharset(Charset.forName("UTF-8"));
        generalCommandLine.setWorkDirectory(supermodelStableDir);

        LogSetup.initLoggers();
        ProcessHandlerFactory factory = ProcessHandlerFactory.getInstance();
        OSProcessHandler processHandler = factory.createColoredProcessHandler(generalCommandLine);
        ProcessTerminatedListener.attach(processHandler);
        return processHandler;
    }
}
