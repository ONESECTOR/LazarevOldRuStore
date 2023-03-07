package com.owlylabs.platform.model.repository.local.db.subs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_state.SubStateCallback
import com.owlylabs.platform.util.SubscriptionsUtil
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
abstract class SubscriptionDao {

    @Query("SELECT * FROM SubscriptionEntity")
    abstract fun getAllSubsBlocking(): List<SubscriptionEntity>

    @Query("SELECT * FROM SubscriptionEntity")
    abstract fun getAllSubsObservable(): Observable<List<SubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveSubscription(data: SubscriptionEntity): Completable

    fun hasAnyActiveSubscription(): Observable<Boolean> {
        return getAllSubsObservable()
            .map {
                it.forEach {
                    if (it.expiryTimeMillis.toLong() > System.currentTimeMillis()) {
                        return@map true
                    }
                }
                false
            }
    }
}