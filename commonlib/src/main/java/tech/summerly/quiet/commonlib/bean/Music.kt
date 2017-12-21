/*
 * Copyright (C) 2017  YangBin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package tech.summerly.quiet.commonlib.bean

import android.os.Parcelable


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/26
 * desc   : 被播放的基本单位. 所有的不管是来自本地,还是来自网络的音乐,都得转换为此对象,
 *          才能被 MusicPlayerService 播放
 */

interface Music : Parcelable {
    val id: Long
    val title: String
    val artist: List<Artist>
    val album: Album
    val picUri: String?
    val type: MusicType
    val mvId: Long
    val duration: Long
    val playUri: List<MusicUri>

    fun toShortString(): String = "$id : $title"
    fun artistAlbumString(): String = "${album.name} - ${artist.joinToString("/")}"
}