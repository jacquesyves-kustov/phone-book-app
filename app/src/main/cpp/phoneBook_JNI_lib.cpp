#include <jni.h>
#include "PhoneBook.h"


// Create an instance with static function
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_phonebookapp_MainActivity_createBook(
        JNIEnv* env,
        jobject) {
    return reinterpret_cast<jlong>(PhoneBook::getInstance());
}


// Methods
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_phonebookapp_MainActivity_sendAllContacts(
        JNIEnv *env,
        jobject /* this */,
        jlong ptBook) {
    PhoneBook* bookInstance = reinterpret_cast<PhoneBook *>(ptBook);

    return env->NewStringUTF(bookInstance->sendContactsList().c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_phonebookapp_MainActivity_sendContactByName(JNIEnv *env, jobject thiz, jlong ptBook,
                                                         jstring request) {
    PhoneBook* bookInstance = reinterpret_cast<PhoneBook *>(ptBook);

    const char *cstr = env->GetStringUTFChars(request, NULL);
    std::string str = std::string(cstr);

    return env->NewStringUTF(bookInstance->findContactByName(str).c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_phonebookapp_MainActivity_addNewContact(JNIEnv *env, jobject thiz, jlong ptBook,
                                                     jstring request) {
    PhoneBook* bookInstance = reinterpret_cast<PhoneBook *>(ptBook);

    const char *cstr = env->GetStringUTFChars(request, NULL);
    std::string str = std::string(cstr);

    bookInstance->addContact(str);

    return;
}
