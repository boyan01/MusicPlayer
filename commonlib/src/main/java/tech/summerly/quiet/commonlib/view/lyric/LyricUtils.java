package tech.summerly.quiet.commonlib.view.lyric;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.summerly.quiet.commonlib.BuildConfig;


/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

final class LyricUtils {
    private static final String TAG = "LyricUtils";

    private LyricUtils() {
    }

    static Lyric parseLyricFromInputStream(InputStream inputStream, @Nullable String charsetName) throws LyricParseException {
        return new LyricParser(charsetName).parse(inputStream);
    }

    /**
     * 将毫秒化成 分钟:秒钟 的形式，如 123:00
     */
    @SuppressLint("DefaultLocale")
    static String toMusicTimeStamp(int millisecond) {
        millisecond = millisecond / 1000;
        int second = millisecond % 60;
        int minute = millisecond / 60;
        return String.format("%02d:%02d", minute, second);
    }


    private static class LyricParser {
        private static final String CHARSET_DEFAULT = "utf-8";

        LyricParser(String charsetName) {
            this.lyric = new Lyric();
            this.encoding = charsetName == null ? CHARSET_DEFAULT : charsetName;
        }


        /**
         * file text encoding
         */
        private String encoding;

        private Lyric lyric;

        Lyric parse(InputStream inputStream) throws LyricParseException {
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, encoding);
                reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "parse: " + line);
                    }
                    parseLine(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new LyricParseException("failed to read file", e);
            } finally {
                closeSilently(reader);
                closeSilently(inputStreamReader);
                closeSilently(inputStream);
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "parse: " + lyric);
            }
            return lyric;
        }

        @SuppressWarnings("StatementWithEmptyBody")
        private void parseLine(String line) {
            if (line.startsWith("[ti:")) {//TODO lyric info
//                lyric.setTitle(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[ar:")) {
//                lyric.setArtist(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[al:")) {
//                lyric.setAlbum(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[au:")) {
//                lyric.setLyricist(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[by:")) {
//                lyric.setFileMaker(line.substring(4, line.length() - 1));
            } else {
                String regex = "\\[\\d{2}:\\d{2}.\\d{2,3}]";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String timeMatcher = matcher.group();

                    //获取时间戳
                    int timeStamp = stamp2Int(timeMatcher.substring(1, timeMatcher.length() - 1));
                    //获取内容
                    String[] contents = pattern.split(line);

                    if (contents.length >= 1) {
                        lyric.putLyricLine(timeStamp, contents[contents.length - 1]);
                    }
                }
            }
        }

        /**
         * 将形如 11:10.100 的时间计算成毫秒数
         */
        private int stamp2Int(final String result) {
            final int indexOfColon = result.indexOf(":");
            final int indexOfPoint = result.indexOf(".");

            final int minute = Integer.parseInt(result.substring(0, indexOfColon));
            final int second = Integer.parseInt(result.substring(indexOfColon + 1, indexOfPoint));
            final int millisecond;
            if (result.length() - indexOfPoint == 2) {
                millisecond = Integer.parseInt(result.substring(indexOfPoint + 1, result.length())) * 10;
            } else {
                millisecond = Integer.parseInt(result.substring(indexOfPoint + 1, result.length()));
            }
            return ((((minute * 60) + second) * 1000) + millisecond);
        }
    }

    static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            //ignore
        }
    }

}
