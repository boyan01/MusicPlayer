package tech.summerly.quiet.service.netease.converter

import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.*
import tech.summerly.quiet.commonlib.utils.string
import tech.summerly.quiet.service.netease.result.*

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/26
 * desc   : 将  CloudMusicService 返回的 ResultBean 转换为应用中通用的实体类
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
    fun convertToMusic(resultBean: PlaylistDetailResultBean.Track): Music {
        val album = convertToAlbum(resultBean.al)
        return Music(
                id = resultBean.id,
                title = resultBean.name ?: "",
                playUri = mutableListOf(MusicUri.NORMAL_QUALITY), //目前无法也无需初始化其 URI ,我们只需要知道歌曲的ID就行了.
                artist = resultBean.ar?.map(this::convertToArtist) ?: emptyList(),
                picUri = album.picUri,
                album = album,
                type = MusicType.NETEASE,
                mvId = resultBean.mv,
                duration = resultBean.dt.toLong()
        )
    }

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
    fun convertToMusic(resultBean: PersonalFmDataResult.Datum): Music {
        return Music(
                id = resultBean.id,
                title = resultBean.name,
                playUri = ArrayList(),
                artist = resultBean.artists?.map {
                    Artist(
                            id = it.id,
                            name = it.name ?: "",
                            picUri = it.picUrl,
                            type = MusicType.NETEASE
                    )
                } ?: emptyList(),
                picUri = resultBean.album?.picUrl,
                album = resultBean.album.let {
                    Album(
                            id = it?.id ?: 0L,
                            name = it?.name ?: "unknown",
                            picUri = it?.picUrl,
                            type = MusicType.NETEASE
                    )
                },
                type = MusicType.NETEASE_FM,
                mvId = resultBean.mvid ?: 0L,
                duration = resultBean.duration.toLong()
        ).also { it.isFavorite = resultBean.starred ?: false }
    }

    fun convertToPlaylist(playlistResultBean: PlaylistResultBean.PlaylistBean): Playlist = with(playlistResultBean) {
        return Playlist(
                id = id,
                name = name,
                coverImageUri = coverImgUrl,
                musicCount = trackCount.toInt(),
                type = MusicType.NETEASE
        )
    }

    fun convertToMusic(resultBean: RecommendSongResultBean.Recommend): Music {
        val playUri = ArrayList<MusicUri>()
        if (resultBean.hMusic != null) {
            playUri.add(MusicUri.HIGH_QUALITY)
        }
        if (resultBean.lMusic != null || resultBean.bMusic != null) {
            playUri.add(MusicUri.NORMAL_QUALITY)
        }
        return Music(
                id = resultBean.id,
                title = resultBean.name,
                artist = resultBean.artists?.map(this::convertToArtist) ?: emptyList(),
                picUri = resultBean.album.picUrl,
                album = convertToAlbum(resultBean.album),
                type = MusicType.NETEASE,
                mvId = resultBean.mvid ?: 0L,
                duration = resultBean.duration.toLong(),
                playUri = playUri
        )
    }

    fun convertToArtist(artistResult: RecommendSongResultBean.Artist) =
            Artist(
                    id = artistResult.id,
                    name = artistResult.name,
                    picUri = artistResult.picUrl,
                    type = MusicType.NETEASE
            )

    private fun convertToAlbum(albumResult: RecommendSongResultBean.Album): Album {
        return Album(
                id = albumResult.id,
                name = albumResult.name,
                picUri = albumResult.picUrl,
                type = MusicType.NETEASE
        )
    }

    fun convertToMusic(music: MusicDetailResultBean.Song): Music = with(music) {
        val playUri = mutableListOf<MusicUri>()
        if (high != null) {
            playUri.add(MusicUri.HIGH_QUALITY)
        }
        if (medium != null || low != null) {
            playUri.add(MusicUri.NORMAL_QUALITY)
        }
        return@with Music(
                id = id,
                title = name,
                artist = artist?.map { Artist(it.id, it.name, null, MusicType.NETEASE) }
                        ?: emptyList(),
                album = Album(album?.id ?: 0L, album?.name ?: "", album?.picUrl, MusicType.NETEASE),
                picUri = album?.picUrl,
                type = MusicType.NETEASE,
                mvId = mv ?: 0L,
                playUri = playUri,
                duration = 0
        )
    }


    fun convertToMusic(songsBean: MusicSearchResultBean.SongsBean): Music = with(songsBean) {
        //netease search api do not deliver image url
        return Music(
                id = songsBean.id,
                title = songsBean.name,
                picUri = artists?.getOrNull(0)?.img1v1Url,
                artist = songsBean.artists?.map {
                    Artist(
                            id = it.id,
                            name = it.name,
                            picUri = it.img1v1Url,
                            type = MusicType.NETEASE
                    )
                } ?: emptyList(),
                album = songsBean.album.let {
                    Album(
                            id = it?.id ?: 0L,
                            name = it?.name ?: "unknown",
                            picUri = null,
                            type = MusicType.NETEASE
                    )
                },
                playUri = mutableListOf(MusicUri.NORMAL_QUALITY),
                type = MusicType.NETEASE,
                mvId = songsBean.mvid,
                duration = songsBean.duration
        )
    }

    fun convertToMusic(song: RecordResultBean.Record.SongBean): Music = with(song) {
        return@with Music(
                id = id,
                title = name,
                picUri = null,
                artist = ar.map { Artist(it.id, it.name, null, MusicType.NETEASE) },
                album = Album(al.id, al.name, al.pic_str, MusicType.NETEASE),
                playUri = mutableListOf(MusicUri.NORMAL_QUALITY),
                type = MusicType.NETEASE,
                mvId = mv,
                duration = dt

        )
    }


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
    private fun convertToArtist(artistResult: PlaylistDetailResultBean.Artist) =
            Artist(
                    id = artistResult.id,
                    name = artistResult.name ?: string(R.string.artist_unknow),
                    picUri = null,
                    type = MusicType.NETEASE
            )

    private fun convertToAlbum(albumResult: PlaylistDetailResultBean.Album?) =
            Album(
                    id = albumResult?.id ?: 0,
                    name = albumResult?.name ?: "unknown",
                    picUri = albumResult?.picUrl,
                    type = MusicType.NETEASE
            )

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
