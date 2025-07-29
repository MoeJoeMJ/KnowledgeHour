package com.moejoe.knowledgehour.presentation.modules

import com.moejoe.knowledgehour.R
import com.moejoe.knowledgehour.utils.AppUtil

/**
 * Created by manoj-20477 on 23/01/25.
 */
enum class Modules(val moduleName: String, val moduleLogo: Int) {
    OTPModule(moduleName = AppUtil.getString(R.string.module_name_otp), moduleLogo = R.drawable.ic_otp),
    SharedElementTransition(moduleName = AppUtil.getString(R.string.module_name_shared_element_transition), moduleLogo = R.drawable.ic_shared_element_transition),
    FaceDetection(moduleName = AppUtil.getString(R.string.module_name_face_detection), moduleLogo = R.drawable.ic_face_detection),
    FabAnimation(moduleName = AppUtil.getString(R.string.module_name_fab_animation), moduleLogo = R.drawable.ic_fab_animation)
}