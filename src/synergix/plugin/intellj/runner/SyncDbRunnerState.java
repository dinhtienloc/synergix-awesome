package synergix.plugin.intellj.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.sun.javafx.binding.StringFormatter;
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
        List<String> errorMsgs = new ArrayList<>();

        if (this.syncDbConfiguration.getDbNames().isEmpty()) {
            errorMsgs.add("echo [Synergix] Sync Error: There is no database to sync.");
        }
        else if (this.syncDbConfiguration.getDbCommand().isEmpty()) {
            errorMsgs.add("echo [Synergix] Sync Error: There is no command action.");
        }
        else if (this.syncDbConfiguration.getDbSchema().isEmpty()) {
            errorMsgs.add("echo [Synergix] Sync Error: There is no schema to sync.");
        }
        else {
            if (RunnerUtil.createNonGUIExportSchemeBat(supermodelStableDir)) {
                bodies.add(RunnerUtil.NON_GUI_EXPORT_SCHEMA_FILE_NAME);
                bodies.add("echo Export schema successful");
                bodies.add("cd " + this.syncDbConfiguration.getSuperModelDistDirectory());

                String command = this.syncDbConfiguration.getDbCommand();
                String schema = this.syncDbConfiguration.getDbSchema();
                String syncTemplale = "(echo %s & echo. & java -jar SuperModel.jar --" + command + " --schema=" + schema + ".xml --db=%s --includeViews)";
                for (String db : this.syncDbConfiguration.getDbNames()) {
                    bodies.add(String.format(syncTemplale, db, db));
                }
            }
        }

        if (errorMsgs.isEmpty() && bodies.isEmpty()) {
            errorMsgs.add("echo [Synergix] Sync Error: Something is wrong. Can not sync!");
        }
        if (!errorMsgs.isEmpty())
            cmds.add(this.parseToCommand(errorMsgs));
        else
            cmds.add(this.parseToCommand(bodies));

        GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
        generalCommandLine.setCharset(Charset.forName("UTF-8"));
        generalCommandLine.setWorkDirectory(supermodelStableDir);

//        LogSetup.initLoggers();
        ProcessHandlerFactory factory = ProcessHandlerFactory.getInstance();
        OSProcessHandler processHandler = factory.createColoredProcessHandler(generalCommandLine);
        ConsoleView console = TextConsoleBuilderFactory.getInstance()
                .createBuilder(this.syncDbConfiguration.getProject()).getConsole();
        console.attachToProcess(processHandler);
//        if (!errorMsgs.isEmpty()) {
//            String errorMessage = String.join("\n", errorMsgs.toArray(new String[errorMsgs.size()]));
//            console.print(errorMessage, ConsoleViewContentType.ERROR_OUTPUT);
//        }
        ProcessTerminatedListener.attach(processHandler);
        return processHandler;
    }

    private String parseToCommand(List<String> msg) {
        return Strings.join("", msg.toArray(new String[msg.size()]), " && ", false);

    }
}
