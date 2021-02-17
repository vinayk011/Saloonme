/*
 *  Created by Vinay.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme

import android.util.Base64
import android.util.Patterns
import com.google.gson.GsonBuilder
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.salonme.base.DB_APP_ID
import io.paperdb.Paper
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    .create()

fun String.fixString(): String {
    return if (this.isBlank()) {
        ""
    } else this.trim { it <= ' ' }
}
operator fun String.div(value:String):String{
    val splitted = value.split("/")
    return findCommonChars(splitted[0], splitted[1])

}
fun findCommonChars(a: String, b: String): String {
    val resultBuilder = StringBuilder()
    val charsMap: MutableSet<Char> = HashSet()
    for (element in a) {
        if (b.indexOf(element) != -1) {
            charsMap.add(Character.valueOf(element))
        }
    }
    val charsIterator: Iterator<Char> = charsMap.iterator()
    while (charsIterator.hasNext()) {
        resultBuilder.append(charsIterator.next())
    }
    return resultBuilder.toString()
}

fun String.isMobileNumber(): Boolean {
    try {
        val mPhoneUtil = PhoneNumberUtil.getInstance()
        val mNumber = mPhoneUtil.parse(this, "")
        return mPhoneUtil.isValidNumber(mNumber)
    } catch (ignored: Exception) {
    }
    return false
}
fun String.getNationalMobileNumber(): String {
    try {
        val mPhoneUtil = PhoneNumberUtil.getInstance()
        val mNumber = mPhoneUtil.parse(this, "")
        return mNumber.nationalNumber.toString()
    } catch (ignored: Exception) {
    }
    return "";
}
fun String.isEmailAddress(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
fun String.isDate():Boolean{
    return true
}
fun String.ellipsize(length: Int): String {
    if(this.length > length){
        return """${this.substring(0, length)}..."""
    }
    return this
}
fun encrypt(data: String): String {
    try {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(keyValueStr.toByteArray(), "AES")
        val ivParameterSpec = IvParameterSpec(ivValue.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        return Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)
            .replace("\n", "").replace("\r", "")
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: NoSuchPaddingException) {
        e.printStackTrace()
    } catch (e: IllegalBlockSizeException) {
        e.printStackTrace()
    } catch (e: BadPaddingException) {
        e.printStackTrace()
    } catch (e: InvalidAlgorithmParameterException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    }
    return data
}

fun decrypt(encrypted: String): String {
    try {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(keyValueStr.toByteArray(), "AES")
        val ivParameterSpec = IvParameterSpec(ivValue.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        return String(cipher.doFinal(Base64.decode(encrypted.toByteArray(), Base64.DEFAULT)))
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: NoSuchPaddingException) {
        e.printStackTrace()
    } catch (e: IllegalBlockSizeException) {
        e.printStackTrace()
    } catch (e: BadPaddingException) {
        e.printStackTrace()
    } catch (e: InvalidAlgorithmParameterException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    }
    return encrypted
}

private const val keyValueStr = "rb!nBwXv4C%Gr^84"

private const val ivValue = "1234567812345678"

fun getAppId(): String {
    var appId = Paper.book().read<String>(DB_APP_ID)
    if (appId.isNullOrBlank()) {
        appId = UUID.randomUUID().toString()
        Paper.book().write(DB_APP_ID, appId)
    }
    return appId
}