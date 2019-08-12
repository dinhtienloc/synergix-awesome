package synergix.plugin.intellj.dom.element;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiFile;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import synergix.plugin.intellj.dom.converter.FormCodeToFilePairConverter;

public interface MenuItemElement extends MenuElement {
	@Attribute("description")
	GenericAttributeValue<String> getDescription();

	@Attribute("form-code")
	@Convert(value = FormCodeToFilePairConverter.class)
	GenericAttributeValue<Pair<String, PsiFile>> getForm();
}
