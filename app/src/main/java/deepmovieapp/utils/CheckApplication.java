package com.vn.nguyenhoanganhtu.deepmovieapp.utils;

import android.app.Application;

public class CheckApplication {
    private static CheckApplication instance;
    private boolean check;

    private CheckApplication() {}

    public static synchronized CheckApplication getInstance() {
        if (instance == null) {
            instance = new CheckApplication();
        }
        return instance;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
