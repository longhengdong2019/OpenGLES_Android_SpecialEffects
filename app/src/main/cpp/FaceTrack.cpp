//
// Created by Leroy on 2022/9/2.
//

#include "FaceTrack.h"
#include "face_alignment.h"

FaceTrack::FaceTrack(const char *path, const char *seeta) {
    // 检测器
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(path));

    // 跟踪器
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(path));

    DetectionBasedTracker::Parameters DetectorParams;
    // 含有两个对象 检测器 跟踪器
    tracker = makePtr<DetectionBasedTracker>(mainDetector, trackingDetector, DetectorParams);
    faceAlignment = makePtr<seeta::FaceAlignment>(seeta);
}

void FaceTrack::startTracking() {
    tracker->run();
}

vector<Rect2f> FaceTrack::detector(Mat mat) {
    vector<Rect> faces;
    vector<Rect2f> rects;

    // 开始检测
    tracker->process(mat);

    // 获取结果
    tracker->getObjects(faces);

    // 人眼识别的 5 个特征点，入参、出参对象
    seeta::FacialLandmark points[5];

    //     0         1
    //          2
    //     3         4
    // 左眼 0  右眼 1  鼻子 2

    if (faces.size()) {
        // LOGE("检测到人脸");
        // 遍历多个人脸，对每一个人眼进行定位 放大
        Rect face = faces[0];
        // 人脸的区域
        rects.push_back(Rect2f(face.x, face.y, face.width, face.height));

        seeta::ImageData image_data(mat.cols, mat.rows);
        image_data.data = mat.data;
        // 待检测的区域
        seeta::FaceInfo faceInfo;
        // 边界
        seeta::Rect bbox;
        bbox.x = face.x;
        bbox.y = face.y;
        bbox.width = face.width;
        bbox.height = face.height;
        faceInfo.bbox = bbox;
        faceAlignment->PointDetectLandmarks(image_data, faceInfo, points);

        for (int i = 0; i < 5; ++i) {
            rects.push_back(Rect2f(points[i].x, points[i].y, 0, 0));
            if (i == 0) {
                // LOGE("------------------------------ 左眼坐标：x:%f, y:%f", rects[0].x, rects[0].y);
            }
            if (i == 1) {
                // LOGE("------------------------------ 右眼坐标：x:%f, y:%f", rects[1].x, rects[1].y);
            }
        }
    }

    // LOGE("------------------------------ 人脸数量：%d", faces.size());
    return rects;
}
