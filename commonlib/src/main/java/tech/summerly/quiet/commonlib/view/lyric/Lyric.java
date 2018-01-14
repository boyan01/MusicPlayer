package tech.summerly.quiet.commonlib.view.lyric;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.SparseArray;


class Lyric {

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

    int size() {
        return lyricEntrySparseArray.size();
    }

    LyricEntry getLyricEntry(int index) {
        return lyricEntrySparseArray.valueAt(index);
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
