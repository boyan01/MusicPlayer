package tech.summerly.quiet.local.model

import tech.summerly.quiet.commonlib.model.IAlbum
import tech.summerly.quiet.commonlib.model.IArtist
import tech.summerly.quiet.commonlib.model.IMusic

class LocalMusic(
        override val id: Long
) : IMusic {

    override val title: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val artist: List<IArtist>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val album: IAlbum
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val duration: Long
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getUrl(bitrate: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isFavorite: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val artwork: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun delete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun like() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}