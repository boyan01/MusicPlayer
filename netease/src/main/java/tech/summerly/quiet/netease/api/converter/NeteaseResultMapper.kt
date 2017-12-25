package tech.summerly.quiet.netease.api.converter

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/26
 * desc   : 将 [CloudMusicService] 返回的 ResultBean 转换为应用中通用的实体类
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
internal class NeteaseResultMapper {

//    fun convertToPlaylist(resultBean: PlaylistDetailResultBean.Playlist): Playlist {
//        return Playlist(
//                id = resultBean.id,
//                name = resultBean.name,
//                coverImageUrl = resultBean.coverImgUrl,
//                musics = resultBean.tracks?.map(this::convertToMusic) ?: emptyList(),
//                type = MusicType.NETEASE
//        )
//    }
//
//    fun convertToPlaylist(resultBean: PlaylistResultBean.PlaylistBean): Playlist {
//        return Playlist(
//                id = resultBean.id,
//                name = resultBean.name,
//                coverImageUrl = resultBean.coverImgUrl,
//                musics = emptyList(),
//                type = MusicType.NETEASE
//        )
//    }
//
//    fun convertToPlaylist(recommend: RecommendPlaylistResultBean.Recommend): Playlist {
//        return Playlist(
//                id = recommend.id,
//                name = recommend.name,
//                coverImageUrl = recommend.picUrl,
//                musics = emptyList(),
//                type = MusicType.NETEASE
//        )
//    }
//
//    fun convertToMusic(resultBean: PlaylistDetailResultBean.Track): Music {
//        return Music(
//                id = resultBean.id,
//                title = resultBean.name ?: string(R.string.music_info_unknown),
//                url = null, //目前无法也无需初始化其 URI ,我们只需要知道歌曲的ID就行了.
//                artist = resultBean.ar?.map(this::convertToArtist) ?: emptyList(),
//                picUrl = null,
//                album = convertToAlbum(resultBean.al),
//                type = MusicType.NETEASE,
//                mvId = resultBean.mv,
//                duration = resultBean.dt
//        )
//    }
//
//    fun convertToMusic(resultBean: RecommendSongResultBean.Recommend): Music {
//        return Music(
//                id = resultBean.id,
//                title = resultBean.name,
//                url = null,
//                artist = resultBean.artists?.map(this::convertToArtist) ?: emptyList(),
//                picUrl = resultBean.album.picUrl,
//                album = convertToAlbum(resultBean.album),
//                type = MusicType.NETEASE,
//                mvId = resultBean.mvid ?: 0L,
//                duration = resultBean.duration
//        )
//    }
//
//    fun convertToMusic(resultBean: PersonalFmDataResult.Datum): Music {
//        return Music(
//                id = resultBean.id,
//                title = resultBean.name,
//                url = null,
//                artist = resultBean.artists?.map {
//                    Artist(
//                            id = it.id,
//                            name = it.name,
//                            picUrl = it.picUrl,
//                            type = MusicType.NETEASE
//                    )
//                } ?: emptyList(),
//                picUrl = resultBean.album?.picUrl,
//                album = resultBean.album.let {
//                    Album(
//                            id = it?.id ?: 0L,
//                            name = it?.name ?: "unknown",
//                            picUrl = it?.picUrl,
//                            type = MusicType.NETEASE
//                    )
//                },
//                type = MusicType.NETEASE_FM,
//                mvId = resultBean.mvid ?: 0L,
//                duration = resultBean.duration,
//                isFavorite = resultBean.starred ?: false
//        )
//    }
//
//    fun convertToMusic(songsBean: MusicSearchResultBean.SongsBean): Music {
//        return Music(
//                id = songsBean.id,
//                title = songsBean.name,
//                url = null,
//                artist = songsBean.artists?.map {
//                    Artist(
//                            id = it.id,
//                            name = it.name,
//                            picUrl = null,
//                            type = MusicType.NETEASE
//                    )
//                } ?: emptyList(),
//                picUrl = null,
//                album = songsBean.album.let {
//                    Album(
//                            id = it?.id ?: 0L,
//                            name = it?.name ?: "unknown",
//                            picUrl = null,
//                            type = MusicType.NETEASE
//                    )
//                },
//                type = MusicType.NETEASE,
//                mvId = songsBean.mvid,
//                duration = songsBean.duration.toInt()
//        )
//    }
//
//    fun convertToArtist(artistResult: RecommendSongResultBean.Artist) =
//            Artist(
//                    id = artistResult.id,
//                    name = artistResult.name,
//                    picUrl = artistResult.picUrl,
//                    type = MusicType.NETEASE
//            )
//
//
//    fun convertToArtist(artistResult: PlaylistDetailResultBean.Artist) =
//            Artist(
//                    id = artistResult.id,
//                    name = artistResult.name ?: string(R.string.music_info_unknown),
//                    picUrl = null,
//                    type = MusicType.NETEASE
//            )
//
//    fun convertToAlbum(albumResult: PlaylistDetailResultBean.Album?) =
//            Album(
//                    id = albumResult?.id ?: 0,
//                    name = albumResult?.name ?: "unknown",
//                    picUrl = albumResult?.picUrl,
//                    type = MusicType.NETEASE
//            )
//
//    private fun convertToAlbum(albumResult: RecommendSongResultBean.Album): Album {
//        return Album(
//                id = albumResult.id,
//                name = albumResult.name,
//                picUrl = albumResult.picUrl,
//                type = MusicType.NETEASE
//        )
//    }
//
//    fun convertToMv(data: MvDetailResultBean.Data): Mv {
//        return Mv(
//                id = data.id,
//                urlList = ArrayList<Pair<String, String>>(4).apply {
//                    data.brs?._240?.let {
//                        add("240" to it)
//                    }
//                    data.brs?._480?.let {
//                        add("480" to it)
//                    }
//                    data.brs?._720?.let {
//                        add("720" to it)
//                    }
//                    data.brs?._1080?.let {
//                        add("1080" to it)
//                    }
//                },
//                duration = data.duration,
//                title = data.name + " - " + data.artistName
//        )
//    }

}
