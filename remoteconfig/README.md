# remoteconfig quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/remoteconfig/README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Environment Requirements](#environment-requirements)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-code)
 * [Result](#result)
 * [Technical Support](#technical-support)
 * [License](#license)

## Introduction
Remote Configuration allows you to manage parameters online. With the service, you can change the behavior and appearance of your app online without requiring users to update the app. Remote Configuration provides cloud-based services, the console, and the client SDK. By integrating the client SDK, your app can periodically obtain parameter values delivered on the console to modify the app's behavior and appearance.

## Environment Requirements
* A computer with Android Studio installed for app development
* A device or an emulator in Android Studio running Android 4.2 or a later version

## Getting Started
1. Check whether you have a HUAWEI ID. If not, [register one](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set **Package type** to **APK (Android app)**.
3. Enable [Remote Configuration](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-introduction-0000001055149778).
4. Go to **Project settings** > **General information**, download the **agconnect-services.json** file, and copy this file to the app's module directory.

## Sample Code

The fetch API is called before app launch and data is used after the fetch operation is successful.
    src\main\java\com\huawei\agc\quickstart\Test1Activity.java

Data fetched by calling the fetch API is applied upon the next app launch.
    src\main\java\com\huawei\agc\quickstart\Test2Activity.java

## Result
**Click 'FETCH DATA AND APPLY'.</br>
<img src="images/fetch and apply.gif" alt="resultpage" height="600"/>

**Click 'FETCH DATA AND APPLY NEXT STARTUP'.</br>
<img src="images/fetch and apply next start.gif" alt="resultpage" height="600"/>

## Technical Support
If you have any questions about the sample code, try the following:
- Visit [Stack Overflow](https://stackoverflow.com/questions/tagged/appgallery-connect), submit your questions, and tag them with `appgallery`. Huawei experts will answer your questions.  
- Go to **AppGallery** in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/AppGalleryConnect/agc-android-demos/issues) or submit a [pull request](https://github.com/AppGalleryConnect/agc-android-demos/pulls).

## License
The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
