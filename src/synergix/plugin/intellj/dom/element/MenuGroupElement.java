package synergix.plugin.intellj.dom.element;

import java.util.List;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

public interface MenuGroupElement extends MenuElement {
	@Attribute("label")
	GenericAttributeValue<String> getLabel();

	@Attribute("wide")
	GenericAttributeValue<Boolean> getWideStatus();

	@SubTagList("menu-group")
	List<MenuGroupElement> getChildrenGroups();

	@SubTagList("menu-include")
	List<MenuIncludeElement> getDeclaredIncludes();

	@SubTagList("menu-item")
	List<MenuItemElement> getDeclaredItems();
}
