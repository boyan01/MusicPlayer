package tech.soit.quiet.model.vo

abstract class User {

    abstract fun getId(): Long

    abstract fun getNickName(): String

    abstract fun getAvatarUrl(): Any

}