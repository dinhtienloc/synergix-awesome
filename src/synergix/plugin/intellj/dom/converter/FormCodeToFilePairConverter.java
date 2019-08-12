package synergix.plugin.intellj.dom.converter;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiFile;

public class FormCodeToFilePairConverter extends StringToObjectConverter<Pair<String, PsiFile>> {
	private String formCode;
	@Override
	protected String getRealPath(String rawPath) {
		this.formCode = rawPath;
		return "_" + rawPath + ".xhtml";
	}

	@Override
	protected Pair<String, PsiFile> convertToAcceptedType(Object data) {
		return new Pair<>(this.formCode, (PsiFile) data);
	}
}
