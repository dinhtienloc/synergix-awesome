package synergix.plugin.intellj.dom.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.lang.StringUtils;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;
import synergix.plugin.intellj.structure.node.BeanNode;
import synergix.plugin.intellj.structure.node.WebFileNode;
import synergix.plugin.intellj.structure.node.XHTMLNode;
import synergix.plugin.intellj.utils.ElUtils;
import synergix.plugin.intellj.utils.SynUtil;

public class XHTMLFileReader {
	private final static String TAG_SYN_INCLUDE = "syn:include";
	private final static String TAG_UI_PARAM = "ui:param";
	private final static String CURRENT_FORM_BEAN_NAME = "currentFormBean";

	private Project project;
	private XmlFile xhtmlFile;
	private SynergixScreensBuilder builder;
	private Set<BeanNode> beanNodes;
	private List<WebFileNode> webFileNodes;
	private Map<String, String> paramBeanMap;

	public XHTMLFileReader(Project project, XmlFile xhtmlFile, SynergixScreensBuilder builder) {
		this.project = project;
		this.xhtmlFile = xhtmlFile;
		this.builder = builder;
		this.beanNodes = new LinkedHashSet<>();
		this.webFileNodes = new ArrayList<>();
		this.paramBeanMap = new HashMap<>();
	}

	public void buildNode() {
		XmlTag root = this.xhtmlFile.getRootTag();
		if (root != null) {
			root.acceptChildren(new XmlTagVisitor());
		}
	}

	private void parseXmlTag(XmlTag tag) {
		if (TAG_SYN_INCLUDE.equals(tag.getName())) {
			WebFileNode node = this.parseSynIncludeTag(tag);
			if (node != null) {
				this.webFileNodes.add(node);
			}
		} else if (TAG_UI_PARAM.equals(tag.getName())) {
			String[] paramPair = this.parseParamTag(tag);
			if (paramPair != null) {
				this.paramBeanMap.put(paramPair[0], paramPair[1]);
			}
		} else {
			Set<BeanNode> beanNodes = this.parseTagToFindBeanNode(tag);
			this.beanNodes.addAll(beanNodes);
		}
	}

	private WebFileNode parseSynIncludeTag(XmlTag tag) {
		String srcAttr = tag.getAttributeValue("src");

		// must be pre-parsed children ui:param to collect declared params
		ParamTagVisitor visitor = new ParamTagVisitor();
		tag.acceptChildren(visitor);
		Map<String, String> paramMapForChildNode = visitor.getCollectedParamMap();

		if (srcAttr != null) {
			VirtualFile includedVirFile = null;
			if (srcAttr.startsWith("/")) {
				VirtualFile webAppFile = VfsUtilCore.findContainingDirectory(this.xhtmlFile.getVirtualFile(), "webapp");
				includedVirFile = VfsUtilCore.findRelativeFile(srcAttr, webAppFile);
			} else if (this.xhtmlFile.getParent() != null) {
				includedVirFile = VfsUtilCore.findRelativeFile(srcAttr, this.xhtmlFile.getParent().getVirtualFile());
			}

			if (includedVirFile == null) return null;
			PsiFile foundPsiFile = PsiManager.getInstance(this.project).findFile(includedVirFile);
			if (foundPsiFile == null) return null;

			WebFileNode webFileNode;

			if (!(foundPsiFile instanceof XmlFile)) {
				webFileNode = new WebFileNode(null, this.project, this.builder, foundPsiFile);
				webFileNode.setMyName(foundPsiFile.getName());
			} else {
				XmlFile childXhtmlFile = (XmlFile) foundPsiFile;
				webFileNode = new XHTMLNode(null, this.project, this.builder, childXhtmlFile);
				webFileNode.setMyName(childXhtmlFile.getName());

				XHTMLFileReader reader = new XHTMLFileReader(this.project, childXhtmlFile, this.builder);

				// param map and bean list are shared in XHTMLFileReaders which have the same form code.
				reader.addAllParamMap(this.getParamBeanMap());
				reader.addAllParamMap(paramMapForChildNode);
				reader.addBeanNode(this.getBeanNodes());

				// build node for child xhtml file
				reader.buildNode();

				List<WebFileNode> childWebFileNodes = reader.getWebFileNodes();
				webFileNode.addChildren(childWebFileNodes);
				this.beanNodes.addAll(reader.getBeanNodes());
			}
			return webFileNode;
		}
		return null;
	}

