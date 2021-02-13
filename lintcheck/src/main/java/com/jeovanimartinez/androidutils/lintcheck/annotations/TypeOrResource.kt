@file:Suppress("UnstableApiUsage")

package com.jeovanimartinez.androidutils.lintcheck.annotations

import com.android.resources.ResourceType
import com.android.tools.lint.checks.AnnotationDetector
import com.android.tools.lint.detector.api.*
import com.intellij.psi.*
import org.jetbrains.kotlin.KtNodeTypes.DOT_QUALIFIED_EXPRESSION
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.uast.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.w3c.dom.Attr

/**
 * A custom lint check that prohibits usage of the `android.widget.Toast` class and suggests
 * using a Snackbar from the support library instead.
 */
class TypeOrResource : AnnotationDetector(){

    companion object {
        // Configuración y descripción del problema
        val ISSUE = Issue.create(
            id = "TypeOrResource",
            briefDescription = "Validate that the correct type or correct resource is received.`",
            explanation = """
                When using this annotation, generally the accepted data type is Any, so this verification is in charge of validating 
                that the correct data type is received according to the annotation. For example, the @StringOrStringRes annotation is
                expected to receive a string value, such as "Hello" or an ID of a string resource, such as R.string.demo. If a String 
                is received, the data is treated as is, if a string ID is received, the value is obtained and it is treated as a String, 
                but if an incorrect data type is received like a Float or an incorrect resource like R .color.demo. app will throw an 
                exception at runtime. For this reason, this check helps to assign a correct value according to the annotation.
            """.trimIndent(),
            category = Category.CORRECTNESS,
            severity = Severity.ERROR,
            implementation = Implementation(TypeOrResource::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    /*

    Mensajes

    Invalid type, expected string object or string resource
    Invalid resource, expected string resource or string object

    Invalid type, expected color int or color resource
    Invalid resource, expected color resource or color int

    Invalid type, expected drawable object or drawable resource
    Invalid resource, expected drawable resource or drawable object

    * */

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


        // Se obtiene el tipo de elemento con su nombre de debug, por ejemplo REGULAR_STRING_PART para un String
        val elementType = usage.sourcePsi?.node?.firstChildNode?.elementType
        // Se obtiene el valor en texto, básicamente es el valor con el que se invoca.toString()
        val text = usage.sourcePsi?.node?.firstChildNode?.text

        val elementPrev = usage.sourcePsi?.node?.firstChildNode?.treePrev // Siguiente elemento
        val elementNext = usage.sourcePsi?.node?.firstChildNode?.treeNext // Elemento anterior

        // Se valida que haya contenido para verificar
        if (elementType != null && text != null) {
            /*
            * Solo se valida si elementPrev && elementNext son null, esto indica que no hay nada antes del valor ni después, por ejemplo
            * si el valor donde se invoca es:
            *   5 si entra, ya que no hay nada antes ni después
            *   5f si entra, ya que f se considera parte del valor
            *   5.toString() no entra, ya que después del valor hay un punto
            * */
            if (elementPrev == null && elementNext == null) {

                // VALIDACIÓN PARA STRING

                // Si el tipo de elemento no es un string, se reporta el error
                if (elementType != KtTokens.CHARACTER_LITERAL && elementType != KtTokens.REGULAR_STRING_PART && elementType != KtTokens.OPEN_QUOTE) {
                    return context.report(
                        issue = ISSUE,
                        scope = usage,
                        location = context.getLocation(usage),
                        message = "Invalid type, expected string object or string resource"
                    )
                }

            }
        }

        // Si no entro en el reporte anterior, ahora se verifica si el valor corresponde a un valor de R.

        // VALIDACIÓN PARA STRING

        if (elementType != null && text != null) {
            if (text.contains("R.")) {
                if (elementType == DOT_QUALIFIED_EXPRESSION && text != "R.string" && text != "android.R.string") {
                    return context.report(
                        issue = ISSUE,
                        scope = usage,
                        location = context.getLocation(usage),
                        message = "Invalid resource, expected string resource or string object"
                    )
                }
            }
        }

//        val c = usage.sourcePsi?.node?.firstChildNode?.elementType
//        val d = usage.sourcePsi?.node?.firstChildNode?.text
//
//
//        val nextS = usage.sourcePsi?.node?.firstChildNode?.treeNext
//        val nextP = usage.sourcePsi?.node?.firstChildNode?.treePrev
//
//        if(nextS == null && nextP == null){
//            context.report(
//                issue = ISSUE,
//                scope = usage,
//                location = context.getLocation(usage),
//                message = "Can evaluate IT"
//            )
//        }
//
//        val isIstring = (c == KtTokens.CHARACTER_LITERAL || c == KtTokens.REGULAR_STRING_PART || c == KtTokens.OPEN_QUOTE)
//
//
//        val applyVerification = c == KtTokens.CHARACTER_LITERAL ||
//        c == KtTokens.REGULAR_STRING_PART ||
//        c == KtTokens.OPEN_QUOTE ||
//        c == KtTokens.INTEGER_LITERAL ||
//        c == KtTokens.OPEN_QUOTE ||
//        c == KtTokens.OPEN_QUOTE ||
//        c == KtTokens.OPEN_QUOTE
//
//        val canInt = try {
//            d?.toInt()
//            true
//        } catch (e: Exception) {
//            false
//        }
//
//        val ee = context.getLocation(usage)


        // QUE SE OBTIENE
        // R.string.demo = DOT_QUALIFIED_EXPRESSION R.string
        // android.R.string.ok = DOT_QUALIFIED_EXPRESSION android.R.string
        // R.color.demo = DOT_QUALIFIED_EXPRESSION R.color
        // R.drawable.demo = DOT_QUALIFIED_EXPRESSION R.drawable
        // R.dimen..demo = DOT_QUALIFIED_EXPRESSION R.dimen
        // R.raw.demo = DOT_QUALIFIED_EXPRESSION R.raw
        // "" = OPEN_QUOTE
        // "one" = REGULAR_STRING_PART
        // 'c' = CHARACTER_LITERAL
        // 25.toString() = INTEGER_CONSTANT
        // 25 = INTEGER_LITERAL
        // 10f = FLOAT_CONSTANT


        /*if(c != null && d != null) {
            if(c == DOT_QUALIFIED_EXPRESSION &&  d != "R.string" && d != "android.R.string" ) {
                context.report(
                    issue = ISSUE,
                    scope = usage,
                    location = context.getLocation(usage),
                    message = "Please use String or String resource ID JM TEST"
                )
            }
        }*/





    }




    override fun checkCall(context: ClassContext, classNode: ClassNode, method: MethodNode, call: MethodInsnNode) {
        super.checkCall(context, classNode, method, call)
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
