package com.owlylabs.platform.model.repository.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.owlylabs.platform.constants.DBConstants
import com.owlylabs.platform.model.repository.local.db.account.AccountDao
import com.owlylabs.platform.model.repository.local.db.account.AccountEntity
import com.owlylabs.platform.model.repository.local.db.audios.AudioDao
import com.owlylabs.platform.model.repository.local.db.audios.AudioEntity
import com.owlylabs.platform.model.repository.local.db.banners.BannerDao
import com.owlylabs.platform.model.repository.local.db.banners.BannerEntity
import com.owlylabs.platform.model.repository.local.db.books.BookDao
import com.owlylabs.platform.model.repository.local.db.books.BookEntity
import com.owlylabs.platform.model.repository.local.db.content_timestamp.ContentTimestampDao
import com.owlylabs.platform.model.repository.local.db.content_timestamp.ContentTimestampEntity
import com.owlylabs.platform.model.repository.local.db.news.NewsDao
import com.owlylabs.platform.model.repository.local.db.news.NewsEntity
import com.owlylabs.platform.model.repository.local.db.recommended.RecommendedBannerEntity
import com.owlylabs.platform.model.repository.local.db.recommended.RecommendedDao
import com.owlylabs.platform.model.repository.local.db.sections.SectionDao
import com.owlylabs.platform.model.repository.local.db.sections.SectionEntity
import com.owlylabs.platform.model.repository.local.db.subs.SubscriptionDao
import com.owlylabs.platform.model.repository.local.db.subs.SubscriptionEntity
import com.owlylabs.platform.model.repository.local.db.table_relations.Section_Item
import com.owlylabs.platform.model.repository.local.db.table_relations.Section_Item_Dao
import com.owlylabs.platform.model.repository.local.db.table_relations.Tab_Section
import com.owlylabs.platform.model.repository.local.db.table_relations.Tab_Section_Dao
import com.owlylabs.platform.model.repository.local.db.tabs.TabDao
import com.owlylabs.platform.model.repository.local.db.tabs.TabEntity
import com.owlylabs.platform.model.repository.local.db.udid.HashDao
import com.owlylabs.platform.model.repository.local.db.udid.HashEntity
import com.owlylabs.platform.model.repository.local.db.udid.UdidDao
import com.owlylabs.platform.model.repository.local.db.udid.UdidEntity
import com.owlylabs.platform.model.repository.local.db.videos.VideoDao
import com.owlylabs.platform.model.repository.local.db.videos.VideoEntity
import com.owlylabs.platform.model.repository.local.db.inapps.ProductEntity
import com.owlylabs.platform.model.repository.local.db.skus_index.SkuIndexEntity

@Database(
    entities = [SectionEntity::class, TabEntity::class, ContentTimestampEntity::class,
        AudioEntity::class, BookEntity::class, VideoEntity::class, NewsEntity::class,
        BannerEntity::class, Tab_Section::class, Section_Item::class, HashEntity::class,
        UdidEntity::class, AccountEntity::class, RecommendedBannerEntity::class,
        SubscriptionEntity::class, ProductEntity::class, SkuIndexEntity::class],
    version = DBConstants.dbVersion,
    exportSchema = false
)
abstract class RoomClientPlatform : RoomDatabase() {
    abstract fun tabs(): TabDao
    abstract fun tab_sections() : Tab_Section_Dao
    abstract fun sections(): SectionDao
    abstract fun section_items(): Section_Item_Dao

    abstract fun contentTimestamp(): ContentTimestampDao

    abstract fun audios(): AudioDao
    abstract fun books(): BookDao
    abstract fun video(): VideoDao
    abstract fun news(): NewsDao
    abstract fun banners(): BannerDao

    abstract fun hash(): HashDao
    abstract fun udid(): UdidDao
    abstract fun account(): AccountDao

    abstract fun recommended(): RecommendedDao
    abstract fun subscriptions(): SubscriptionDao

    // v3
    //abstract fun inApps(): ProductDao
    //abstract fun skuIndex(): SkuIndexDao

    companion object{
        val MIGRATION_1_2: Migration by lazy {
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(DBConstants.MIGRATION_1_2_p1)
                }
            }
        }

        val MIGRATION_2_3: Migration by lazy {
            object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(DBConstants.MIGRATION_2_3_p1)
                    database.execSQL(DBConstants.MIGRATION_2_3_p2)
                }
            }
        }

    }


}