package com.moejoe.knowledgehour

import android.app.Application

/**
 * Created by manoj-20477 on 12/12/24.
 */
class KnowledgeHourApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    companion object {
        private lateinit var mInstance: KnowledgeHourApplication

        fun getInstance(): KnowledgeHourApplication {
            return mInstance
        }
    }
}