## Dependencies Update

### Important considerations to keep in mind when updating project dependencies in Gradle files

- Update the dependencies of the project modules to the latest available version.
- If a dependency generates conflicts, try using an older version until the files can be successfully synchronized and the project 
  can be compiled correctly.
- If the Gradle version is updated, update the value of lintVersion and ensure that the lintcheck module functions correctly.
- When updating Dokka for documentation, verify that the documentation is generated correctly with the new version of the plugin.
- Verify that Firebase Analytics is functioning correctly and sending events to the server.
- Verify that Firebase Crashlytics is functioning correctly and sending any errors occurring in the app to the server.
- Verify that everything in the app is functioning correctly after performing the update.
