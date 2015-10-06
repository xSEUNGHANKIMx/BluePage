package com.example.bluepage.utils;

import java.util.List;

public abstract class BaseListSort<T> {

    protected List<T> mLists;

    public BaseListSort(List<T> lists) {
        mLists = lists;
    }

    static char koreanCharacterToIMFUnicode(char s) {
        int[] result = new int[3];
        int a = s - 44032;
        result[0] = 0x1100 + ((a / 28) / 21);
        result[1] = 0x1161 + ((a / 21) % 21);
        result[2] = 0x11a8 + (a % 28);

        char cho = (char) result[0];
        return cho;
    }

    public static String getLable(String name) {
        String label;

        if (UtilHangul.isHangul(name.charAt(0))) {
            label = UtilHangul.getHangulInitialSound(name.substring(0, 1));
        } else if (UtilHangul.isEnglish(name.charAt(0))) {
            label = Character.toString(name.charAt(0)).toUpperCase();
        } else if (UtilHangul.isChinese(name.charAt(0))) {
            label = name.substring(0, 1);
        } else {
            label = "#";
        }

        return label;
    }

    static boolean isKorean(String str) {
        if (str.length() > 0) {
            char c = str.charAt(0);
            return ('\uAC00' <= c) && (c <= '\uD7A3');
        }
        return false;
    }

    static boolean isJapanese(String str) {
        if (str.length() > 0) {
            char c = str.charAt(0);
            return (('\u3040' <= c) && (c <= '\u309F' // hiragana
                ))
                || (('\u30A0' <= c) && (c <= '\u30FF')); // katakana
        }
        return false;
    }

    static boolean isAlphabet(String str) {
        if (str.length() > 0) {
            char c = str.charAt(0);
            return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'));
        }
        return false;
    }
}
