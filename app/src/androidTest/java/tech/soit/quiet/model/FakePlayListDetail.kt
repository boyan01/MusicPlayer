package tech.soit.quiet.model

import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.model.vo.User

class FakePlayListDetail(
        private val id: Long,
        private val name: String,
        private val coverUrl: String,
        private val creator: User,
        private val tracks: List<Music>,
        private val isSubscribed: Boolean,
        private val playCount: Int
) : PlayListDetail() {

    override fun getId(): Long {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getCoverUrl(): Any {
        return coverUrl
    }

    override fun getCreator(): User {
        return creator
    }

    override fun getTracks(): List<Music> {
        return tracks
    }

    override fun isSubscribed(): Boolean {
        return isSubscribed
    }

    override fun getPlayCount(): Int {
        return playCount
    }
}