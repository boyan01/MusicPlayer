package tech.soit.quiet.ui.adapter

import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.ui.adapter.viewholder.MusicListHeaderViewHolder

class LatestPlayListAdapter : MusicListAdapter2() {

    companion object {

        private const val TOKEN = "latest-play-list"

    }


    override val token: String get() = TOKEN

    override val musicIndexOffset: Int get() = 1

    override val trackCount: Int get() = musics.size

    override fun getItemCount(): Int {
        return if (isPreviewMode) 2 else musics.size + 1
    }

    fun showLatestPlayList(musics: List<Music>?) {
        if (musics.isNullOrEmpty()) {
            preview()
        } else {
            showList(musics)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            isPreviewMode -> TYPE_HEADER
            else -> TYPE_MUSIC
        }
    }

    override fun bindHeader(holder: MusicListHeaderViewHolder) {
        holder.setHeader(trackCount, false, false)
    }

}