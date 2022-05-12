# APMS Demo

## Table of Contents

 * [Introduction](#introduction)
 * [Environment Requirements](#environment-requirements)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Technical Support](#technical-support)
 * [License](#license)


## Introduction
App Performance Management (APM) helps you improve user experience of your apps by helping you discover and rectify app performance issues.

## Environment Requirements
* A computer with Android Studio installed for app development
* A device or an emulator in Android Studio running Android 4.2 or a later version 

## Getting Started
1. Check whether you have a HUAWEI ID. If not, [register one](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set **Package type** to **APK (Android app)**.
3. Select your project and app in My projects, and go to **Quality** > **APM** to enable the APM service.
4. Go to **Project settings** > **General information**, download the **agconnect-services.json** file, and copy this file to the app's module directory.

## Sample Code
The APM SDK supports collecting performance data automatic and collection switch setting.

Sample code: src\main\java\com\example\quickstart\apmsandroiddemo\MainActivity.java

## Result
**Report APM Data**</br>
<img src="images/apms.gif" alt="resultpage" height="600"/>

## Technical Support
If you have any questions about the sample code, try the following:  
- Visit [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect), submit your questions, and tag them with `huawei-mobile-services`. Huawei experts will answer your questions. 
- Visit the AppGallery section in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/AppGalleryConnect/agc-demos/issues) or submit a [pull request](https://github.com/AppGalleryConnect/agc-demos/pulls).



## License
The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
