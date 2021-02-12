package com.jeovanimartinez.androidutils.lintcheck.annotations

import com.android.resources.ResourceType
import com.android.tools.lint.checks.AnnotationDetector
import com.android.tools.lint.detector.api.*
import com.intellij.psi.*
import org.jetbrains.kotlin.KtNodeTypes.DOT_QUALIFIED_EXPRESSION
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UReferenceExpression
import org.w3c.dom.Attr

/**
 * A custom lint check that prohibits usage of the `android.widget.Toast` class and suggests
 * using a Snackbar from the support library instead.
 */
class TypeOrResource : AnnotationDetector(){

    companion object {
        val ISSUE = Issue.create(
            id = "AndroidToastJavaKotlin",
            briefDescription = "Prohibits usages of `android.widget.Toast`",
            explanation = "UPDATEDDDDD Usages of `android.widget.Toast` are prohibited",
            category = Category.CORRECTNESS,
            severity = Severity.ERROR,
            implementation = Implementation(
                TypeOrResource::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun inheritAnnotation(annotation: String): Boolean {
        return super.inheritAnnotation(annotation)
    }

    override fun isApplicableAnnotationUsage(type: AnnotationUsageType): Boolean {
        return super.isApplicableAnnotationUsage(type)
    }

    override fun visitAnnotationUsage(context: JavaContext, usage: UElement, type: AnnotationUsageType, annotation: UAnnotation, qualifiedName: String, method: PsiMethod?, referenced: PsiElement?, annotations: List<UAnnotation>, allMemberAnnotations: List<UAnnotation>, allClassAnnotations: List<UAnnotation>, allPackageAnnotations: List<UAnnotation>) {
        super.visitAnnotationUsage(context, usage, type, annotation, qualifiedName, method, referenced, annotations, allMemberAnnotations, allClassAnnotations, allPackageAnnotations)
    }

    override fun visitAnnotationUsage(context: JavaContext, usage: UElement, type: AnnotationUsageType, annotation: UAnnotation, qualifiedName: String,
                                      method: PsiMethod?, annotations: List<UAnnotation>, allMemberAnnotations: List<UAnnotation>, allClassAnnotations: List<UAnnotation>, allPackageAnnotations: List<UAnnotation>) {




        val c = usage.sourcePsi?.node?.firstChildNode?.elementType
        val d = usage.sourcePsi?.node?.firstChildNode?.text

        if(c != null && d != null) {
            if(c == DOT_QUALIFIED_EXPRESSION &&  d != "R.string" && d != "android.R.string" ) {
                context.report(
                    issue = ISSUE,
                    scope = usage,
                    location = context.getLocation(usage),
                    message = "Please use String or String resource ID JM TEST"
                )
            }
        }



    }

    override fun visitReference(
        context: JavaContext,
        visitor: JavaElementVisitor?,
        reference: PsiJavaCodeReferenceElement,
        referenced: PsiElement
    ) {
        super.visitReference(context, visitor, reference, referenced)
    }

    override fun visitReference(
        context: JavaContext,
        reference: UReferenceExpression,
        referenced: PsiElement
    ) {
        super.visitReference(context, reference, referenced)
    }

    override fun visitResourceReference(
        context: JavaContext,
        visitor: JavaElementVisitor?,
        node: PsiElement,
        type: ResourceType,
        name: String,
        isFramework: Boolean
    ) {
        super.visitResourceReference(context, visitor, node, type, name, isFramework)
    }

    override fun visitResourceReference(
        context: JavaContext,
        node: UElement,
        type: ResourceType,
        name: String,
        isFramework: Boolean
    ) {
        super.visitResourceReference(context, node, type, name, isFramework)
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        super.visitAttribute(context, attribute)
    }



    override fun applicableAnnotations(): List<String>? {
        return listOf("com.jeovanimartinez.androidutils.annotations.StringOrStringRes")
    }



    override fun createPsiVisitor(context: JavaContext): JavaElementVisitor? {
        return super.createPsiVisitor(context)
    }



}
