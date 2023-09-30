package per.chaos.infra.utils.pausable_task;

/**
 * PausableThread管理器
 * <p>
 * 简化PausableThread的使用，保证多种状态切换不引起其他异常，
 * 支持停止任务后再次启动新任务
 */
public class ResumableThreadManager {
    // 是否开始
    private boolean isStart;
    // 是否销毁
    private boolean isDispose;
    // 线程，默认为null，即未初始化状态为已销毁状态
    private ResumableThread rThread;
    // 任务
    private final Runnable runnable;

    private void initNewPausableThread() {
        rThread = new ResumableThread(
                "pausable",
                () -> {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        throw new RuntimeException("rethrow");
                    }
                }
        );
    }

    private void initAllState() {
        isStart = false;
        isDispose = true;
    }

    public void start() {
        if (!isStart) {
            if (isDispose) {
                initNewPausableThread();
            }

            isStart = true;
            rThread.start();
        }
    }

    public void stop() {
        if (isStart) {
            isStart = false;
            isDispose = true;
            rThread.interrupt();
            rThread = null;
        }
    }

    public void pause() {
        if (isStart) {
            rThread.pauseExecute();
        }
    }

    public void resume() {
        if (isStart) {
            rThread.resumeExecute();
        }
    }

    public ResumableThreadManager(Runnable runnable) {
        this.runnable = runnable;
        initAllState();
    }
}
