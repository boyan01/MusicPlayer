package tech.summerly.quiet.commonlib.view.lyric;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.SparseArray;

import java.io.ByteArrayInputStream;


public class Lyric {

    private static final String CHAR_SET = "utf-8";

    Lyric() {

    }

    public Lyric from(String lyric) {
        final ByteArrayInputStream inputStream;
        try {
            inputStream = new ByteArrayInputStream(lyric.getBytes(CHAR_SET));
            return LyricUtils.parseLyricFromInputStream(inputStream, CHAR_SET);
        } catch (Exception e) {
            return null;
        }
    }

    //预计的最大歌词行数
    private static final int MAX_LINES = 50;

    /**
     * key : 歌词的时间戳
     * value : 歌词
     */
    private SparseArray<LyricEntry> lyricEntrySparseArray = new SparseArray<>(MAX_LINES);

    void putLyricLine(int timeStamp, String line) {
        lyricEntrySparseArray.put(timeStamp, new LyricEntry(line, LyricUtils.toMusicTimeStamp(timeStamp)));
    }

    int getTimeStamp(int index) {
        return lyricEntrySparseArray.keyAt(index);
    }

    public int size() {
        return lyricEntrySparseArray.size();
    }

    LyricEntry getLyricEntry(int index) {
        return lyricEntrySparseArray.valueAt(index);
    }

    /**
     * 根据时间戳来寻找匹配当前时刻的歌词
     *
     * @param timeStamp  歌词的时间戳(毫秒)
     * @param anchorLine the start line to search
     * @return index to getLyricEntry
     */
    public int findIndexByTimeStamp(final int timeStamp,
                                    final int anchorLine) {

        int position = anchorLine;
        if (position < 0 || position > size() - 1) {
            position = 0;
        }
        if (getTimeStamp(position) > timeStamp) {//向前查找
            while (getTimeStamp(position) > timeStamp) {
                position--;
                if (position <= 0) {
                    position = 0;
                    break;
                }
            }
        } else {
            while (getTimeStamp(position) < timeStamp) {//向后查找
                position++;
                if (position <= size() - 1 && getTimeStamp(position) > timeStamp) {
                    position--;
                    break;
                }
                if (position >= size() - 1) {
                    position = size() - 1;
                    break;
                }
            }
        }
        return position;
    }


    static class LyricEntry {

        private final String line;
        private StaticLayout staticLayout;
        //时间戳:形如 145:00 冒号前面代表分钟,后面代表秒数
        private final String timeStamp;

        LyricEntry(String line, String timeStamp) {
            this.line = line;
            this.timeStamp = timeStamp;
        }

        void buildLayout(TextPaint paint, int width, float spacingAdd) {
            staticLayout = new StaticLayout(line, paint, width, Layout.Alignment.ALIGN_CENTER,
                    1f, spacingAdd, true);
        }

        StaticLayout getStaticLayout() {
            return staticLayout;
        }

        String getTimeStamp() {
            return timeStamp;
        }

        @Override
        public String toString() {
            return line + "\n";
        }
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "lyricEntrySparseArray=" + lyricEntrySparseArray.toString() +
                '}';
    }
}
