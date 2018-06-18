package tech.summerly.quiet.local.model

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.model.IAlbum
import tech.summerly.quiet.commonlib.model.IArtist
import tech.summerly.quiet.commonlib.model.IMusic

class LocalMusic(
        override val id: Long
) : IMusic, Parcelable {
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

    constructor(source: Parcel) : this(
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocalMusic> = object : Parcelable.Creator<LocalMusic> {
            override fun createFromParcel(source: Parcel): LocalMusic = LocalMusic(source)
            override fun newArray(size: Int): Array<LocalMusic?> = arrayOfNulls(size)
        }
    }
}