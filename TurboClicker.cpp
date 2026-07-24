#include <iostream>
#include <thread>
#include <chrono>
#include <atomic>
#include <functional>

class TurboClicker {
private:
    std::function<void()> clickAction;
    std::chrono::milliseconds startDelay;
    std::chrono::milliseconds initialInterval;
    std::chrono::milliseconds minInterval;
    std::chrono::milliseconds accelerationStep;

    std::atomic<bool> isPressed{false};
    std::thread workerThread;

    void turboLoop() {
        // 1. Начальная задержка перед ускорением
        std::this_thread::sleep_for(startDelay);

        std::chrono::milliseconds currentInterval = initialInterval;

        // 2. Цикл автокликов с нарастающим темпом
        while (isPressed) {
            clickAction();
            std::this_thread::sleep_for(currentInterval);

            // Ускорение
            if (currentInterval > minInterval) {
                currentInterval -= accelerationStep;
                if (currentInterval < minInterval) currentInterval = minInterval;
            }
        }
    }

   public:
    TurboClicker(std::function<void()> action, int delayMs, int initIntervalMs, int minIntervalMs, int stepMs)
        : clickAction(action),
          startDelay(delayMs),
          initialInterval(initIntervalMs),
          minInterval(minIntervalMs),
          accelerationStep(stepMs) {}

    ~TurboClicker() {
        onPointerUp();
    }

    // Вызывать при зажатии кнопки
    void onPointerDown() {
        if (isPressed.exchange(true)) return; // Защита от повторного вызова

        clickAction(); // Первый мгновенный клик
        workerThread = std::thread(&TurboClicker::turboLoop, this);
    }

    // Вызывать, когда кнопку отпустили
    void onPointerUp() {
        if (isPressed.exchange(false)) {
            if (workerThread.joinable()) {
                workerThread.join();
            }
        }
    }
};
