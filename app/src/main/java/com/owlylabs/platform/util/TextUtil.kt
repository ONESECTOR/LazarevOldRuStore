package com.owlylabs.platform.util

import android.content.Context
import android.os.Build
import com.android.billingclient.api.SkuDetails
import com.owlylabs.platform.R
import org.threeten.bp.Period
import org.threeten.extra.AmountFormats
import java.text.DecimalFormat
import java.util.*

class TextUtil {
    companion object{
        fun parseStringByComma(str: String): List<String> {
            return if (str.length == 0) ArrayList() else Arrays.asList(*str.split(",").toTypedArray())
        }

        fun getTitleForSubscriptionItemHolder(context: Context, skuDetails: SkuDetails) : String{
            val price = skuDetails.priceAmountMicros / 1_000_000.0
            val currencyCode = skuDetails.priceCurrencyCode
            val subscriptionPeriodThreeTen = Period.parse(skuDetails.subscriptionPeriod)
            val subscriptionPeriodInMonths : String
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val periodJava = java.time.Period.of(subscriptionPeriodThreeTen.years, subscriptionPeriodThreeTen.months, subscriptionPeriodThreeTen.days)
                subscriptionPeriodInMonths = AmountFormats.wordBased(periodJava, Locale.getDefault())
            } else {
                subscriptionPeriodInMonths = context.resources.getQuantityString(
                    R.plurals.plurals_months,
                    subscriptionPeriodThreeTen.toTotalMonths().toInt(),
                    subscriptionPeriodThreeTen.toTotalMonths().toInt())
            }

            return context.getString(
                R.string.subscription_item_title_placeholder_title_and_price,
                skuDetails.title.replace("""(?> \(.+?\))$""".toRegex(), ""),
                getFormattedPrice(price, currencyCode),
                subscriptionPeriodInMonths
            )
        }

        fun getPriceForSubscriptionItemHolder(context: Context, skuDetails: SkuDetails) : String{
            val trialPeriodThreeTen = Period.parse(skuDetails.freeTrialPeriod).days

            return context.getString(R.string.for_free, context.resources.getQuantityString(R.plurals.plurals_first_day, trialPeriodThreeTen, trialPeriodThreeTen))
        }

        /*fun getTitleForSubscriptionItemHolder(context: Context, skuDetails: SkuDetails) : String{
            val trialPeriodDays = Period.parse(skuDetails.freeTrialPeriod).days
            return context.getString(
                R.string.subscription_item_title_placeholder,
                skuDetails.title.replace("""(?> \(.+?\))$""".toRegex(), ""),
                context.resources.getQuantityString(R.plurals.number_of_days, trialPeriodDays, trialPeriodDays))
        }


        fun getPriceForSubscriptionItemHolder(context: Context, skuDetails: SkuDetails) : String{
            val trialPeriodMonths = Period.parse(skuDetails.subscriptionPeriod).toTotalMonths()
            val pricePerMonth = Math.floorDiv(skuDetails.priceAmountMicros, trialPeriodMonths) / 1_000_000.0
            val currencyCode = skuDetails.priceCurrencyCode

            val formattedPricePerMonth = context.getString(
                R.string.subscription_item_price_monthly,
                getFormattedPrice(pricePerMonth, currencyCode)
            )

            var finalText = StringBuilder(formattedPricePerMonth)
            if (trialPeriodMonths > 1){
                val formattedPricePerYear = context.getString(R.string.subscription_item_price_yearly,
                    getFormattedPrice(skuDetails.priceAmountMicros / 1_000_000.0, currencyCode)
                )
                finalText.append(" ")
                finalText.append(formattedPricePerYear)
            }

            return finalText.toString()
        }*/

        private fun getFormattedPrice(price: Double, currencyCode: String): String {
            val format = DecimalFormat.getCurrencyInstance(Locale.getDefault())
            format.setCurrency(Currency.getInstance(currencyCode));
            if (format is DecimalFormat) {
                //format.setDecimalSeparatorAlwaysShown(false)
            }
            format.setMaximumFractionDigits(0)
            format.setMinimumFractionDigits(0)
            return format.format(price)
        }
    }
}