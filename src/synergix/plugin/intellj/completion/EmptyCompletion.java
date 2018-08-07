package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;

import java.util.ArrayList;
import java.util.List;

public class EmptyCompletion extends SynCompletion {
	public EmptyCompletion(CompletionParameters parameters) {
		super(parameters);
	}

	@Override
	public List<LookupElement> createLookupElementList() {
		return new ArrayList<>();
	}
}
