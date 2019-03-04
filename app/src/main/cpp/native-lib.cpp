#include <jni.h>
#include <string>
//重新声明bspatch.c的main
extern "C"{
extern  int p_main(int argc,char * argv[]);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_yfsd_wl_mybsdiff_MainActivity_bsPatch(JNIEnv *env, jobject instance, jstring oldApk_,
                                               jstring patch_, jstring output_) {
    //将java字符串转为c/c++ 的字符串  或者说 转换为utf-8的char指针
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);


    //bspatch oldfile newfile patchfile
    const char *argv[] = {"",oldApk,output,patch};
    p_main(4, (char **)argv);//3.1需要强转   p_main(4,argv)
// 释放只想unicode格式的char指针
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}