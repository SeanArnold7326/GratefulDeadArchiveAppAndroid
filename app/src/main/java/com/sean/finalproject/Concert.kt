package com.sean.finalproject

import android.app.ActivityManager
import android.os.Parcelable
import java.io.Serializable

data class Concert(val identifier: String?, val mediatype: String?, val subject: String?, val description: String?, val date: String?, val source: String?,
                   val venue: String?, val md5s: String?, val files: ArrayList<SongFile>, val d1: String?, val d2: String?, val dir: String?) : Serializable