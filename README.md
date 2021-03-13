# Android-Utils Docs

Project with documentation for the Android Utils library

This website is built using [Docusaurus 2](https://v2.docusaurus.io/), a modern static website generator.

---

## Commands

`npm run start` [Start in developer mode] <br/>
`npm run build` [Generate build for production] <br/>
`npm run serve` [Run build in local server] <br/>

---

## Considerations

- The recommended size for the documentation images, to obtain the best quality, is a width of 807px and the desired height.
- All the content of the documentation, including images, is in the docs folder, this for if the documentation is versioned in the future.

## Deploy Instructions

1. In the Android Studio project, generate the library documentation in HTML format, and place all the generated content inside the folder `docs/reference`
2. Adjust references into docs to HTML documentation if its is necessary.
3. In the file `docusaurus.config.js` change `baseUrl: '/'` to `baseUrl: '/Android-Utils/'`
4. Use the `npm run build` command to generate the build of the documentation for production.
5. Copy the folder `docs/reference` into `build/docs` folder.
6. Delete all content of `gh-pages` branch of the project (except .gitignore and README.md), and put all content of `build` folder in gh-pages branch.
7. Create a compressed file of this documentation project (including source code and build) and add the file to the Github release assets for the library target release.
8. Revert changes into `docusaurus.config.js`, to keep `baseUrl: '/'` for develop mode.
