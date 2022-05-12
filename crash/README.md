## crash quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/crash/README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Environment Requirements](#environment-requirements)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-code)
 * [Result](#result)
 * [Technical Support](#technical-support)
 * [License](#license)

## Introduction
The AppGallery Connect Crash service provides a powerful yet lightweight solution to app crash problems. With the service, you can quickly detect, locate, and resolve app crashes (unexpected exits of apps), and have access to highly readable crash reports in real time, without the need to write any code.

## Environment Requirements
* A computer with Android Studio installed for app development
* A device or an emulator in Android Studio running Android 4.2 or a later version 

## Getting Started
Before running the crash quickstart app, you need to:
1. Check whether you have a HUAWEI ID. If not, [register one](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set **Package type** to **APK (Android app)**.
3. Click **My projects**, select your project and app, go to **Quality** > **Crash**, and enable the Crash service. (The Crash service integrates HUAWEI Analysis Kit for crash event reporting. As a result, you need to enable HUAWEI Analysis Kit before integrating the Crash SDK.)
4. Go to **Project settings** > **General information**, download the **agconnect-services.json** file from AppGallery Connect, and copy the **agconnect-services.json** file to the app's module directory.

## Sample Code
The Crash SDK supports crash simulation, exception simulation, crash collection switch, and some other functions.

Sample code: src\main\java\com\huawei\agc\quickstart\crash\MainActivity.java

## Result
**Click makeCrash.**</br>
<img src="images/crash.gif" alt="resultpage" height="600"/>

## Technical Support
If you have any questions about the sample code, try the following:  
- Visit [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect), submit your questions, and tag them with `AppGallery`. Huawei experts will answer your questions.  
- Visit the AppGallery section in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/AppGalleryConnect/agc-demos/issues) or submit a [pull request](https://github.com/AppGalleryConnect/agc-demos/pulls).

## License
The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
