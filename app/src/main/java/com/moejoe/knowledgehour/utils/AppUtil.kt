package com.moejoe.knowledgehour.utils

import com.moejoe.knowledgehour.KnowledgeHourApplication

/**
 * Created by manoj-20477 on 23/01/25.
 */
object AppUtil {
    val applicationInstance = KnowledgeHourApplication.getInstance()

    fun getString(stringId: Int): String {
        return applicationInstance.getString(stringId)
    }
}