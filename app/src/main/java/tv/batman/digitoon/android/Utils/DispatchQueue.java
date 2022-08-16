package tv.batman.digitoon.android.Utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;

// created by morti

public class DispatchQueue extends Thread {

    private volatile Handler handler = null;
    private final CountDownLatch syncLatch = new CountDownLatch(1);

    public DispatchQueue(String threadName) {
        setName(DispatchQueue.class.getSimpleName() + " " + threadName);
        start();
    }

    public void sendMessage(Message msg, int delay) {
        try {
            syncLatch.await();
            handler.sendMessageDelayed(msg, Math.max(0, delay));
        } catch (Exception ignore) {

        }
    }

    public void cancelRunnable(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        try {
            syncLatch.await();
            handler.removeCallbacks(runnable);
        } catch (Exception e) {
            AppLog.e(DispatchQueue.class, e.getMessage());
        }
    }

    public void cancelRunnable(Runnable[] runnables) {
        if (runnables == null || runnables.length <= 0) {
            return;
        }
        try {
            syncLatch.await();
            for (Runnable runnable : runnables) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            AppLog.e(DispatchQueue.class, e.getMessage());
        }
    }

    public void postRunnable(Runnable runnable) {
        postRunnable(runnable, 0);
    }

    public void postRunnable(Runnable runnable, long delay) {
        if (runnable == null) {
            return;
        }
        try {
            syncLatch.await();
        } catch (Exception e) {
            AppLog.e(DispatchQueue.class, e.getMessage());
        }
        if (delay <= 0) {
            handler.post(runnable);
        } else {
            handler.postDelayed(runnable, delay);
        }
    }

    public void cleanupQueue() {
        try {
            syncLatch.await();
            handler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            AppLog.e(DispatchQueue.class, e.getMessage());
        }
    }

    public void handleMessage(Message inputMessage) {

    }

    public void recycle() {
        handler.getLooper().quit();
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                DispatchQueue.this.handleMessage(msg);
            }
        };
        syncLatch.countDown();
        Looper.loop();
    }
}
