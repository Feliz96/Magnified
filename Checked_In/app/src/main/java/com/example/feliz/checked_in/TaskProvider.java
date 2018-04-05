package com.example.feliz.checked_in;

import android.os.AsyncTask;

/**
 * Created by Feliz on 2017/09/16.
 */

public interface TaskProvider<T extends AsyncTask<Void,?,?>> {
    T getTask(int num);
}