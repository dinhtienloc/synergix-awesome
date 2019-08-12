package synergix.plugin.intellj.dom.element;

import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import synergix.plugin.intellj.dom.converter.MenuPathToXmlConverter;

public interface MenuIncludeElement extends MenuElement {
	@Attribute("href")
	@Convert(value = MenuPathToXmlConverter.class)
	GenericAttributeValue<XmlFile> getIncludedXmlFile();
}
