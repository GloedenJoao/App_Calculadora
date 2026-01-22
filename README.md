# Calculadora

A simple Android calculator built with Android Studio and Kotlin.

## Features
- Standard arithmetic operations: addition, subtraction, multiplication, and division.
- Multi-operator expressions with parentheses for order of operations.
- `Ans` button to reuse the last calculated result in the next expression.

## Getting Started
1. Open the project in Android Studio.
2. Let Gradle sync dependencies.
3. Run the app on an emulator or physical device.

## Run in Android Studio (Step by Step)
1. Launch Android Studio.
2. Click **File > Open...** and select the project root folder (`App_Calculadora`).
3. Wait for **Gradle Sync** to complete (Android Studio will prompt you if needed).
4. Create or select a device:
   - Click the device dropdown and choose an emulator, **or**
   - Click **Device Manager** to create a new virtual device.
5. Set up the Run/Debug configuration if prompted:
   - Click the configuration dropdown and select **app**.
   - If **app** is missing, click **Edit Configurations...** and add an **Android App** config.
   - Select the **app** module, then click **Apply** and **OK**.
6. Click the green **Run** ▶️ button.
7. The app will build and install on the selected device.

## Usage
- Enter expressions using the on-screen buttons.
- Use parentheses to control precedence.
- Tap `Ans` to insert the previous result into a new expression.
- Press `=` to evaluate and store the new `Ans` value.
