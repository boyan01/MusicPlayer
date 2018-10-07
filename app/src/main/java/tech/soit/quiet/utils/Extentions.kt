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
fun List<Artist>.getString(): String = joinToString(ARTIST_SEPARATOR) { it.name }

/**
 * music artist and album info
 */
val Music.subTitle: String
    get() = artists.getString() + " - " + album.title


/**
 * if music was marked as Favorite
 */
val Music.isFavorite: Boolean
    get() = attach.containsKey("isFavorite")