	private String[] parseParamTag(XmlTag tag) {
		String name = tag.getAttributeValue("name");
		String value = tag.getAttributeValue("value");
		if (name != null && value != null && ElUtils.isElBlock(value)) {
			String expr = ElUtils.removeELBraces(value);

			if (name.equals(expr)) {
				String upperParamName = this.paramBeanMap.get(expr);

				// this case is only existed when ui:param refer the param name to non-existed value
				if (upperParamName == null) {
					return null;
				}

				while (upperParamName.equals(name)) {
					upperParamName = this.paramBeanMap.get(upperParamName);
				}
				return new String[]{name, upperParamName};
			}
			else {

				// if param name is currentFormBean, find java bean directly
				if (CURRENT_FORM_BEAN_NAME.equals(name)) {
					PsiFile psiFileBean = this.findJavaClassBean(expr);
					if (psiFileBean != null) {
						BeanNode beanNode = this.createBeanNodeFromPsiFile(psiFileBean);
						if (beanNode != null) {
							this.beanNodes.add(beanNode);
						}
					}
				}

				return new String[]{name, expr};
			}
		}
		return null;
	}

	private Set<BeanNode> parseTagToFindBeanNode(XmlTag tag) {
		Set<BeanNode> beanNodes = new LinkedHashSet<>();

		List<String> attrStrs = new ArrayList<>();
		attrStrs.add(tag.getAttributeValue("value"));
		attrStrs.add(tag.getAttributeValue("action"));
		attrStrs.add(tag.getAttributeValue("ajaxAction"));

		for (String attrStr : attrStrs) {
			if (attrStr != null && ElUtils.isElBlock(attrStr)) {
				BeanNode beanNode = this.buildBeanNodeFromElExpression(attrStr);
				if (beanNode != null) {
					beanNodes.add(beanNode);
				}
			}
		}

		return beanNodes;
	}

	private BeanNode buildBeanNodeFromElExpression(String elExpr) {
		String expr = ElUtils.removeELBraces(elExpr);

		String realBeanName = this.findRealBeanFromParamMap(expr);
		if (expr.indexOf(".") > 0) {
			realBeanName = expr.substring(0, expr.indexOf("."));
		}
		realBeanName = this.findRealBeanFromParamMap(realBeanName);

		PsiFile psiFileBean = this.findJavaClassBean(realBeanName);
		if (psiFileBean != null) {
			return this.createBeanNodeFromPsiFile(psiFileBean);
		}

		return null;
	}

	private BeanNode createBeanNodeFromPsiFile(PsiFile psiFileBean) {
		PsiClass psiBeanClass = ((PsiJavaFile)psiFileBean).getClasses()[0];

		for (BeanNode createdNode : this.beanNodes) {
			// BeanNode with this name is existed, ignored
			if (createdNode.getMyName().equals(psiBeanClass.getName())) {
				return null;
			}
		}

		BeanNode beanNode = new BeanNode(null, this.project, this.builder, psiFileBean);
		beanNode.setMyName(psiBeanClass.getName());
		return beanNode;
	}

	private String findRealBeanFromParamMap(String param) {
		String beanName = param;
		String refParamName = this.paramBeanMap.get(beanName);

		while (refParamName != null) {
			beanName = refParamName;
			refParamName = this.paramBeanMap.get(beanName);
		}

		return beanName;
	}

	private PsiFile findJavaClassBean(String beanName) {
//		ClassInheritorsSearch.search(new SearchParameters());
		String className = StringUtils.capitalize(beanName);
		PsiFile[] foundFiles = SynUtil.getFilesByName(this.project, className + ".java", "TH6");

		if (foundFiles.length == 0) {
			return null;
		}

		return foundFiles[0];
	}

	public Set<BeanNode> getBeanNodes() {
		return this.beanNodes;
	}

	public List<WebFileNode> getWebFileNodes() {
		return this.webFileNodes;
	}

	private Map<String, String> getParamBeanMap() {
		return this.paramBeanMap;
	}

	private void addAllParamMap(Map<String, String> anotherParamBeanMap) {
		this.paramBeanMap.putAll(anotherParamBeanMap);
	}

	private void addBeanNode(Set<BeanNode> beanNodes) {
		this.beanNodes.addAll(beanNodes);
	}

	private class XmlTagVisitor extends PsiElementVisitor {
		@Override
		public void visitElement(PsiElement element) {
			if (element instanceof XmlTag) {
				XmlTag tag = (XmlTag) element;
				XHTMLFileReader.this.parseXmlTag(tag);

				// syn:include tag must be solved its children itself
				if (!TAG_SYN_INCLUDE.equals(tag.getName())) {
					element.acceptChildren(this);
				}
			}
		}
	}

	private class ParamTagVisitor extends PsiElementVisitor {
		private Map<String, String> collectedParamMap = new HashMap<>();

		@Override
		public void visitElement(PsiElement element) {
			if (element instanceof XmlTag) {
				XmlTag tag = (XmlTag) element;
				if (TAG_UI_PARAM.equals(tag.getName())) {
					String[] paramPair = XHTMLFileReader.this.parseParamTag(tag);
					if (paramPair != null) {
						this.collectedParamMap.put(paramPair[0], paramPair[1]);
					}
				}
			}
		}

		Map<String, String> getCollectedParamMap() {
			return this.collectedParamMap;
		}
	}
}
