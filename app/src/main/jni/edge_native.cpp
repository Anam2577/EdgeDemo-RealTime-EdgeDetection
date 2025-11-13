#include <jni.h>
#include <opencv2/imgproc.hpp>
#include <opencv2/core.hpp>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "edge_native", __VA_ARGS__)

using namespace cv;

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_edgedemo_MainActivity_nativeProcessFrame(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray nv21Array,
        jint width,
        jint height,
        jint lowThresh,
        jint highThresh) {

    jbyte *nv21 = env->GetByteArrayElements(nv21Array, NULL);
    Mat yuv(height + height/2, width, CV_8UC1, (unsigned char *)nv21);
    Mat bgr;
    cvtColor(yuv, bgr, COLOR_YUV2BGR_NV21);

    Mat gray;
    cvtColor(bgr, gray, COLOR_BGR2GRAY);
    Mat edges;
    Canny(gray, edges, lowThresh, highThresh);

    Mat rgba;
    cvtColor(edges, rgba, COLOR_GRAY2RGBA);

    int outSize = rgba.total() * rgba.elemSize();
    jbyteArray out = env->NewByteArray(outSize);
    env->SetByteArrayRegion(out, 0, outSize, (jbyte*)rgba.data);

    env->ReleaseByteArrayElements(nv21Array, nv21, 0);
    return out;
}
