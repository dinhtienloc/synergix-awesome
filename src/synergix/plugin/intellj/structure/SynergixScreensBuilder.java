package synergix.plugin.intellj.structure;

import java.util.List;
import java.util.Set;

import com.intellij.icons.AllIcons.Nodes;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import synergix.plugin.intellj.dom.element.MenuGroupElement;
import synergix.plugin.intellj.dom.element.MenuIncludeElement;
import synergix.plugin.intellj.dom.element.MenuItemElement;
import synergix.plugin.intellj.dom.reader.XHTMLFileReader;
import synergix.plugin.intellj.structure.node.BeanNode;
import synergix.plugin.intellj.structure.node.MenuGroupNode;
import synergix.plugin.intellj.structure.node.MenuItemNode;
import synergix.plugin.intellj.structure.node.ProjectNode;
import synergix.plugin.intellj.structure.node.SynergixTreeNode;
import synergix.plugin.intellj.structure.node.WebFileNode;
import synergix.plugin.intellj.structure.node.XHTMLNode;
import synergix.plugin.intellj.utils.SynUtil;

public class SynergixScreensBuilder {
	private static final Logger LOG = Logger.getInstance(SynergixScreensBuilder.class);

	private final Project myProject;
	private SynergixScreensStructure myTreeStructure;
	private final DomManager domManager;

	public SynergixScreensBuilder(Project project) {
		this.myProject = project;
		this.domManager = DomManager.getDomManager(this.myProject);
		this.myTreeStructure = new SynergixScreensStructure(this.myProject, this);
	}

	SynergixTreeNode getRootNode() {
		Module th6Module = SynUtil.getModuleByName(this.myProject, "TH6");
		if (th6Module == null) {
			return null;
		}

		PsiFile[] psiMenuXml = FilenameIndex.getFilesByName(this.myProject, "menu.xml", GlobalSearchScope.moduleScope(th6Module));
		if (psiMenuXml.length == 0) {
			return null;
		}
		XmlFile menuXmlFile = (XmlFile) psiMenuXml[0];
		final DomFileElement<MenuGroupElement> element = this.domManager.getFileElement(menuXmlFile, MenuGroupElement.class);

		if (element == null) return null;

		final MenuGroupElement root = element.getRootElement();
		SynergixTreeNode rootNode = this.buildMenuGroupNode(null, root);
		return rootNode;
	}

	private SynergixTreeNode buildMenuGroupNode(SynergixTreeNode parent, MenuGroupElement element) {
		SynergixTreeNode realParent;
		if (parent == null) {
			realParent = new ProjectNode(this.myProject, this);
			realParent.setMyName("Synergix Screens");
		} else {
			// ignore menu group which has wide=true (Business Formats and Reports)
			final Boolean isWide = element.getWideStatus().getValue();
			if (isWide != null && isWide) {
				return null;
			}

			realParent = new MenuGroupNode(parent, this.myProject, this, element);
			realParent.setMyName(element.getLabel().getStringValue());
		}

		for (MenuGroupElement menuGroupChild : element.getChildrenGroups()) {
//			LOG.info("Build tree for menu group tag: " + menuGroupChild.getXmlTag().getAttributeValue("label"));
			this.buildMenuGroupNode(realParent, menuGroupChild);
		}

//		LOG.info("Build tree for list of include tag: " + element.getXmlElement());
		for (MenuIncludeElement includedChild : element.getDeclaredIncludes()) {
//			LOG.info("Build tree for included tag: " + includedChild.getIncludedXmlFile().getValue());
			this.buildIncludedNode(realParent, includedChild);
		}

		for (MenuItemElement menuItem : element.getDeclaredItems()) {
//			LOG.info("Build tree for menu item tag: " + menuItem.getXmlTag().getAttributeValue("form-code"));
			this.buildMenuItemNode(realParent, menuItem);
		}

		return realParent;
	}

	private void buildIncludedNode(SynergixTreeNode parent, MenuIncludeElement elem) {
		XmlFile includedFile = elem.getIncludedXmlFile().getValue();
		if (includedFile == null) {
			return;
		}

		final DomFileElement<MenuGroupElement> element = this.domManager.getFileElement(includedFile, MenuGroupElement.class);
		if (element == null) return;
		final MenuGroupElement root = element.getRootElement();
		this.buildMenuGroupNode(parent, root);
	}

	private void buildMenuItemNode(SynergixTreeNode parent, MenuItemElement element) {
		MenuItemNode node = new MenuItemNode(parent, this.myProject, this, element);
		String desc = element.getDescription().getValue();
		Pair<String, PsiFile> formDataPair = element.getForm().getValue();

		if (formDataPair != null) {
			node.setMyName(desc + " (" + formDataPair.getFirst() + ")");

			if (formDataPair.getSecond() != null) {
				SynergixTreeNode beanNode = new SynergixTreeNode(node, this.myProject, this);
				beanNode.setMyName("Beans");
				beanNode.setIcon(Nodes.Ejb);

				SynergixTreeNode xhtmlNode = new SynergixTreeNode(node, this.myProject, this);
				xhtmlNode.setMyName("Web Pages");
				xhtmlNode.setIcon(Nodes.PpWeb);

				this.buildTreeFromXhtmlNode(beanNode, xhtmlNode, formDataPair.getSecond());
			}
		}
	}

	private void buildTreeFromXhtmlNode(SynergixTreeNode beanNode, SynergixTreeNode webPageNode, PsiFile xhtmlFile) {
		XHTMLNode xhtmlNode = new XHTMLNode(webPageNode, this.myProject, this, xhtmlFile);
		xhtmlNode.setMyName(xhtmlFile.getName());

		XHTMLFileReader xhtmlReader = new XHTMLFileReader(this.myProject, (XmlFile) xhtmlFile, this);
		xhtmlReader.buildNode();
		List<WebFileNode> webFileNodes = xhtmlReader.getWebFileNodes();
		xhtmlNode.addChildren(webFileNodes);

		Set<BeanNode> beanNodes = xhtmlReader.getBeanNodes();
		beanNode.addChildren(beanNodes);
	}

	public SynergixScreensStructure getMyTreeStructure() {
		return this.myTreeStructure;
	}
}
