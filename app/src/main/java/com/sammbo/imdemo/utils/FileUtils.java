package com.sammbo.imdemo.utils;

import java.util.Locale;

/**
 * time   : 2021/01/27
 * desc   :
 * version:
 */
public class FileUtils {

    public enum SizeUnit {
        Byte,
        KB,
        MB,
        GB,
        TB,
        Auto,
    }

    public static String formatFileSize(long size) {
        return formatFileSize(size, SizeUnit.Auto);
    }

    public static String formatFileSize(long size, SizeUnit unit) {
        if (size < 0) {
            return "";
        }

        final double KB = 1024;
        final double MB = KB * 1024;
        final double GB = MB * 1024;
        final double TB = GB * 1024;
        if (unit == SizeUnit.Auto) {
            if (size < KB) {
                unit = SizeUnit.Byte;
            } else if (size < MB) {
                unit = SizeUnit.KB;
            } else if (size < GB) {
                unit = SizeUnit.MB;
            } else if (size < TB) {
                unit = SizeUnit.GB;
            } else {
                unit = SizeUnit.TB;
            }
        }

        switch (unit) {
            case Byte:
                return size + "B";
            case KB:
                return String.format(Locale.US, "%.1fKB", size / KB);
            case MB:
                return String.format(Locale.US, "%.1fMB", size / MB);
            case GB:
                return String.format(Locale.US, "%.1fGB", size / GB);
            case TB:
                return String.format(Locale.US, "%.1fPB", size / TB);
            default:
                return size + "B";
        }
    }
}
