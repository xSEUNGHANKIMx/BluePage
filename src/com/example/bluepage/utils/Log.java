package com.example.bluepage.utils;

public class Log {

    static public boolean SHOW_V = true;
    static public boolean SHOW_D = true;
    static public boolean SHOW_I = true;
    static public boolean SHOW_W = true;
    static public boolean SHOW_E = true;

    static public void d(String tag, String contents) {
        if (SHOW_D) {
            android.util.Log.d(tag, contents);
        }
    }

    static public void i(String tag, String contents) {
        if (SHOW_I) {
            android.util.Log.i(tag, contents);
        }
    }

    static public void e(String tag, String contents) {
        if (SHOW_E) {
            android.util.Log.e(tag, contents);
        }
    }

    static public void e(String tag, String contents, Throwable e) {
        if (SHOW_E) {
            android.util.Log.e(tag, contents);
        }
    }

    static public void v(String tag, String contents) {
        if (SHOW_V) {
            android.util.Log.v(tag, contents);
        }
    }

    static public void w(String tag, String contents) {
        if (SHOW_W) {
            android.util.Log.w(tag, contents);
        }
    }

    static public void w(String tag, String contents, Throwable e) {
        if (SHOW_W) {
            android.util.Log.w(tag, contents);
        }
    }
}
