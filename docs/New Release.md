## New Release

### Instructions for a new library release

- Make sure everything is ready and documented in the library and the test app.
- Run the necessary tests and verify that everything works correctly.
- Update library version code and version name in `build.gradle` (at the project level).
    - Commit and push changes.
- Update `README.md` (at root project folder) if necessary.
    - Update also in the documentation branch into docs/android-utils/introduction.md
- Generate API reference docs (instructions in api-reference/README.md).
    - Copy the generated docs to the documentation branch into api_reference/reference folder.
- Update documentation as required.
    - Add new release notes.
- Build new documentation (in documentation project):
    - Adjust the version in `package.json` to match the library version.
    - Execute the `npm run build` command to generate the build of the documentation for production.
    - Delete all content of the `gh-pages` branch of the project (except .gitignore and README.md), and put all content of the `build` folder in the `gh-pages` branch.
    - Generate a RAR file named `documentation.rar` with all content of the documentation project including the build folder (exclude only .docusaurus, .git, .idea, and node_modules folders).
- Create a pull request and merge develop branch into the master branch.
- Create a new release in GitHub and attach the `documentation.rar` file.
    - In the release description add a link to release notes of the documentation website.
- Test the library in some app and check documentation and API reference on the library website.
