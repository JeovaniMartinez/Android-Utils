/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    "mainSidebar": [
        {
            "collapsed": true,
            "type": "category",
            "label": "Android Utils",
            "link": {"type": "doc", "id": 'android-utils/index'},
            "items": [
                {
                    "type": "doc",
                    "id": "android-utils/introduction"
                },
                {
                    "type": "doc",
                    "id": "android-utils/motivation"
                }
            ]
        },
        {
            "collapsed": true,
            "type": "category",
            "label": "Configuration",
            "link": {"type": "doc", "id": 'configuration/index'},
            "items": [
                {
                    "type": "doc",
                    "id": "configuration/getting-started"
                },
                {
                    "type": "doc",
                    "id": "configuration/log"
                },
                {
                    "type": "doc",
                    "id": "configuration/firebase-analytics"
                },
                {
                    "type": "doc",
                    "id": "configuration/firebase-crashlytics"
                }
            ]
        },
        {
            "collapsed": true,
            "type": "category",
            "label": "Annotations",
            "link": {"type": "doc", "id": 'annotations/index'},
            "items": [
                {
                    "type": "doc",
                    "id": "annotations/code-inspection-annotations"
                }
            ]
        },
        {
            "collapsed": false,
            "type": "category",
            "label": "Library Utilities",
            "link": {"type": "doc", "id": 'library-utilities/index'},
            "items": [
                {
                    "type": "doc",
                    "id": "library-utilities/rate-app"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/watermark"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/view-to-image"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/about-app"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/translucent-theme"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/premium-app"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/file-utils"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/more-apps"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/system-web-browser"
                },
                {
                    "type": "doc",
                    "id": "library-utilities/extension-functions"
                }
            ]
        },
        {
            "collapsed": true,
            "type": "category",
            "label": "Release Notes",
            "link": {"type": "doc", "id": 'release-notes/index'},
            "items": [
                {
                    "type": "doc",
                    "id": "release-notes/v0.9.0-beta"
                }
            ]
        }
    ]
};

module.exports = sidebars;
