package tech.summerly.quiet.commonlib.view.lyric;

/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/4/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */

class LyricParseException extends Exception {

    LyricParseException(String message) {
        super(message);
    }

    LyricParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
