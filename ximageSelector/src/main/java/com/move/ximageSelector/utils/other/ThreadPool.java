package com.move.ximageSelector.utils.other;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.Vector;

/**
 * Created by cxj on 2016/5/5.
 * 线程池,可以执行任务,任务是以{@link Runnable}接口的形式
 * 详情请看{@link ThreadPool#invoke(Runnable)}
 */
public class ThreadPool implements Runnable {

    /**
     * 执行任务完毕停止
     */
    private final int stopped_flag = 0;

    /**
     * 无任务停止
     */
    private final int stoppedWithOutTash_flag = 1;

    //私有化构造函数
    private ThreadPool() {
    }

    private static ThreadPool threadPool;

    /**
     * 用于设置图片到控件上
     */
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case stopped_flag: //如果是停止了
                    currentThreadNum--;
                    startThread();
                    break;
                case stoppedWithOutTash_flag:
                    currentThreadNum--;
                    break;
            }
        }
    };

    public static ThreadPool getInstance() {
        if (threadPool == null) {
            threadPool = new ThreadPool();
        }
        return threadPool;
    }

    /**
     * 最大的线程数量
     */
    private int maxThreadNum = 3;

    public void setMaxThreadNum(int maxThreadNum) {
        if (maxThreadNum < 1) {
            maxThreadNum = 1;
        }
        this.maxThreadNum = maxThreadNum;
    }

    /**
     * 当前的线程
     */
    private int currentThreadNum = 0;

    /**
     * 只留的任务的集合
     */
    private Vector<Runnable> runnables = new Vector<Runnable>();


    /**
     * 执行一个任务
     *
     * @param runnable
     */
    public void invoke(Runnable runnable) {
        runnables.add(runnable);
        startThread();
    }

    /**
     * 启动线程
     */
    private void startThread() {
        //如果还没有达到最大的线程数量
        if (currentThreadNum < maxThreadNum) {
            currentThreadNum++;
            Thread t = new Thread(this);
            t.start();
        }
    }

    /**
     * 获取一个任务
     *
     * @return
     */
    @Nullable
    private synchronized Runnable getTask() {
        if (runnables.size() > 0) {
            return runnables.remove(0);
        }
        return null;
    }

    /**
     * 非UI线程执行
     */
    @Override
    public void run() {

        //获取一个任务
        Runnable task = getTask();
        if (task == null) { //如果没有任务,直接返回
            //发送停止的信息
            h.sendEmptyMessage(stoppedWithOutTash_flag);
            return;
        }

        //执行任务
        task.run();

        //发送停止的信息
        h.sendEmptyMessage(stopped_flag);

    }

}
