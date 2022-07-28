"use strict";(self.webpackChunkandroid_utils_docs=self.webpackChunkandroid_utils_docs||[]).push([[417],{3905:(e,t,n)=>{n.d(t,{Zo:()=>c,kt:()=>m});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},i=Object.keys(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var l=a.createContext({}),d=function(e){var t=a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},c=function(e){var t=d(e.components);return a.createElement(l.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},u=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,i=e.originalType,l=e.parentName,c=s(e,["components","mdxType","originalType","parentName"]),u=d(n),m=r,b=u["".concat(l,".").concat(m)]||u[m]||p[m]||i;return n?a.createElement(b,o(o({ref:t},c),{},{components:n})):a.createElement(b,o({ref:t},c))}));function m(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var i=n.length,o=new Array(i);o[0]=u;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s.mdxType="string"==typeof e?e:r,o[1]=s;for(var d=2;d<i;d++)o[d]=n[d];return a.createElement.apply(null,o)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},5786:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>l,contentTitle:()=>o,default:()=>p,frontMatter:()=>i,metadata:()=>s,toc:()=>d});var a=n(7462),r=(n(7294),n(3905));const i={id:"code-inspection-annotations",title:"Code Inspection Annotations",description:"Annotations to assist the development process."},o=void 0,s={unversionedId:"annotations/code-inspection-annotations",id:"annotations/code-inspection-annotations",title:"Code Inspection Annotations",description:"Annotations to assist the development process.",source:"@site/docs/annotations/code-inspection-annotations.md",sourceDirName:"annotations",slug:"/annotations/code-inspection-annotations",permalink:"/Android-Utils/docs/annotations/code-inspection-annotations",draft:!1,editUrl:"https://github.com/JeovaniMartinez/Android-Utils/edit/documentation/docs/annotations/code-inspection-annotations.md",tags:[],version:"current",frontMatter:{id:"code-inspection-annotations",title:"Code Inspection Annotations",description:"Annotations to assist the development process."},sidebar:"mainSidebar",previous:{title:"Annotations",permalink:"/Android-Utils/docs/annotations/"},next:{title:"Library Utilities",permalink:"/Android-Utils/docs/library-utilities/"}},l={},d=[{value:"Description",id:"description",level:2},{value:"Annotations",id:"annotations",level:2},{value:"<code>@DrawableOrDrawableRes</code>",id:"drawableordrawableres",level:3},{value:'<a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.annotations/-drawable-or-drawable-res/index.html" target="_blank"><b> Reference </b></a>',id:"-reference-",level:4},{value:"<code>@StringOrStringRes</code>",id:"stringorstringres",level:3},{value:'<a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.annotations/-string-or-string-res/index.html" target="_blank"><b> Reference </b></a>',id:"-reference--1",level:4},{value:"Custom Implementation",id:"custom-implementation",level:2}],c={toc:d};function p(e){let{components:t,...i}=e;return(0,r.kt)("wrapper",(0,a.Z)({},c,i,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"description"},"Description"),(0,r.kt)("p",null,"These annotations are designed to guide the developer on the type of data or resource that can be assigned to a property or variable, the library has\nthe ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/JeovaniMartinez/Android-Utils/tree/master/lintcheck"},"Lint Check")," module that tells the IDE how to verify the use of annotations\nand display warnings in case an incorrect value is detected."),(0,r.kt)("p",null,"Generally, when these annotations are used, the expected data type is ",(0,r.kt)("inlineCode",{parentName:"p"},"Any")," to give you the flexibility to accept different data types. For example, in some cases,\na ",(0,r.kt)("inlineCode",{parentName:"p"},"String")," data type is expected, which is processed as is, but an ",(0,r.kt)("inlineCode",{parentName:"p"},"Int")," data type (which represents the resource ID) can also be expected and in this case\nthe String is obtained from the resources by means of its ID. The library allows this flexibility and is in charge of identifying and treating the data\ntype appropriately, and in case the data type cannot be properly treated, an exception is thrown."),(0,r.kt)("p",null,"In some cases, the use of values of the wrong data type cannot be detected at compile time, but an exception will be thrown at runtime if it is detected."),(0,r.kt)("hr",null),(0,r.kt)("h2",{id:"annotations"},"Annotations"),(0,r.kt)("h3",{id:"drawableordrawableres"},(0,r.kt)("inlineCode",{parentName:"h3"},"@DrawableOrDrawableRes")),(0,r.kt)("h4",{id:"-reference-"},(0,r.kt)("a",{href:"/reference/-android%20-utils/com.jeovanimartinez.androidutils.annotations/-drawable-or-drawable-res/index.html",target:"_blank"},(0,r.kt)("b",null,"[ Reference ]"))),(0,r.kt)("p",null,"Indicates that the expected value or object should be a Drawable object or the ID of a drawable resource. For example: ",(0,r.kt)("inlineCode",{parentName:"p"},"drawableObject"),", ",(0,r.kt)("inlineCode",{parentName:"p"},"R.drawable.demo"),"."),(0,r.kt)("p",null,(0,r.kt)("em",{parentName:"p"},"Detection example in Android Studio:")),(0,r.kt)("p",{align:"center"},(0,r.kt)("img",{src:n(7323).Z,alt:""})),(0,r.kt)("h3",{id:"stringorstringres"},(0,r.kt)("inlineCode",{parentName:"h3"},"@StringOrStringRes")),(0,r.kt)("h4",{id:"-reference--1"},(0,r.kt)("a",{href:"/reference/-android%20-utils/com.jeovanimartinez.androidutils.annotations/-string-or-string-res/index.html",target:"_blank"},(0,r.kt)("b",null,"[ Reference ]"))),(0,r.kt)("p",null,"Indicates that the expected value or object must be a data type String, Char (since it can be represented as String), or the ID of a string resource.\nFor example: ",(0,r.kt)("inlineCode",{parentName:"p"},"'a'"),", ",(0,r.kt)("inlineCode",{parentName:"p"},'"Hello"'),", ",(0,r.kt)("inlineCode",{parentName:"p"},"R.string.demo")),(0,r.kt)("p",null,(0,r.kt)("em",{parentName:"p"},"Detection example in Android Studio:")),(0,r.kt)("p",{align:"center"},(0,r.kt)("img",{src:n(7331).Z,alt:""})),(0,r.kt)("hr",null),(0,r.kt)("h2",{id:"custom-implementation"},"Custom Implementation"),(0,r.kt)("p",null,"Annotations for code inspection are used by library classes, but you can also assign them to any property or parameter that you require."),(0,r.kt)("p",null,"In this case, add the required annotation, for example:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"private fun showImage(@DrawableOrDrawableRes image: Any) { ... }\nprivate fun showMessage(@StringOrStringRes message: Any) { ... }\n")),(0,r.kt)("p",null,"Then, to process the value, the library has extension functions to get the value easily:"),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},(0,r.kt)("inlineCode",{parentName:"p"},"typeAsDrawable"),(0,r.kt)("br",null),"It is a context extension function, that allows you to parse the data type and always treat it as a ",(0,r.kt)("inlineCode",{parentName:"p"},"Drawable"),".",(0,r.kt)("br",null),(0,r.kt)("a",{href:"/reference/-android%20-utils/com.jeovanimartinez.androidutils.extensions.context/type-as-drawable.html",target:"_blank"},(0,r.kt)("b",null,"[ Reference ]")))),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"private fun showImage(@DrawableOrDrawableRes image: Any) {\n    val finalImage = typeAsDrawable(image)\n}\n")),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},(0,r.kt)("inlineCode",{parentName:"p"},"typeAsString"),(0,r.kt)("br",null),"It is a context extension function, that allows you to parse the data type and always treat it as a ",(0,r.kt)("inlineCode",{parentName:"p"},"String"),".",(0,r.kt)("br",null),(0,r.kt)("a",{href:"/reference/-android%20-utils/com.jeovanimartinez.androidutils.extensions.context/type-as-string.html",target:"_blank"},(0,r.kt)("b",null,"[ Reference ]")))),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"private fun showMessage(@StringOrStringRes message: Any) {\n    val finalMessage = typeAsString(message)\n}\n")))}p.isMDXComponent=!0},7323:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/annotations-img1-70abd3ae638716fef20396c3c80b16d3.png"},7331:(e,t,n)=>{n.d(t,{Z:()=>a});const a=n.p+"assets/images/annotations-img2-f4b32a2df734a02360bfa0154e93fbd8.png"}}]);