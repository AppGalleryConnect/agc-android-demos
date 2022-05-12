## appmessaging quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/appmessaging/README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Environment Requirements](#environment-requirements)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-code)
 * [Result](#result)
 * [Technical Support](#technical-support)
 * [License](#license)

## Introduction
You can use App Messaging to send relevant messages to users actively using your app to encourage them to use key app functions.

## Environment Requirements
* A computer with Android Studio installed for app development
* A device or an emulator in Android Studio running Android 4.2 or a later version 

## Getting Started
   1. Check whether you have a HUAWEI ID. If not, [register one](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
   2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set **Package type** to **APK (Android app)**.
   3. Enable [App Messaging](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-appmessage-introduction-0000001071884501).
   4. Go to **Project settings** > **General information**, download the **agconnect-services.json** file, and copy this file to the app's module directory of your Android Studio project.

## Sample Code
The SDK supports message debugging.
Sample code: src\main\java\com\huawei\agc\quickstart\appmessaging\AppMessagingActivity.java

## Result
**Sign in to AppGallery Connect and create an AppOnForeground message.**</br>
<img src="images/appmessaging.gif" alt="resultpage" height="600"/>

## Technical Support
If you have any questions about the sample code, try the following:
- Visit [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect), submit your questions, and tag them with `AppGallery`. Huawei experts will answer your questions.  
- Visit the AppGallery section in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/AppGalleryConnect/agc-demos/issues) or submit a [pull request](https://github.com/AppGalleryConnect/agc-demos/pulls).

## License
The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
