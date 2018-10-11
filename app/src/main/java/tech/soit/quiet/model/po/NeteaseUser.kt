package tech.soit.quiet.model.po

import tech.soit.quiet.model.vo.User

data class NeteaseUser(
        private val id: Long,
        private val name: String
) : User() {

    override fun getId(): Long {
        return id
    }

    override fun getName(): String {
        return name
    }

}