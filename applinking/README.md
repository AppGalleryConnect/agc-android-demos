## AppLinking quickstart

<<<<<<< HEAD
English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/applinking/README_ZH.md)
=======
English | [中文]()
>>>>>>> dfa4723fa944e69ada9f5f1677ee9ed3a9ba5607

## Table of Contents

 * [Introduction](#Introduction)
 * [Environment Requirements](#environment-requirements)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Technical Support](#technical-support)
 * [License](#license)

## Introduction
App Linking allows you to create cross-platform links that can work as defined regardless of whether your app has been installed by a user. When a user taps the link on an Android or iOS device, the user will be redirected to the specified in-app content. If a user taps the link in a browser, the user will be redirected to the same content of the web version.

## Environment Requirements
* A computer with Android Studio installed for app development
* A device or an emulator in Android Studio running Android 4.2 or a later version 

## Getting Started
1. Check whether you have a HUAWEI ID. If not, [register one](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set **Package type** to **APK (Android app)**.
3. Enable [App Linking](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-applinking-introduction-0000001054143215).
4. Go to **Project settings** > **General information**, download the **agconnect-services.json** file, and copy this file to the app's module directory.
5. Create a link prefix in App Linking and replace the value of **DOMAIN_URI_PREFIX** in MainActivity with this prefix.

## Sample Code

Main entry of the app, which processes received links.
Sample code: src\main\java\com\huawei\agc\quickstart\MainActivity.java

Redirection to the target page when a deep link is processed in the app.
Sample code: src\main\java\com\huawei\agc\quickstart\DetailActivity.java

## Result


## Technical Support
If you have any questions about the sample code, try the following:  
- Visit [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect), submit your questions, and tag them with `AppGallery`. Huawei experts will answer your questions.  
- Visit the AppGallery section in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/AppGalleryConnect/agc-demos/issues) or submit a [pull request](https://github.com/AppGalleryConnect/agc-demos/pulls).

## License
The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
