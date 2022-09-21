//
// Created by Leroy on 2022/9/2.
//

#ifndef OPENCVGL_Log_H
#define OPENCVGL_Log_H

#include <android/log.h>

#define TAG "Leroy"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#endif //OPENCVGL_Log_H
