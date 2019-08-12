package synergix.plugin.intellj.dom.descriptor;

import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.dom.element.MenuElement;

public class MenuDescription<T extends MenuElement> extends DomFileDescription<T> {
	public MenuDescription(Class<T> rootElementClass, String rootTagName, @NotNull String... allPossibleRootTagNamespaces) {
		super(rootElementClass, rootTagName, allPossibleRootTagNamespaces);
	}
}
