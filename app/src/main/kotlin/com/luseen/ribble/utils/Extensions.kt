package com.luseen.ribble.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.os.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.luseen.logger.Logger

/**
 * Created by Chatikyan on 01.08.2017.
 */


inline fun log(message: () -> Any?) {
    Logger.log(message())
}

fun log(vararg message: () -> Any?) {
    message.forEach {
        Logger.log(it())
    }
}

fun log(message: Any?) {
    Logger.log(message)
}

infix fun ViewGroup.inflate(layoutResId: Int): View =
        LayoutInflater.from(this.context).inflate(layoutResId, this, false)

infix fun Context.takeColor(colorId: Int) = ContextCompat.getColor(this, colorId)

fun ImageView.tint(colorId: Int) {
    this.setColorFilter(this.context.takeColor(colorId), PorterDuff.Mode.SRC_IN)
}

inline fun UiThread(crossinline action: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        action()
    }
}

inline fun delay(milliseconds: Long, crossinline action: () -> Unit) {
    Handler().postDelayed({
        action()
    }, milliseconds)
}

inline fun Activity.start(clazz: () -> Class<*>) {
    this.startActivity(Intent(this, clazz()))
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun emptyString() = ""

fun DrawerLayout.lock() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}

fun DrawerLayout.unlock() {
    this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
}

fun Fragment.whitArgument(key: String, value: Any) {
    val args = Bundle()
    if (value is Parcelable) {
        args.putParcelable(key, value)
    } else {
        throw UnsupportedOperationException("Only parcelable supported")
    }
    arguments = args
}

inline infix fun <reified T> Fragment.getExtra(key: String): T {
    val value: Any = arguments[key]
    return value as T
}

inline fun FragmentManager.inTransaction(transaction: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = this.beginTransaction()
    fragmentTransaction.transaction()
    fragmentTransaction.commit()
}

@SuppressLint("InlinedApi")
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun LorAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        body()
    }
}

@TargetApi(Build.VERSION_CODES.N)
inline fun NorAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        body()
    }
}

@SuppressLint("NewApi")
fun String.toHtml(): Spanned {
    NorAbove {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    }
    return Html.fromHtml(this)
}

fun Int.toPx(context: Context): Int {
    val displayMetrics = context.resources.displayMetrics
    return Math.round(this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun View.topMargin(marginInDp: Int) {
    (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin = marginInDp.toPx(this.context)
}

fun View.bottomMargin(marginInDp: Int) {
    (this.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = marginInDp.toPx(this.context)
}

fun Activity.isPortrait() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

fun Fragment.isPortrait() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

