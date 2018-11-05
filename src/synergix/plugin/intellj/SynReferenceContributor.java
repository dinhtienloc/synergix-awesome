//package synergix.plugin.intellj;
//
//import com.intellij.patterns.XmlPatterns;
//import com.intellij.psi.*;
//import com.intellij.psi.xml.XmlAttributeValue;
//import com.intellij.util.ProcessingContext;
//import org.jetbrains.annotations.NotNull;
//import synergix.plugin.intellj.reference.XmlAttributeValueSynReference;
//
//public class SynReferenceContributor extends PsiReferenceContributor {
//
//    @Override
//    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
////        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue(), new PsiReferenceProvider() {
////            @NotNull
////            @Override
////            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
////                return new PsiReference[]{new XmlAttributeValueSynReference((XmlAttributeValue) element)};
////            }
////        });
//    }
//}
