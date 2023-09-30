package per.chaos.infra.utils.pausable_task;

public class ResumableThread extends Thread {
    // 对象锁
    private final Object syncLock;
    // 暂停状态
    private volatile boolean isPaused;
    // 执行任务
    private final Runnable callback;

    public ResumableThread(String name, Runnable callback) {
        super.setName(name);
        this.syncLock = new Object();
        this.isPaused = false;
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();
        for (; ; ) {
            synchronized (syncLock) {
                if (isPaused) {
                    try {
                        syncLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("ResumableThread was in wait state, but got interrupted");
                        break;
                    }
                }
            }

            try {
                callback.run();
            } catch (Exception e) {
                System.out.println("ResumableThread was execute task, but got interrupted");
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
