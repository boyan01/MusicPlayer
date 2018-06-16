package tech.summerly.quiet.local.repository.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import tech.summerly.quiet.local.repository.database.LocalMusicDatabase
import tech.summerly.quiet.local.repository.entity.ArtistEntity
import tech.summerly.quiet.local.repository.entity.MusicArtistRelation
import tech.summerly.quiet.local.repository.entity.MusicEntity

@Dao
internal abstract class ArtistDao(
        val database: LocalMusicDatabase
) {


    @Query("select * from entity_artist")
    abstract fun allArtist(): LiveData<List<ArtistEntity>>

    @Query("select * from entity_artist where id = :id")
    abstract fun artist(id: Long): ArtistEntity

    @Query("select entity_music.* from relation_music_artist " +
            "left join entity_music on entity_music.id = relation_music_artist.music_id " +
            "left join entity_artist on entity_artist.id = relation_music_artist.artist_id " +
            "where entity_artist.id = :artistId")
    abstract fun musics(artistId: Long): List<MusicEntity>

    @Query(value = "select entity_artist.* from relation_music_artist " +
            "left join entity_music on entity_music.id = relation_music_artist.music_id " +
            "left join entity_artist on entity_artist.id = relation_music_artist.artist_id " +
            "where entity_music.id = :musicId")
    abstract fun artists(musicId: Long): List<ArtistEntity>

    @Query("select * from relation_music_artist where music_id = :musicId")
    abstract fun relation(musicId: Long): List<MusicArtistRelation>

    @Delete
    abstract fun removeArtist(artistEntity: ArtistEntity): Int

    @Delete
    abstract fun removeRelation(relation: MusicArtistRelation): Int


    @Transaction
    open fun removeMusic(musicEntity: MusicEntity) {
        val relations = relation(musicEntity.id)
        relations.forEach {
            removeRelation(it)
        }
    }
}