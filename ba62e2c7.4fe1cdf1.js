(window.webpackJsonp=window.webpackJsonp||[]).push([[12],{147:function(e,t,r){"use strict";r.r(t),t.default=r.p+"assets/images/more-apps-img1-0c6a21f5f90a410b94a90f2ae98868eb.png"},84:function(e,t,r){"use strict";r.r(t),r.d(t,"frontMatter",(function(){return a})),r.d(t,"metadata",(function(){return l})),r.d(t,"toc",(function(){return p})),r.d(t,"default",(function(){return s}));var n=r(3),i=r(7),o=(r(0),r(95)),a={id:"more-apps",title:"More Apps"},l={unversionedId:"utilities/more-apps",id:"utilities/more-apps",isDocsHomePage:!1,title:"More Apps",description:"[ Reference ]",source:"@site/docs\\utilities\\more-apps.md",slug:"/utilities/more-apps",permalink:"/docs/utilities/more-apps",editUrl:"https://github.com/JeovaniMartinez/Android-Utils/edit/documentation/docs/utilities/more-apps.md",version:"current",sidebar:"someSidebar",previous:{title:"File Utils",permalink:"/docs/utilities/file-utils"},next:{title:"System Web Browser",permalink:"/docs/utilities/system-web-browser"}},p=[{value:"Description",id:"description",children:[]},{value:"Utilities",id:"utilities",children:[{value:"- showAppListInGooglePlay",id:"--showapplistingoogleplay",children:[]}]}],c={toc:p};function s(e){var t=e.components,a=Object(i.a)(e,["components"]);return Object(o.b)("wrapper",Object(n.a)({},c,a,{components:t,mdxType:"MDXLayout"}),Object(o.b)("h4",{id:"reference"},Object(o.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.moreapps/-more-apps/index.html",target:"_blank"},Object(o.b)("b",null,"[ Reference ]"))),Object(o.b)("h2",{id:"description"},"Description"),Object(o.b)("p",null,"Utility to invite the user to install the developer's apps."),Object(o.b)("hr",null),Object(o.b)("h2",{id:"utilities"},"Utilities"),Object(o.b)("h3",{id:"--showapplistingoogleplay"},"- showAppListInGooglePlay"),Object(o.b)("blockquote",null,Object(o.b)("h4",{parentName:"blockquote",id:"reference-1"},Object(o.b)("a",{href:"../reference/androidutils/com.jeovanimartinez.androidutils.moreapps/-more-apps/show-app-list-in-google-play.html",target:"_blank"},Object(o.b)("b",null,"[ Reference ]")))),Object(o.b)("p",null,"Direct the user to the app developer page on Google Play, usually used to invite the user to install the developer apps."),Object(o.b)("p",null,Object(o.b)("img",{alt:"img",src:r(147).default})),Object(o.b)("h4",{id:"usage"},"Usage"),Object(o.b)("p",null,"1.- Set the developer id."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-kotlin"},'MoreApps.apply { developerId = "Jedemm+Technologies" }\n')),Object(o.b)("p",null,"2.- Go to Google Play to show the list of apps from the developer."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-kotlin"},"MoreApps.showAppListInGooglePlay(activity)\n")))}s.isMDXComponent=!0},95:function(e,t,r){"use strict";r.d(t,"a",(function(){return u})),r.d(t,"b",(function(){return m}));var n=r(0),i=r.n(n);function o(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function a(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function l(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?a(Object(r),!0).forEach((function(t){o(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):a(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function p(e,t){if(null==e)return{};var r,n,i=function(e,t){if(null==e)return{};var r,n,i={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(i[r]=e[r]);return i}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(i[r]=e[r])}return i}var c=i.a.createContext({}),s=function(e){var t=i.a.useContext(c),r=t;return e&&(r="function"==typeof e?e(t):l(l({},t),e)),r},u=function(e){var t=s(e.components);return i.a.createElement(c.Provider,{value:t},e.children)},b={inlineCode:"code",wrapper:function(e){var t=e.children;return i.a.createElement(i.a.Fragment,{},t)}},d=i.a.forwardRef((function(e,t){var r=e.components,n=e.mdxType,o=e.originalType,a=e.parentName,c=p(e,["components","mdxType","originalType","parentName"]),u=s(r),d=n,m=u["".concat(a,".").concat(d)]||u[d]||b[d]||o;return r?i.a.createElement(m,l(l({ref:t},c),{},{components:r})):i.a.createElement(m,l({ref:t},c))}));function m(e,t){var r=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var o=r.length,a=new Array(o);a[0]=d;var l={};for(var p in t)hasOwnProperty.call(t,p)&&(l[p]=t[p]);l.originalType=e,l.mdxType="string"==typeof e?e:n,a[1]=l;for(var c=2;c<o;c++)a[c]=r[c];return i.a.createElement.apply(null,a)}return i.a.createElement.apply(null,r)}d.displayName="MDXCreateElement"}}]);