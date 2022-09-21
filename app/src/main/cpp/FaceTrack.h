//
// Created by Leroy on 2022/9/2.
//

#ifndef OPENCVGL_FACETRACK_H
#define OPENCVGL_FACETRACK_H

#include <opencv2/opencv.hpp>
#include <opencv2/objdetect.hpp>
#include <android/bitmap.h>
#include <android/log.h>
#include "face_alignment.h"

#define TAG "FaceTrack"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

// 使用命名空间
using namespace cv;
using namespace std;

class CascadeDetectorAdapter: public DetectionBasedTracker::IDetector{ // 实现IDetector接口
public:
    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector) : IDetector(), Detector(detector) {
        // CV_Assert(detector);
    }

    // 检测到人脸，则会回调 Mat - Bitmap
    void detect(const cv::Mat &Image, std::vector<cv::Rect> &objects){
        Detector->detectMultiScale(Image,objects, scaleFactor, minNeighbours,0,minObjSize,maxObjSize);
    }

    virtual ~CascadeDetectorAdapter(){
        LOGE("CascadeDetectorAdapter::Detect::~Detect");
    }

private:
    CascadeDetectorAdapter();
    cv::Ptr<cv::CascadeClassifier> Detector;
};

class FaceTrack {
public:
    FaceTrack(const char *path, const char *seeta);

    void startTracking();

    vector<Rect2f> detector(Mat mat);

private:
    Ptr<DetectionBasedTracker> tracker;
    Ptr<seeta::FaceAlignment> faceAlignment;
};


#endif //OPENCVGL_FACETRACK_H
