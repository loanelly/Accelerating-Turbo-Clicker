package com.lnl.engineering;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TurboClicker {
    private final Runnable clickAction;
    private final long startDelayMs;
    private final long initialIntervalMs;
    private final long minIntervalMs;
    private final long accelerationStepMs;

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> currentTask;
    private long currentIntervalMs;
    private boolean isPressed = false;

    public TurboClicker(Runnable clickAction, long startDelayMs, long initialIntervalMs, long minIntervalMs, long accelerationStepMs) {
        this.clickAction = clickAction;
        this.startDelayMs = startDelayMs;
        this.initialIntervalMs = initialIntervalMs;
        this.minIntervalMs = minIntervalMs;
        this.accelerationStepMs = accelerationStepMs;
    }

    // Вызывать при нажатии (зажатии) кнопки
    public synchronized void onPointerDown() {
        if (isPressed) return;
        isPressed = true;
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // 1. Мгновенный первый клик
        clickAction.run(); 
        currentIntervalMs = initialIntervalMs;

        // 2. Запуск планировщика после начальной задержки
        currentTask = scheduler.schedule(this::runTurboLoop, startDelayMs, TimeUnit.MILLISECONDS);
    }

    // Вызывать, когда кнопку отпустили
    public synchronized void onPointerUp() {
        isPressed = false;
        if (currentTask != null) currentTask.cancel(true);
        if (scheduler != null) scheduler.shutdownNow();
    }

    private void runTurboLoop() {
        if (!isPressed) return;
        
        clickAction.run();
        
        // Ускоряем шаг, уменьшая интервал
        currentIntervalMs = Math.max(minIntervalMs, currentIntervalMs - accelerationStepMs);
        
        // Рекурсивно планируем следующий клик с новым интервалом
        if (isPressed && !scheduler.isShutdown()) {
            currentTask = scheduler.schedule(this::runTurboLoop, currentIntervalMs, TimeUnit.MILLISECONDS);
        }
    }
}
