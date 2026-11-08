# 🚗 BMB Autos — Full-Stack Automotive Dealership Platform

[![Android CI](https://github.com/thabo-bmb/bmb-autos-android/actions/workflows/android.yml/badge.svg)](https://github.com/thabo-bmb/bmb-autos-android/actions)
[![AWS EC2 Deployed](https://img.shields.io/badge/AWS%20EC2-Live%20Backend-orange?logo=amazon-aws)](http://ec2-16-171-44-236.eu-north-1.compute.amazonaws.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin)](https://kotlinlang.org/)
[![PHP](https://img.shields.io/badge/PHP-8.2-blue?logo=php)](https://www.php.net/)

---

## 📖 1. Project Overview & Purpose
**BMB Autos** is a comprehensive, production-ready digital automotive dealership platform developed as a final year Work Integrated Learning (WIL) capstone project. 

The platform bridges the gap between modern customer mobility and dealership operations through a robust full-stack architecture:
*   **Web Platform (Website & Admin Panel):** Empowers dealership administrators to manage vehicle inventory, stock quantities, pricing, and view customer orders in real-time.
*   **Native Android Application (Kotlin):** Delivers a seamless mobile experience for customers to browse vehicle showrooms, purchase cars securely, book certified workshop services, view transaction receipts, and manage their personal vehicle garage ("My Garage").

---

## 🛠️ 2. Tech Stack

| Tier | Technologies Used |
| :--- | :--- |
| **Mobile App (Android)** | Kotlin, Material Design 3, Retrofit 2, OkHttp, Glide, Firebase Auth (Google SSO), Coroutines |
| **Web Backend & API** | PHP 8.2, MySQL, RESTful API Architecture, Apache HTTP Server |
| **Cloud Infrastructure** | AWS EC2 (Elastic Compute Cloud), Linux Ubuntu Server |
| **DevOps & CI/CD** | GitHub Actions (Automated builds, linting, and unit testing) |

---

## 📱 3. Key Features (Android App)
*   🔑 **Secure Authentication & Google SSO:** Email/password login/registration with Firebase Google Single Sign-On integration.
*   🔄 **Persistent Login (Auto-Login):** Automatically verifies stored `API_TOKEN` in `SharedPreferences` on app launch via `SplashActivity`, routing returning users straight to the dashboard.
*   🚗 **"My Garage":** Dedicated hub where users can view vehicles they have successfully purchased and book maintenance directly for their owned cars.
*   💳 **Seamless Purchasing Flow:** Instant car purchasing with automated stock decrement on the AWS backend and instant transaction record creation.
*   🧾 **Digital Receipt Generation:** Generates printable transaction receipts hosted on AWS (`/receipt.php?transaction_id=ID`), accessible directly via browser intents.
*   🔔 **System Push Notifications:** High-priority local system tray notifications for successful booking appointments and vehicle purchases.
*   🎨 **Modern UI & Dark Mode:** Built with Material 3 components, bubbly icon badges, and fully persistent night mode toggles.
*   🔍 **Advanced Search:** Real-time filtering across vehicle make, model, and color.

---

## 🌐 4. Key Features (Website & Admin Platform)
*   🚘 **Extensive Showroom Inventory:** 50+ unique vehicles featuring detailed specs, color variants, stock quantities, and high-res imagery.
*   📱 **Responsive Web Design:** Fully optimized across mobile, tablet, and desktop viewports.
*   🔎 **Advanced Filtering:** Instant filtering by price range, year, make, and body type.
*   🛡️ **Admin Dashboard:** Secure backend portal for inventory management, stock level tracking, and customer booking administration.

---

## ☁️ 5. Live Deployment Details
The backend REST API and MySQL database are fully deployed and hosted live on an **AWS EC2 instance**. No local WAMP/XAMPP server or Ngrok tunneling is required.

*   **Live API Base URL:** `http://ec2-16-171-44-236.eu-north-1.compute.amazonaws.com/api/`
*   **Live Web Platform:** `http://ec2-16-171-44-236.eu-north-1.compute.amazonaws.com/`

---

## ⚙️ 6. Setup & Installation

### Android App Repository:
1. Clone the Android repository:
   ```bash
   git clone https://github.com/thabo-bmb/bmb-autos-android.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle and run the app on an Android emulator or physical device (API 24+). *Note: The app automatically connects to the live AWS backend endpoint.*

### Web & API Repository:
1. Clone the Web API repository:
   ```bash
   git clone https://github.com/thabo-bmb/bmb-autos-backend.git
   ```
2. Deploy the PHP files to any Apache/PHP server or review the source code structure.

---

## 🔄 7. CI/CD Pipeline
The project utilizes **GitHub Actions** for continuous integration. Every push and pull request triggers an automated workflow (`android.yml`) that:
*   Sets up the JDK environment.
*   Caches Gradle dependencies.
*   Executes unit tests (`app:testDebugUnitTest`).
*   Builds the debug APK (`app:assembleDebug`) to guarantee code health and compilation success.

---

## 🚀 8. Release Notes (v2.0.0)

| Version | Release Date | Key Upgrades & Changes |
| :---: | :---: | :--- |
| **v1.0.0** | *Prototype* | Basic local WAMP setup, static XML views, manual server switching via Ngrok. |
| **v2.0.0** | *Current Release* | • Migrated backend & MySQL database to **AWS EC2**.<br>• Added **Persistent Login** via `SplashActivity`.<br>• Implemented **"My Garage"** and **Digital Receipts**.<br>• Upgraded UI to Material 3 with bubbly icons & dark mode.<br>• Automated inventory stock decrement on purchase. |

---

## 🎥 9. Demonstration Video
[![Watch the Demo Video](https://img.shields.io/badge/YouTube-Watch%20Demo-red?logo=youtube&logoColor=white)](https://youtube.com/placeholder-link-bmb-autos)
*(Click the badge above to watch the full walkthrough demonstration video).*

---

## 🔗 10. Repositories
*   📱 **Android App Repository:** [GitHub - bmb-autos-android](https://github.com/thabo-bmb/bmb-autos-android)
*   💻 **Website & API Repository:** [GitHub - bmb-autos-backend](https://github.com/thabo-bmb/bmb-autos-backend)
