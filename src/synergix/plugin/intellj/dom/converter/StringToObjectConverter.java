package synergix.plugin.intellj.dom.converter;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.Converter;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.Nullable;
import synergix.plugin.intellj.structure.SynergixScreensBuilder;

public class StringToObjectConverter<T> extends Converter<T> {
	private static final Logger LOG = Logger.getInstance(StringToObjectConverter.class);


	@Nullable
	@Override
	public T fromString(@Nullable String s, ConvertContext context) {
		final GenericAttributeValue attribValue = context.getInvocationElement().getParentOfType(GenericAttributeValue.class, false);
		if (attribValue == null) {
			return null;
		}
		String path = attribValue.getStringValue();
		if (path == null) {
			return null;
		}

		String realPath = this.getRealPath(path);
		Project project = context.getProject();
		PsiFile[] psiMenuXml = FilenameIndex.getFilesByName(project, realPath, GlobalSearchScope.projectScope(project));
		if (psiMenuXml.length == 0) {
			LOG.info("Can not find any file with name " + realPath);
			return this.convertToAcceptedType(null);
		}
		return this.convertToAcceptedType(psiMenuXml[0]);
	}

	@Nullable
	@Override
	public String toString(@Nullable T t, ConvertContext context) {
		final GenericAttributeValue attribValue = context.getInvocationElement().getParentOfType(GenericAttributeValue.class, false);
		if (attribValue == null) {
			return null;
		}
		return attribValue.getRawText();
	}

	protected String getRealPath(String rawPath) {
		return rawPath;
	}

	@SuppressWarnings("unchecked")
	protected T convertToAcceptedType(Object data) {
		return (T) data;
	}
}
