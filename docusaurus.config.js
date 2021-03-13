module.exports = {
    title: 'Android Utils',
    tagline: 'Set of utilities for Android app development',
    url: 'https://jeovanimartinez.github.io/Android-Utils/',
    baseUrl: '/', // Use '/' for develop and '/Android-Utils/' for production
    onBrokenLinks: 'warn',
    onBrokenMarkdownLinks: 'warn',
    favicon: 'img/site/favicon.ico',
    organizationName: 'Jeovani Martínez',
    projectName: 'Android Utils',
    themeConfig: {
        navbar: {
            title: 'Android Utils',
            logo: {
                alt: 'My Site Logo',
                src: 'img/site/logo.svg',
            },
            items: [
                {
                    to: 'docs/',
                    activeBasePath: 'docs',
                    label: 'Docs',
                    position: 'left',
                },
                {
                    href: '/docs/reference/androidutils/index.html',
                    label: 'API Reference',
                    position: 'left',
                },
                {
                    href: 'https://github.com/JeovaniMartinez/Android-Utils',
                    label: 'GitHub',
                    position: 'right',
                },
            ],
        },
        footer: {
            style: 'dark',
            copyright: `Copyright © ${new Date().getFullYear()} Jeovani Martínez. Built with Docusaurus.`,
        },
    },
    presets: [
        [
            '@docusaurus/preset-classic',
            {
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                    editUrl:
                        'https://github.com/JeovaniMartinez/Android-Utils/edit/documentation/',
                },
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },
            },
        ],
    ],
};
