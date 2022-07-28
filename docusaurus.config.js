const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/palenight');

/** @type {import('@docusaurus/types').Config} */
const config = {

    title: 'Android Utils',
    tagline: 'Set of utilities for Android app development',
    url: 'https://jeovanimartinez.github.io/Android-Utils/',
    baseUrl: '/Android-Utils/',
    onBrokenLinks: 'warn',
    onBrokenMarkdownLinks: 'warn',
    favicon: 'img/site/favicon.ico',

    // GitHub pages deployment config.
    organizationName: 'Jeovani Martinez',
    projectName: 'Android Utils',

    staticDirectories: ['static', 'api_reference', 'license'],

    // Internalization
    i18n: {
        defaultLocale: 'en',
        locales: ['en'],
    },

    presets: [
        [
            'classic',
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                    // Remove this to remove the "edit this page" links.
                    editUrl:
                        'https://github.com/JeovaniMartinez/Android-Utils/edit/documentation/',
                },
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },
            }),
        ],
    ],

    themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
        ({
            navbar: {
                title: 'Android Utils',
                logo: {
                    alt: 'Android Utils',
                    src: 'img/site/logo.svg',
                },
                items: [
                    {
                        type: 'doc',
                        docId: 'configuration/getting-started',
                        position: 'left',
                        label: 'Docs',
                    },
                    {
                        href: '/reference/index.html',
                        target: '_blank',
                        label: 'API Reference',
                        position: 'left',
                    },
                    {
                        href: 'https://github.com/JeovaniMartinez/Android-Utils',
                        target: '_blank',
                        label: 'GitHub',
                        position: 'right',
                    },
                ],
            },
            footer: {
                style: 'dark',
                copyright: `Copyright © 2020 - Present by Jeovani Martinez. Built with Docusaurus.`,
            },
            prism: {
                theme: lightCodeTheme,
                darkTheme: darkCodeTheme,
                additionalLanguages: ['kotlin'],
                defaultLanguage: 'kotlin',
            },
            docs: {
                sidebar: {
                    hideable: true,
                },
            },
            algolia: {
                appId: 'AN3LZKXY0J',
                apiKey: 'f072fadb6075c01b373d39472db751de',
                indexName: 'Android-Utils',
            },
        }),
};

module.exports = config;
