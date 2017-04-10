package de.ktran.anno1404warenrechner.data;

import android.os.AsyncTask;


public abstract class Task extends AsyncTask<Void, Void, Integer> {
    @Override
    protected final Integer doInBackground(Void... params) {
        doTask();

        return 0;
    }

    public abstract void doTask();

    public static void doAsync(Runnable runnable) {
        new Task() {
            @Override
            public void doTask() {
                runnable.run();
            }
        }.execute();
    }
}
