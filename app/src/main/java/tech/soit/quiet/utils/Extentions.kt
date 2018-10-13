package tech.soit.quiet.utils

import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music

/**
 * the separate of artists
 */
private const val ARTIST_SEPARATOR = "/"

/**
 * convert a List of Artist to String
 */
fun List<Artist>.getString(): String = joinToString(ARTIST_SEPARATOR) { it.getName() }

/**
 * music artist and album info
 */
val Music.subTitle: String
    get() = getArtists().getString() + " - " + getAlbum().getName()


/**
 * if music was marked as Favorite
 * TODO
 */
val Music.isFavorite: Boolean
    get() = false