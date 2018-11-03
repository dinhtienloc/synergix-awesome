package synergix.plugin.intellj.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import java.util.List;

public class NumberTypeCompletion extends DataTypeCompletition {

	public NumberTypeCompletion(CompletionParameters parameters) {
		super(parameters);
	}

	@Override
	public List<LookupElement> createLookupElementList() {
		return this.getDefaultDataType();
	}


}
