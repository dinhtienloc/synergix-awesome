package synergix.plugin.intellj.dom.descriptor;

import synergix.plugin.intellj.dom.element.MenuGroupElement;

public class MenuGroupDescription extends MenuDescription<MenuGroupElement> {
	public MenuGroupDescription() {
		super(MenuGroupElement.class, "menu-group");
	}
}
