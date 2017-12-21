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

package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.state.PlayMode


/**
 * author : yangbin10
 * date   : 2017/11/20
 * attention : playPause() playNext() playPrevious 这三个方法是给 service 用的
 */
interface IMusicPlayer {

    var playMode: PlayMode

    fun currentPlaying(): Music?

    fun isPlaying(): Boolean

    fun playNext()

    fun playPrevious()

    fun playPause()

    fun stop()

    fun play(music: Music)

    fun setPlaylist(musics: List<Music>)

    fun getPlaylist(): List<Music>

    fun remove(music: Music)

    fun addToNext(music: Music)

    fun seekToPosition(position: Long)

    fun currentPosition(): Long

    fun destroy()

    fun restore()

    fun save()
}