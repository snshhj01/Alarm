/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * package-level logging flag
 */

package kr.co.miracom.alarm.util;

import android.provider.AlarmClock;
import android.util.Config;
import android.util.Log;

public class Logger {

    private static final String TAG = Logger.class.getSimpleName();
    private static final String EMPTY = "";

    public static int v(String tag, String format, Object... args) {
        return Log.v(tag, format(format, args));
    }

    public static int v(String tag, String msg, Throwable e) {
        return Log.v(tag, msg, e);
    }

    public static int v(String tag, String format, Throwable e, Object... args) {
        return Log.v(tag, format(format, args), e);
    }

    public static int d(String tag, String format, Object... args) {
        return Log.d(tag, format(format, args));
    }

    public static int d(String tag, String msg, Throwable e) {
        return Log.d(tag, msg, e);
    }

    public static int d(String tag, String format, Throwable e, Object... args) {
        return Log.d(tag, format(format, args), e);
    }

    public static int w(String tag, String format, Object... args) {
        return Log.w(tag, format(format, args));
    }

    public static int w(String tag, String msg, Throwable e) {
        return Log.w(tag, msg, e);
    }

    public static int w(String tag, String format, Throwable e, Object... args) {
        return Log.w(tag, format(format, args), e);
    }

    public static int i(String tag, String format, Object... args) {
        return Log.i(tag, format(format, args));
    }

    public static int i(String tag, String msg, Throwable e) {
        return Log.i(tag, msg, e);
    }

    public static int i(String tag, String format, Throwable e, Object... args) {
        return Log.i(tag, format(format, args), e);
    }

    public static int e(String tag, String format, Object... args) {
        return Log.e(tag, format(format, args));
    }

    public static int e(String tag, String msg, Throwable e) {
        return Log.e(tag, msg, e);
    }

    public static int e(String tag, String format, Throwable e, Object... args) {
        return Log.e(tag, format(format, args), e);
    }

    public static int v(Class tag, String format, Object... args) {
        return Log.v(tag.getSimpleName(), format(format, args));
    }

    public static int v(Class tag, String msg, Throwable e) {
        return Log.v(tag.getSimpleName(), msg, e);
    }

    public static int v(Class tag, String format, Throwable e, Object... args) {
        return Log.v(tag.getSimpleName(), format(format, args), e);
    }

    public static int d(Class tag, String format, Object... args) {
        return Log.d(tag.getSimpleName(), format(format, args));
    }

    public static int d(Class tag, String msg, Throwable e) {
        return Log.d(tag.getSimpleName(), msg, e);
    }

    public static int d(Class tag, String format, Throwable e, Object... args) {
        return Log.d(tag.getSimpleName(), format(format, args), e);
    }

    public static int w(Class tag, String format, Object... args) {
        return Log.w(tag.getSimpleName(), format(format, args));
    }

    public static int w(Class tag, String msg, Throwable e) {
        return Log.w(tag.getSimpleName(), msg, e);
    }

    public static int w(Class tag, String format, Throwable e, Object... args) {
        return Log.w(tag.getSimpleName(), format(format, args), e);
    }

    public static int i(Class tag, String format, Object... args) {
        return Log.i(tag.getSimpleName(), format(format, args));
    }

    public static int i(Class tag, String msg, Throwable e) {
        return Log.i(tag.getSimpleName(), msg, e);
    }

    public static int i(Class tag, String format, Throwable e, Object... args) {
        return Log.i(tag.getSimpleName(), format(format, args), e);
    }

    public static int e(Class tag, String format, Object... args) {
        return Log.e(tag.getSimpleName(), format(format, args));
    }

    public static int e(Class tag, String msg, Throwable e) {
        return Log.e(tag.getSimpleName(), msg, e);
    }

    public static int e(Class tag, String format, Throwable e, Object... args) {
        return Log.e(tag.getSimpleName(), format(format, args), e);
    }

    private static String format(String format, Object... args) {
        try {
            return String.format(format == null ? EMPTY : format, args);
        } catch (RuntimeException e) {
            Logger.w(TAG, "format error. reason=%s, format=%s", e.getMessage(), format);
            return String.format(EMPTY, format);
        }

    }
}
