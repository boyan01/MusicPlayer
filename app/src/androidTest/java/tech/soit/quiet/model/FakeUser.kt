package tech.soit.quiet.model

import tech.soit.quiet.model.vo.User

class FakeUser(
        private val id: Long,
        private val nickname: String,
        private val avatarUrl: String
) : User() {

    constructor(i: Int) : this(i.toLong(), "nickname", "")

    override fun getId(): Long {
        return id
    }

    override fun getNickName(): String {
        return nickname
    }

    override fun getAvatarUrl(): Any {
        return avatarUrl
    }
}