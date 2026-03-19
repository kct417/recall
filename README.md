# Recall

Recall is a personal bookmarking and organization tool that lets users save, manage, and revisit useful content in a structured way. Users can create bookmarks with a name, URI, description, and customizable tags, making it easy to categorize and search through saved items. The app supports quick addition of links, filtering bookmarks by name, and organizing related information like notes and multiple URIs under a single entry.It helps users keep track of resources efficiently and retrieve them quickly.

## Figma

-   [Figma Prototype](https://www.figma.com/proto/LJyZFEtqjYudlsJjc6CB9E/Wireframe-Kit-for-Android--Community-?node-id=42-24174&t=09fMxx0FwaVr8rAB-1)

## Features

This app is built using modern Android development tools, primarily Jetpack Compose for the entire UI layer. It uses Compose components such as LazyColumn for efficiently rendering lists, Material 3 elements like Card, Text, OutlinedTextField, and SearchBar for UI design, and BoxWithConstraints to support responsive layouts that adapt between vertical and horizontal screen orientations. State is managed reactively using StateFlow and MutableStateFlow, with lifecycle-aware collection via collectAsStateWithLifecycle, ensuring the UI updates automatically as data changes. Navigation is handled through Navigation Compose with type-safe routes powered by Kotlin serialization. For data persistence, the app uses a Room-based repository pattern, exposing data as Flow and combining multiple streams (bookmarks, tags, URIs, and notes) using operators like combine and flatMapLatest to produce a unified UI state. The app also integrates with core Android features such as Intents, enabling users to open links (Intent.ACTION_VIEW), share bookmarks (Intent.ACTION_SEND), and receive shared content from other apps, which is then parsed and used to prefill the add-bookmark dialog. Additional libraries and features include kotlinx.serialization for encoding and decoding shared data, and experimental Compose components like FlowRow for flexible tag layouts.

## Requirements

This app was built and tested on an Android emulator running Android 14 (UpsideDownCake, x86_64, API 34). It requires an internet connection for certain features, such as opening bookmarked links, but can function offline for managing and viewing saved bookmarks.
