package tech.soit.quiet.utils

import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music

/**
 * provider dummy data
 */
object Dummy {


    val MUSICS = listOf(
            Music(0, "test1", Album("album1"), listOf(Artist("artist1"), Artist("artist2"))),
            Music(0, "test2", Album("album2"), listOf(Artist("artist2"))),
            Music(0, "test3", Album("album1"), listOf(Artist("artist1"))),
            Music(0, "test4", Album("album2"), listOf(Artist("artist2"), Artist("artist3"))),
            Music(0, "test5", Album("album1"), listOf(Artist("artist3")))
    )

}