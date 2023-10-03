package per.chaos.infra.utils.pausable_task;

import java.util.Objects;

public class ResumableThread extends Thread {
    // 对象锁
    private final Object syncLock;
    // 暂停状态
    private volatile boolean isPaused;
    // 任务
    private final Runnable task;
    // 暂停前回调
    private final Runnable pauseCallback;
    // 暂停恢复回调
    private final Runnable resumeCallback;
    // 线程被打断后回调
    private final Runnable interruptedCallback;

    public ResumableThread(String name, Runnable task, Runnable pauseCallback, Runnable resumeCallback, Runnable interruptedCallback) {
        super.setName(name);
        this.syncLock = new Object();
        this.isPaused = false;
        this.task = task;
        this.pauseCallback = pauseCallback;
        this.resumeCallback = resumeCallback;
        this.interruptedCallback = interruptedCallback;
    }

    @Override
    public void run() {
        super.run();
        for (; ; ) {
            synchronized (syncLock) {
                if (isPaused) {
                    try {
                        if (Objects.nonNull(pauseCallback)) {
                            pauseCallback.run();
                        }
                    } catch (Exception e) {
                        // do nothing
                    }

                    try {
                        syncLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("ResumableThread was in wait state, but got interrupted");

                        try {
                            if (Objects.nonNull(interruptedCallback)) {
                                interruptedCallback.run();
                            }
                        } catch (Exception ex) {
                            // do nothing
                        }

                        break;
                    }

                    try {
                        if (Objects.nonNull(resumeCallback)) {
                            resumeCallback.run();
                        }
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }

            try {
                task.run();
            } catch (Exception e) {
                System.out.println("ResumableThread was execute task, but got interrupted");

                try {
                    if (Objects.nonNull(interruptedCallback)) {
                        interruptedCallback.run();
                    }
                } catch (Exception ex) {
                    // do nothing
                }

                break;
            }
        }
    }

    public void pauseExecute() {
        synchronized (syncLock) {
            isPaused = true;
        }
    }

    public void resumeExecute() {
        synchronized (syncLock) {
            isPaused = false;
            syncLock.notifyAll();
        }
    }
}
