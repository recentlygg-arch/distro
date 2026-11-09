#include <jni.h>
#include <cmath>
#include <random>
#include <string>
#include <vector>

// Пароль хранится в нативном коде
static const char* kPassword = "08012026";

extern "C" JNIEXPORT jboolean JNICALL
Java_com_polina_love_Native_checkPassword(JNIEnv* env, jclass, jstring input) {
    const char* s = env->GetStringUTFChars(input, nullptr);
    bool ok = std::string(s) == kPassword;
    env->ReleaseStringUTFChars(input, s);
    return ok ? JNI_TRUE : JNI_FALSE;
}

// Раскладка надписей "I love you" внутри формы сердца.
// На каждую надпись 6 чисел: x, y, угол, размер текста, задержка (0..1), оттенок (0..1)
extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_polina_love_Native_heartTags(JNIEnv* env, jclass, jfloat cx, jfloat cy,
                                      jfloat scale, jint count, jlong seed) {
    std::mt19937 rng(static_cast<unsigned>(seed));
    std::uniform_real_distribution<float> ux(-1.4f, 1.4f), uy(-1.3f, 1.4f), u01(0.f, 1.f);
    std::vector<float> out;
    out.reserve(count * 6);
    while ((int)out.size() < count * 6) {
        float x = ux(rng), y = uy(rng);
        float a = x * x + y * y - 1.f;
        if (a * a * a - x * x * y * y * y > 0.f) continue; // вне сердца
        out.push_back(cx + x * scale);
        out.push_back(cy - (y - 0.1f) * scale);
        out.push_back((u01(rng) - 0.5f) * 50.f);
        out.push_back(scale * (0.05f + 0.08f * u01(rng) * u01(rng)));
        out.push_back(u01(rng));
        out.push_back(u01(rng));
    }
    jfloatArray arr = env->NewFloatArray((jsize)out.size());
    env->SetFloatArrayRegion(arr, 0, (jsize)out.size(), out.data());
    return arr;
}
