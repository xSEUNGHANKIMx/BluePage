package com.example.bluepage.utils;

public class UtilHangul {

    private static String toHexString(int decimal) {
        Long intDec = Long.valueOf(decimal);
        return Long.toHexString(intDec);
    }

    public static final int KOR_BASE_UNIT = 588;

    public static final int ENG_UPPER_BEGIN_UNICODE = 65; // A
    public static final int ENG_UPPER_END_UNICODE = 90; // Z

    public static final int ENG_LOWER_BEGIN_UNICODE = 97; // a
    public static final int ENG_LOWER_END_UNICODE = 122; // z

    public static final int KOR_JA_BEGIN_UNICODE = 12593; // ㄱ
    public static final int KOR_JA_END_UNICODE = 12622; // ㅎ

    public static final int KOR_MO_BEGIN_UNICODE = 12623; // ㅏ
    public static final int KOR_MO_END_UNICODE = 12643; // ㅣ

    public static final int CHN_UNIFIED_BEGIN_UNICODE = 19968;
    public static final int CHN_UNIFIED_END_UNICODE = 40959;

    public static final int KOR_BEGIN_UNICODE = 44032; // 가
    public static final int KOR_END_UNICODE = 55203; // 힣

    public static final int[] INITIAL_SOUND_UNICODE = { 12593, 12594, 12596,
        12599, 12600, 12601, 12609, 12610, 12611, 12613, 12614, 12615,
        12616, 12617, 12618, 12619, 12620, 12621, 12622 };

    public static final char[] CHO_SUNG = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
        'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    public static final char[] JOONG_SUNG = { 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ',
        'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' };

    public static final char[] JONG_SUNG = { 0, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ',
        'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    /** 문자를 유니코드(10진수)로 변환 후 반환 한다.
     *
     * @param ch
     * @return */
    public static int convertCharToUnicode(char ch) {
        return ch;
    }

    /** 문자열을 유니코드(10진수)로 변환 후 반환 한다.
     *
     * @param str
     * @return */
    public static int[] convertStringToUnicode(String str) {

        int[] unicodeList = null;

        if (str != null) {
            unicodeList = new int[str.length()];
            for (int i = 0; i < str.length(); i++) {
                unicodeList[i] = convertCharToUnicode(str.charAt(i));
            }
        }

        return unicodeList;
    }

    /** 유니코드(16진수)를 문자로 변환 후 반환 한다.
     *
     * @param hexUnicode
     * @return */
    public static char convertUnicodeToChar(String hexUnicode) {
        return (char) Integer.parseInt(hexUnicode, 16);
    }

    /** 유니코드(10진수)를 문자로 변환 후 반환 한다.
     *
     * @param unicode
     * @return */
    public static char convertUnicodeToChar(int unicode) {
        return convertUnicodeToChar(toHexString(unicode));
    }

    /** @param value
     * @return */
    public static String getHangulInitialSound(String value) {

        StringBuffer result = new StringBuffer();

        int[] unicodeList = convertStringToUnicode(value);
        for (int unicode : unicodeList) {

            if ((KOR_BEGIN_UNICODE <= unicode) && (unicode <= KOR_END_UNICODE)) {
                int tmp = (unicode - KOR_BEGIN_UNICODE);
                int index = tmp / KOR_BASE_UNIT;
                result.append(CHO_SUNG[index]);
            } else {
                result.append(convertUnicodeToChar(unicode));
            }
        }

        return result.toString();
    }

    public static boolean isChoSungOnly(char searchChar) {
        boolean isCho = false;
        for (char cho : CHO_SUNG) {
            if (searchChar == cho) {
                isCho = true;
                break;
            }
        }

        return isCho;
    }

    public static boolean[] getIsChoSungList(String name) {
        if (name == null) {
            return null;
        }

        boolean[] choList = new boolean[name.length()];

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            boolean isCho = false;
            for (char cho : CHO_SUNG) {
                if (c == cho) {
                    isCho = true;
                    break;
                }
            }

            choList[i] = isCho;

        }

        return choList;
    }

    public static String getHangulInitialSound(String value, String searchKeyword) {
        return getHangulInitialSound(value, getIsChoSungList(searchKeyword));
    }

    public static String getHangulInitialSound(String value, boolean[] isChoList) {

        StringBuffer result = new StringBuffer();

        int[] unicodeList = convertStringToUnicode(value);
        for (int idx = 0; idx < unicodeList.length; idx++) {
            int unicode = unicodeList[idx];

            if ((isChoList != null) && (idx <= (isChoList.length - 1))) {
                if (isChoList[idx]) {
                    if ((KOR_BEGIN_UNICODE <= unicode)
                        && (unicode <= KOR_END_UNICODE)) {
                        int tmp = (unicode - KOR_BEGIN_UNICODE);
                        int index = tmp / KOR_BASE_UNIT;
                        result.append(CHO_SUNG[index]);
                    } else {
                        result.append(convertUnicodeToChar(unicode));
                    }
                } else {
                    result.append(convertUnicodeToChar(unicode));
                }
            } else {
                result.append(convertUnicodeToChar(unicode));
            }
        }

        return result.toString();
    }

    public static boolean isHangul(char c) {
        return ((KOR_JA_BEGIN_UNICODE <= c) && (c <= KOR_MO_END_UNICODE))
            || ((KOR_BEGIN_UNICODE <= c) && (c <= KOR_END_UNICODE));
    }

    public static boolean isEnglish(char c) {
        return ((ENG_UPPER_BEGIN_UNICODE <= c) && (c <= ENG_UPPER_END_UNICODE))
            || ((ENG_LOWER_BEGIN_UNICODE <= c) && (c <= ENG_LOWER_END_UNICODE));
    }

    public static boolean isChinese(char c) {
        return (CHN_UNIFIED_BEGIN_UNICODE <= c) && (c <= CHN_UNIFIED_END_UNICODE);
    }

    public static int[] getHangulJaSo(char s) {
        int[] result = new int[3];
        int a, b, c;

        c = s - 0xAC00;
        a = c / (21 * 28);
        c = c % (21 * 28);
        b = c / 28;
        c = c % 28;

        result[0] = a;
        result[1] = b;
        result[2] = c;

        return result;
    }

    public static String unicodeToString(int[] s) throws IllegalArgumentException {
        if (s.length != 3) {
            throw new IllegalArgumentException();
        }
        s[0] -= 0x1100;
        s[1] -= 0x1161;
        s[2] -= 0x11a8;
        char c = (char) ((((s[0] * 588) + (s[1] * 28)) + s[2]) + 44032);
        return String.valueOf(c);
    }
}
