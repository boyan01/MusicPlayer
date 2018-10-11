package tech.soit.quiet.model.vo

abstract class User {

    companion object {

        /**
         * the empty user
         */
        val EMPTY = object : User() {
            override fun getName(): String {
                return ""
            }

            override fun getId(): Long {
                return 0L
            }
        }

    }

    abstract fun getName(): String

    abstract fun getId(): Long

}