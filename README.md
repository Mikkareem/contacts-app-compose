# Contacts App Compose

This app is built for managing the Random contacts as well as the Device Contacts as well.

- *100% Jetpack Compose*
- Ktor Client for Network Request
- Room for Local Storage
- Kotlinx serialization for JSON Parsing
- Compose Navigation for Multiple screen navigation
- Koin for Dependency Injection
- Coil for Image loading
- Adaptive UI for Different Screen Sizes
- Usage of Kotlin Specific features like (Extension functions, Sealed Class/Interfaces)
- Type safe URL building for navigation and API Calls

Adaptive UIs:
1) Tabs Screen and Detail Screens have adaptive UIs.
2) Dynamic color support is OFF for now.


Considerations: (These things, not able to implement within the time constraints, But I can explain how to achieve this in our app)
1) Font changes
2) stringResource() usage
3) Except CompactWidth devices, Add Device/Random is not yet implemented in TwoPane screens


Usage:
Device Contact Add/Edit:
1) One can Add/edit at most 3 phone nos(Home, Mobile, Work each)
2) One can add/edit at most 3 emails (Home, Mobile, Work each)
3) One can add 2 events (Birthday, Anniversary) each

Global Search:
1) Individual Search UI is there, but not implemented.
2) Global Search (Search Icon Click) is implemented as separate screen, it will show search result as both random and device contacts
