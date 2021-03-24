(window.webpackJsonp=window.webpackJsonp||[]).push([[19],{102:function(e,t,i){"use strict";i.d(t,"a",(function(){return u})),i.d(t,"b",(function(){return m}));var n=i(0),r=i.n(n);function a(e,t,i){return t in e?Object.defineProperty(e,t,{value:i,enumerable:!0,configurable:!0,writable:!0}):e[t]=i,e}function l(e,t){var i=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),i.push.apply(i,n)}return i}function c(e){for(var t=1;t<arguments.length;t++){var i=null!=arguments[t]?arguments[t]:{};t%2?l(Object(i),!0).forEach((function(t){a(e,t,i[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(i)):l(Object(i)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(i,t))}))}return e}function o(e,t){if(null==e)return{};var i,n,r=function(e,t){if(null==e)return{};var i,n,r={},a=Object.keys(e);for(n=0;n<a.length;n++)i=a[n],t.indexOf(i)>=0||(r[i]=e[i]);return r}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(n=0;n<a.length;n++)i=a[n],t.indexOf(i)>=0||Object.prototype.propertyIsEnumerable.call(e,i)&&(r[i]=e[i])}return r}var d=r.a.createContext({}),b=function(e){var t=r.a.useContext(d),i=t;return e&&(i="function"==typeof e?e(t):c(c({},t),e)),i},u=function(e){var t=b(e.components);return r.a.createElement(d.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return r.a.createElement(r.a.Fragment,{},t)}},s=r.a.forwardRef((function(e,t){var i=e.components,n=e.mdxType,a=e.originalType,l=e.parentName,d=o(e,["components","mdxType","originalType","parentName"]),u=b(i),s=n,m=u["".concat(l,".").concat(s)]||u[s]||p[s]||a;return i?r.a.createElement(m,c(c({ref:t},d),{},{components:i})):r.a.createElement(m,c({ref:t},d))}));function m(e,t){var i=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var a=i.length,l=new Array(a);l[0]=s;var c={};for(var o in t)hasOwnProperty.call(t,o)&&(c[o]=t[o]);c.originalType=e,c.mdxType="string"==typeof e?e:n,l[1]=c;for(var d=2;d<a;d++)l[d]=i[d];return r.a.createElement.apply(null,l)}return r.a.createElement.apply(null,i)}s.displayName="MDXCreateElement"},110:function(e,t,i){"use strict";i.r(t),t.default=i.p+"assets/images/pending-image-196cb63a01e79c54d2f5948e4257f290.png"},94:function(e,t,i){"use strict";i.r(t),i.d(t,"frontMatter",(function(){return l})),i.d(t,"metadata",(function(){return c})),i.d(t,"toc",(function(){return o})),i.d(t,"default",(function(){return b}));var n=i(3),r=i(7),a=(i(0),i(102)),l={id:"view-to-image",title:"View To Image"},c={unversionedId:"utilities/view-to-image",id:"utilities/view-to-image",isDocsHomePage:!1,title:"View To Image",description:"[ Reference ]",source:"@site/docs\\utilities\\view-to-image.md",slug:"/utilities/view-to-image",permalink:"/Android-Utils/docs/utilities/view-to-image",editUrl:"https://github.com/JeovaniMartinez/Android-Utils/edit/documentation/docs/utilities/view-to-image.md",version:"current",sidebar:"someSidebar",previous:{title:"Watermark",permalink:"/Android-Utils/docs/utilities/watermark"},next:{title:"About App",permalink:"/Android-Utils/docs/utilities/about-app"}},o=[{value:"Description",id:"description",children:[]},{value:"Usage",id:"usage",children:[]},{value:"Considerations",id:"considerations",children:[]},{value:"&gt; Padding and Margin",id:"-padding-and-margin",children:[]},{value:"&gt; Exclude Children Views",id:"-exclude-children-views",children:[{value:"- Hide",id:"--hide",children:[]},{value:"- Crop Vertically",id:"--crop-vertically",children:[]},{value:"- Crop Horizontally",id:"--crop-horizontally",children:[]},{value:"- Crop All",id:"--crop-all",children:[]}]}],d={toc:o};function b(e){var t=e.components,l=Object(r.a)(e,["components"]);return Object(a.b)("wrapper",Object(n.a)({},d,l,{components:t,mdxType:"MDXLayout"}),Object(a.b)("h4",{id:"reference"},Object(a.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.web/-system-web-browser/index.html",target:"_blank"},Object(a.b)("b",null,"[ Reference ]"))),Object(a.b)("h2",{id:"description"},"Description"),Object(a.b)("p",null,"Utility for convert views to images. Works for any view and view groups, including layouts with all their children views."),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})),Object(a.b)("hr",null),Object(a.b)("h2",{id:"usage"},"Usage"),Object(a.b)("p",null,"Call the following function of the utility passing the configuration to generate the image of the view."),Object(a.b)("blockquote",null,Object(a.b)("h4",{parentName:"blockquote",id:"configuration-parameters"},Object(a.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.about/-about-app-config/index.html",target:"_blank"},Object(a.b)("b",null,"[ Configuration Parameters  ]")))),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-kotlin"},"    // CODE\n")),Object(a.b)("hr",null),Object(a.b)("h2",{id:"considerations"},"Considerations"),Object(a.b)("h2",{id:"-padding-and-margin"},"> Padding and Margin"),Object(a.b)("p",null,"When converting the view to an image, a margin and padding can be specified, the margin is completely independent, and the padding is applied within\nthe specified background, for example:"),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})),Object(a.b)("hr",null),Object(a.b)("h2",{id:"-exclude-children-views"},"> Exclude Children Views"),Object(a.b)("p",null,"If the view to be converted to an image is a view group as a layout, by default the generated image includes all children views, however, it is\npossible to pass a configuration to exclude certain children views in different ways."),Object(a.b)("blockquote",null,Object(a.b)("p",{parentName:"blockquote"},"ExcludeMode enum defines the ways in which the child view can be excluded from the image."),Object(a.b)("h4",{parentName:"blockquote",id:"reference-1"},Object(a.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.watermark.config/-watermark-position/index.html",target:"_blank"},Object(a.b)("b",null,"[ Reference ]"))),Object(a.b)("hr",{parentName:"blockquote"}),Object(a.b)("p",{parentName:"blockquote"},"ExcludeView define the configuration to exclude a child view from the image."),Object(a.b)("h4",{parentName:"blockquote",id:"reference-2"},Object(a.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.watermark.config/-watermark-position/index.html",target:"_blank"},Object(a.b)("b",null,"[ Reference ]")))),Object(a.b)("p",null,"To exemplify the ways in which a child view can be excluded, we will consider the following layout:"),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})),Object(a.b)("h3",{id:"--hide"},"- Hide"),Object(a.b)("blockquote",null,Object(a.b)("p",{parentName:"blockquote"},"ExcludeMode.HIDE")),Object(a.b)("p",null,"In this mode, the space occupied by the child view is replaced by the background color."),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-kotlin"},"    // CODE\n")),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})),Object(a.b)("h3",{id:"--crop-vertically"},"- Crop Vertically"),Object(a.b)("blockquote",null,Object(a.b)("p",{parentName:"blockquote"},"ExcludeMode.CROP_VERTICALLY")),Object(a.b)("p",null,"In this mode, the image of the view is cropped vertically, deleting all the space occupied by the child view."),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})),Object(a.b)("h3",{id:"--crop-horizontally"},"- Crop Horizontally"),Object(a.b)("blockquote",null,Object(a.b)("p",{parentName:"blockquote"},"ExcludeMode.CROP_HORIZONTALLY")),Object(a.b)("p",null,"In this mode, the image of the view is cropped horizontally, deleting all the space occupied by the child view."),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})),Object(a.b)("h3",{id:"--crop-all"},"- Crop All"),Object(a.b)("blockquote",null,Object(a.b)("p",{parentName:"blockquote"},"ExcludeMode.CROP_ALL")),Object(a.b)("p",null,"In this mode, the image of the view is cropped vertically and horizontally, deleting all the space occupied by the child view."),Object(a.b)("p",null,Object(a.b)("img",{alt:"img",src:i(110).default})))}b.isMDXComponent=!0}}]);