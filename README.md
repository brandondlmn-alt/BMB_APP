# 🚗 BMB Autos - Android Mobile Application

## 📖 Project Overview
BMB Autos is a comprehensive automotive dealership platform. This repository contains the native **Android Mobile Application** built in Kotlin, which serves as the mobile extension of a PHP/MySQL web platform. 

The purpose of this app is to digitize the customer experience, allowing users to browse inventory, securely purchase vehicles, book workshop services, and manage their profiles on the go.

## ✨ Key Features
*   **Secure Authentication:** Standard Email/Password login and Registration.
*   **Google SSO:** Seamless Single Sign-On integrated via Firebase.
*   **Modern UI/UX:** Built with Material 3 components, custom vector icons, and a Tailwind-inspired color palette.
*   **Dark Mode:** Fully persistent night mode toggle.
*   **Vehicle Showroom:** Browse available cars with real-time search and unique stock numbers.
*   **Purchasing System:** Buy vehicles directly through the app, which updates the database and logs the transaction.
*   **Smart Service Booking:** A system that verifies if the user owns a vehicle before allowing them to book a service.
*   **Trade-In Evaluations:** Submit vehicles for grading, automatically generating a workshop booking.
*   **Push Notifications:** Local system notifications for successful actions.

---

## 📸 App Screenshots

| Login | Home Screen | Showroom |
| :---: | :---: | :---: |
| ![Login](images/Login.jpg) | ![Home Screen](images/home_screen.jpg) | ![Showroom](images/showroom.jpg) |

| Dark Mode | Sign Up | Settings & Profile |
| :---: | :---: | :---: |
| ![Dark Mode](images/dark_mode.jpg) | ![Sign Up](images/sign_up.jpg) | ![Settings](images/settings_&_profiles.jpg) |

| Sell Your Car (Trade-In) | My Bookings | My Purchases |
| :---: | :---: | :---: |
| ![Sell](images/sell.jpg) | ![My Bookings](images/my_bookings.jpg) | ![My Purchases](images/my_purchases.jpg) |

---

## 🛠️ Tech Stack
*   **Language:** Kotlin
*   **Networking:** Retrofit & OkHttp
*   **Image Loading:** Glide
*   **Authentication:** Firebase Auth (Google SSO) + Custom PHP Token Auth
*   **Backend:** PHP / MySQL (REST API)
*   **CI/CD:** GitHub Actions

---

## 💻 How to Run and Use the App (Local Setup via Ngrok)

*Note: During development, cloud hosting platforms (Azure, ProFreeHost) experienced regional policy restrictions and DNS propagation delays. To ensure a fully functional demonstration, the app is currently configured to connect to a local WAMP server via an Ngrok secure tunnel. These cloud hosting issues will be resolved for the final Task 3 submission.*

### Prerequisites:
1. **WAMP Server** (or XAMPP) installed on your PC.
2. **Ngrok** installed and authenticated on your PC.
3. The `BMB_AUTOS` PHP backend and MySQL database running on WAMP.

### Step-by-Step Setup:

1. **Start your local server:** Ensure WAMP is running and the icon is green (Put Online).
2. **Start the Ngrok Tunnel:** Open a terminal (Command Prompt) where Ngrok is installed and run:
   ```bash
   ngrok http 80
   ```
3. **⚠️ UPDATE THE URL IN THE CODE (Crucial Step):**
   If you generated a new Ngrok link, you must update it in the Android Studio code before running:
   
   Open the file: `app/src/main/java/com/example/bmbautos/LoginActivity.kt`
   
   Scroll down to the bottom to the `showServerDialog()` function.
   
   Find this exact line:
   ```kotlin
   1 -> "https://YOUR-NGROK-URL-HERE.ngrok-free.app/BMB_AUTOS/api/"
   ```
   Replace `https://YOUR-NGROK-URL-HERE.ngrok-free.app` with your new Ngrok Forwarding URL.
   
   **Important:** Ensure the URL keeps the `/BMB_AUTOS/api/` at the very end.

4. **Click Build -> Clean Project and Build -> Rebuild Project** in Android Studio.

5. **Launch the App:** Run the app on a physical device or emulator.

6. **Select the Server:** On the Login screen, tap the text at the very bottom that says "Current Server". Select "Ngrok Tunnel (Physical Phone)" from the popup dialog.

7. **Log In:** Use the demo credentials (`thabo@bmbautos.co.za` / `welcome123`) or register a new account.
