package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberTypeCompletion extends SynCompletion {
	public NumberTypeCompletion(CompletionParameters parameters) {
		super(parameters);
	}

	@Override
	public List<LookupElement> createLookupElementList() {
		return new ArrayList<>(Arrays.asList(LookupElementBuilder.create("Hello")));
	}
}
