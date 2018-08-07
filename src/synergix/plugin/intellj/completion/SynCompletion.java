package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SynCompletion {
	private CompletionParameters parameters;

	public SynCompletion(CompletionParameters parameters) {
		this.parameters = parameters;
	}

	public abstract List<LookupElement> createLookupElementList();

	public CompletionParameters getParameters() {
		return parameters;
	}
}
