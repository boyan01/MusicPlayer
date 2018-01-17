package tech.summerly.quiet.commonlib.player.state


enum class PlayMode {
    //随机播放
    Shuffle,
    //单曲循环
    Single,
    //列表循环
    Sequence;

    companion object {
        fun next(current: PlayMode): PlayMode {
            return when (current) {
                Single -> Shuffle
                Shuffle -> Sequence
                Sequence -> Single
            }
        }


        @Deprecated("", ReplaceWith("PlayMode(name)", "tech.summerly.quiet.commonlib.player.state.PlayMode.Companion.invoke"))
        fun fromName(name: String) = invoke(name)

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
}