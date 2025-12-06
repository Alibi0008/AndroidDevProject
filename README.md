# 📰 NewsWave 

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg) ![Platform](https://img.shields.io/badge/Platform-Android-green.svg) ![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg) ![Status](https://img.shields.io/badge/Status-Active_Development-red.svg)

> **NewsWave** is a modern Android application designed to aggregate news from around the world. The project follows the **MVP to Scalable Product** strategy: starting as a classic News App and evolving into a multi-format digital kiosk (Magazines, Audio Articles, PDFs).

---

## 📱 Screenshots (Work in Progress)

| News Feed | Article Details | Search | Saved (Offline) |
|:---:|:---:|:---:|:---:|
| ![Feed](https://via.placeholder.com/200x400?text=Feed+Screen) | ![Details](https://via.placeholder.com/200x400?text=Details+Screen) | ![Search](https://via.placeholder.com/200x400?text=Search+Screen) | ![Saved](https://via.placeholder.com/200x400?text=Saved+Screen) |

*(Note: Screenshots will be updated as the UI is developed)*

---

## 🛠 Tech Stack & Libraries

This project is built using modern Android development practices to ensure scalability and maintainability.

* **Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
* **Network:** Retrofit2 + OkHttp3 + Gson (NewsAPI integration)
* **Async Operations:** Coroutines + Flow
* **Local Database:** Room (SQLite abstraction for offline access)
* **Image Loading:** Glide / Coil
* **UI Components:** RecyclerView, CardView, ConstraintLayout, WebView
* **Other:** ViewBinding, Navigation Component

---

## 🚀 Features & Roadmap

The project development is divided into phases. Currently, we are in **Phase 1**.

### ✅ Phase 1: The Foundation (Current Goal)
- [ ] **News Feed:** Display top headlines from NewsAPI.
- [ ] **Categories:** Filter news by Sports, Business, Tech, etc.
- [ ] **Search:** Search articles by keywords.
- [ ] **Details View:** Read full articles via in-app WebView.
- [ ] **Offline Mode:** Save articles to "Favorites" using Room Database.

### 🚧 Phase 2: "Kiozk" Evolution (Planned)
- [ ] **Magazines Shelf:** Horizontal scroll for digital magazines.
- [ ] **PDF Reader:** Integrated viewer for magazine issues.
- [ ] **Audio Mode:** Text-to-Speech (TTS) integration for listening to articles.
- [ ] **Dark Mode:** Adaptive UI for better reading experience.

---

## 🏗 Architecture Overview

The app uses a **Single Source of Truth** approach with a Repository that manages data fetching strategies:

1.  **UI (Activity/Fragment):** Observes `StateFlow` from ViewModel.
2.  **ViewModel:** Manages UI state (Loading/Success/Error) and survives configuration changes.
3.  **Repository:** Decides whether to fetch data from the **Network (NewsAPI)** or **Local DB (Room)**.
4.  **Data Sources:**
    * *Remote:* Retrofit Service.
    * *Local:* Room DAO.

---

## 📦 How to Run

1.  Clone this repository:
    ```bash
    git clone https://github.com/Alibi0008/AndroidDevProject.git
    ```
2.  Open the project in **Android Studio**.
3.  **Important:** You need an API Key from [NewsAPI.org](https://newsapi.org/).
4.  Create a file `apikey.properties` (or add it to `local.properties`) to store your key securely.
5.  Build and Run on Emulator or Device.

---

## 🤝 Contribution

This project is developed for the **Endterm Examination**.
* **Developer:** Tolegen Alibi

---
