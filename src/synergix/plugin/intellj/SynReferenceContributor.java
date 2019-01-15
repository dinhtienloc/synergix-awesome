package synergix.plugin.intellj;

import com.intellij.patterns.StandardPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import synergix.plugin.intellj.provider.PathLookupReferenceProvider;
import synergix.plugin.intellj.reference.PsiLiteralGenericFileNameReference;
import synergix.plugin.intellj.reference.XmlAttributeGenericFileNameReference;

public class SynReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(StandardPatterns.instanceOf(PsiLiteral.class), new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                return new PsiReference[]{new PsiLiteralGenericFileNameReference((PsiLiteral) element)};
            }
        });

        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().inside(
                        XmlPatterns.xmlTag().withName(XmlPatterns.string().andNot(XmlPatterns.string().endsWith("include"))))
                        .withValue(StandardPatterns.string().matches("^(?!\\#\\{).+")),
                new PsiReferenceProvider() {
                    @NotNull
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        return new PsiReference[]{
                                new XmlAttributeGenericFileNameReference((XmlAttributeValue) element)
                        };
                    }
                }, -100.0D);

        final PsiReferenceProvider pathLookupReferenceProvider = new PathLookupReferenceProvider();
        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().inside(
                        XmlPatterns.xmlTag().withName(XmlPatterns.string().endsWith("include")))
                        .withValue(StandardPatterns.string().matches("^(?!\\#\\{).+")),
                pathLookupReferenceProvider);

//        registrar.registerReferenceProvider(
//                StandardPatterns.instanceOf(PsiLiteral.class),
//                pathLookupReferenceProvider);

//        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue(), new PsiReferenceProvider() {
//            @NotNull
//            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
//                return new PsiReference[]{new OneWayPsiFileFromXmlTagReference((XmlTag)element)};
//            }
//        }, -100.0D);
    }
}
