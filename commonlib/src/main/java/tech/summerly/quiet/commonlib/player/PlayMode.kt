package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.R


enum class PlayMode {
    //随机播放
    Shuffle,
    //单曲循环
    Single,
    //列表循环
    Sequence;

    companion object {

        /**
         * safely convert enum name to instance
         */
        operator fun invoke(name: String) = when (name) {
            Shuffle.name -> Shuffle
            Single.name -> Single
            Sequence.name -> Sequence
            else -> Sequence
        }

    }

    fun next(): PlayMode = when (this) {
        Single -> Shuffle
        Shuffle -> Sequence
        Sequence -> Single
    }

    fun drawableRes(): Int = when (this) {
        Single -> R.drawable.ic_repeat_one_black_24dp
        Shuffle -> R.drawable.ic_shuffle_black_24dp
        Sequence -> R.drawable.ic_repeat_black_24dp
    }
}