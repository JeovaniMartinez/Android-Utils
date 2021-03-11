(window.webpackJsonp=window.webpackJsonp||[]).push([[16],{148:function(e,t,n){"use strict";n.r(t),t.default=n.p+"assets/images/annotations-img1-f83e1de94c16c54a59242d0e31c7d136.png"},149:function(e,t,n){"use strict";n.r(t),t.default=n.p+"assets/images/annotations-img2-7eec56a40202408688f198ec27d8e3ea.png"},88:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return o})),n.d(t,"metadata",(function(){return c})),n.d(t,"toc",(function(){return s})),n.d(t,"default",(function(){return b}));var a=n(3),r=n(7),i=(n(0),n(95)),o={id:"code-inspection-annotations",title:"Code Inspection Annotations"},c={unversionedId:"annotations/code-inspection-annotations",id:"annotations/code-inspection-annotations",isDocsHomePage:!1,title:"Code Inspection Annotations",description:"Description",source:"@site/docs\\annotations\\code-inspection-annotations.md",slug:"/annotations/code-inspection-annotations",permalink:"/docs/annotations/code-inspection-annotations",editUrl:"https://github.com/JeovaniMartinez/Android-Utils/edit/documentation/docs/annotations/code-inspection-annotations.md",version:"current",sidebar:"someSidebar",previous:{title:"Firebase Crashlytics",permalink:"/docs/configuration/firebase-crashlytics"},next:{title:"Rate App",permalink:"/docs/utilities/rate-app"}},s=[{value:"Description",id:"description",children:[]},{value:"Annotations",id:"annotations",children:[{value:"<code>@DrawableOrDrawableRes</code>",id:"drawableordrawableres",children:[]},{value:"<code>@StringOrStringRes</code>",id:"stringorstringres",children:[]}]},{value:"Custom Implementation",id:"custom-implementation",children:[]}],l={toc:s};function b(e){var t=e.components,o=Object(r.a)(e,["components"]);return Object(i.b)("wrapper",Object(a.a)({},l,o,{components:t,mdxType:"MDXLayout"}),Object(i.b)("h2",{id:"description"},"Description"),Object(i.b)("p",null,"These annotations are designed to guide the developer on the type of data or resource that can be assigned to a property or variable, the library has\nthe ",Object(i.b)("a",{parentName:"p",href:"https://github.com/JeovaniMartinez/Android-Utils/tree/master/lintcheck"},"lintcheck")," module that tells the IDE how to verify the use of annotations\nand display warnings in case an incorrect value is detected."),Object(i.b)("p",null,"Generally, in these annotations, the expected data type is Any, to give you the flexibility to accept different data types. For example, in some cases\na String data type is expected, which is processed as is, but an Int data type (which represents the ID) can also be expected and in this case the String\nis obtained from the resources by means of its ID. The library allows this flexibility and is in charge of identifying the type of data and treating it\nappropriately, and if it is an incorrect type of data, an exception is thrown."),Object(i.b)("p",null,"In some cases, the use of values of the wrong data type cannot be detected at compile time, but an exception will be thrown at runtime if it is detected."),Object(i.b)("hr",null),Object(i.b)("h2",{id:"annotations"},"Annotations"),Object(i.b)("h3",{id:"drawableordrawableres"},Object(i.b)("inlineCode",{parentName:"h3"},"@DrawableOrDrawableRes")),Object(i.b)("h4",{id:"reference"},Object(i.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.annotations/-drawable-or-drawable-res/index.html",target:"_blank"},Object(i.b)("b",null,"[ Reference ]"))),Object(i.b)("p",null,"Indicates that the expected value or object should be a Drawable object or the ID of a drawable resource. For example: ",Object(i.b)("inlineCode",{parentName:"p"},"drawableObject"),", ",Object(i.b)("inlineCode",{parentName:"p"},"R.drawable.demo"),"."),Object(i.b)("p",null,Object(i.b)("em",{parentName:"p"},"Detection example in Android Studio:")),Object(i.b)("p",null,Object(i.b)("img",{alt:"img",src:n(148).default})),Object(i.b)("h3",{id:"stringorstringres"},Object(i.b)("inlineCode",{parentName:"h3"},"@StringOrStringRes")),Object(i.b)("h4",{id:"reference-1"},Object(i.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.annotations/-string-or-string-res/index.html",target:"_blank"},Object(i.b)("b",null,"[ Reference ]"))),Object(i.b)("p",null,"Indicates that the expected value or object must be a data type String, Char (since it can be represented as String) or the ID of a string resource.\nFor example: ",Object(i.b)("inlineCode",{parentName:"p"},"'a'"),", ",Object(i.b)("inlineCode",{parentName:"p"},'"Hello"'),", ",Object(i.b)("inlineCode",{parentName:"p"},"R.string.demo")),Object(i.b)("p",null,Object(i.b)("em",{parentName:"p"},"Detection example in Android Studio:")),Object(i.b)("p",null,Object(i.b)("img",{alt:"img",src:n(149).default})),Object(i.b)("hr",null),Object(i.b)("h2",{id:"custom-implementation"},"Custom Implementation"),Object(i.b)("p",null,"Annotations for code inspection are used by library classes, but you can also assign them to any property or parameter that you require."),Object(i.b)("p",null,"In this case, add the required annotation, for example:"),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-kotlin"},"private fun showImage(@DrawableOrDrawableRes image: Any) { ... }\nprivate fun showMessage(@StringOrStringRes message: Any) { ... }\n")),Object(i.b)("p",null,"Then, to process the value, the library has extension functions to get the value easily:"),Object(i.b)("blockquote",null,Object(i.b)("p",{parentName:"blockquote"},Object(i.b)("inlineCode",{parentName:"p"},"typeAsDrawable"),Object(i.b)("br",null),"It is a context extension function, allows you to parse the data type and always treat it as a Drawable.",Object(i.b)("br",null),Object(i.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.extensions.context/type-as-drawable.html",target:"_blank"},Object(i.b)("b",null,"[ Reference ]")))),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-kotlin"},"private fun showImage(@DrawableOrDrawableRes image: Any) {\n    val finalImage = typeAsDrawable(image)\n}\n")),Object(i.b)("blockquote",null,Object(i.b)("p",{parentName:"blockquote"},Object(i.b)("inlineCode",{parentName:"p"},"typeAsString"),Object(i.b)("br",null),"It is a context extension function, allows you to parse the data type and always treat it as a String.",Object(i.b)("br",null),Object(i.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.extensions.context/type-as-string.html",target:"_blank"},Object(i.b)("b",null,"[ Reference ]")))),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-kotlin"},"private fun showMessage(@StringOrStringRes message: Any) {\n    val finalMessage = typeAsString(message)\n}\n")))}b.isMDXComponent=!0},95:function(e,t,n){"use strict";n.d(t,"a",(function(){return p})),n.d(t,"b",(function(){return m}));var a=n(0),r=n.n(a);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function c(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},i=Object.keys(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var l=r.a.createContext({}),b=function(e){var t=r.a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):c(c({},t),e)),n},p=function(e){var t=b(e.components);return r.a.createElement(l.Provider,{value:t},e.children)},d={inlineCode:"code",wrapper:function(e){var t=e.children;return r.a.createElement(r.a.Fragment,{},t)}},u=r.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,i=e.originalType,o=e.parentName,l=s(e,["components","mdxType","originalType","parentName"]),p=b(n),u=a,m=p["".concat(o,".").concat(u)]||p[u]||d[u]||i;return n?r.a.createElement(m,c(c({ref:t},l),{},{components:n})):r.a.createElement(m,c({ref:t},l))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var i=n.length,o=new Array(i);o[0]=u;var c={};for(var s in t)hasOwnProperty.call(t,s)&&(c[s]=t[s]);c.originalType=e,c.mdxType="string"==typeof e?e:a,o[1]=c;for(var l=2;l<i;l++)o[l]=n[l];return r.a.createElement.apply(null,o)}return r.a.createElement.apply(null,n)}u.displayName="MDXCreateElement"}}]);