# 🚀 Accelerating Turbo Clicker (C++ & Java)



A lightweight, high-performance code block implementing a **holding-down clicker with dynamic acceleration**. Perfect for camera burst-shooting features (long-press photo/video capture), smart UI increments, and fast scrolling systems.

---

## ⚙️ How It Works

1. **Instant Trigger:** As soon as the button is pressed, the first action executes instantly to eliminate input lag.
2. **Initial Delay:** If the user keeps holding the button, the script waits for a predefined `startDelay` period.
3. **Turbo Phase:** If the button is still held after the delay, an automated clicking loop starts. The interval between clicks systematically drops (speeds up) with every click until it reaches `minInterval`.
4. **Instant Release:** The loop terminates immediately upon releasing the button.

---

## 📦 Repository Structure

The project contains pure, dependency-free implementations tailored for different application layers:

*   [**`TurboClicker.java`**](https://github.com/loanelly/Accelerating-Turbo-Clicker/blob/main/TurboClicker.java) – Async implementation using `ScheduledExecutorService`. Ideal for Android Camera Apps or Java Desktop GUIs.
*   [**`TurboClicker.cpp`**](https://github.com/loanelly/Accelerating-Turbo-Clicker/blob/main/TurboClicker.cpp) – Thread-safe C++11 implementation using `std::thread` and `std::chrono`. Perfect for Game Engines (Unreal/Custom) and Embedded IoT devices.

---

## 🚀 Quick Start / Integration

### Java Example

```java
// Define action (e.g., Take Photo)
Runnable takePhoto = () -> System.out.println("📸 Photo Captured!");

// Setup: action, delay (500ms), start interval (400ms), max speed (50ms), step (40ms)
TurboClicker clicker = new TurboClicker(takePhoto, 500, 400, 50, 40);

// Link to UI Elements
button.setOnMousePressed(e -> clicker.onPointerDown());
button.setOnMouseReleased(e -> clicker.onPointerUp());
```

### C++ Example

```cpp
#include "TurboClicker.cpp"

void takeSnapshot() {
    std::cout << "📸 Burst shot taken!" << std::endl;
}

int main() {
    // Setup parameters: action, delay, initial_interval, min_interval, accel_step (in ms)
    TurboClicker clicker(takeSnapshot, 500, 400, 50, 40);

    // Simulated hold-down event
    clicker.onPointerDown(); 
    std::this_thread::sleep_for(std::chrono::seconds(3)); // Holding for 3 seconds
    clicker.onPointerUp();   // Released

    return 0;
}
```

## 🛠️ Configuration Parameters

| Parameter | Type | Description | Default Value |
| :--- | :--- | :--- | :--- |
| `startDelay` | `long / int` | Delay before the turbo-burst starts (ms) | `500 ms` |
| `initialInterval` | `long / int` | Starting speed between burst cycles (ms) | `400 ms` |
| `minInterval` | `long / int` | Maximum caps lock speed (ms) | `50 ms` |
| `accelerationStep`| `long / int` | Interval deduction per click cycle (ms) | `40 ms` |

---

## 📜 License

Distributed under the **MIT License**. Created by [loanelly](https://github.com/loanelly) / **LNL-Engineering**.
