module.exports = {
    title: 'Android Utils',
    tagline: 'Set of utilities for Android app development',
    url: 'https://jeovanimartinez.github.io/Android-Utils/',
    baseUrl: '/',
    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',
    favicon: 'img/favicon.ico',
    organizationName: 'Jeovani Martínez',
    projectName: 'Android Utils',
    themeConfig: {
        navbar: {
            title: 'Android Utils',
            logo: {
                alt: 'My Site Logo',
                src: 'img/logo.svg',
            },
            items: [
                {
                    to: 'docs/',
                    activeBasePath: 'docs',
                    label: 'Docs',
                    position: 'left',
                },
                {
                    href: 'https://jeovanimartinez.github.io/Android-Utils/reference/androidutils/index.html',
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
