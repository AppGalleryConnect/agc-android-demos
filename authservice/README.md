## auth quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/tree/main/Android/agc-authservice-demo-java/blob/master/README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Environment Requirements](#environment-requirements)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Technical Support](#technical-support)
 * [License](#license)

## Introduction
Auth Service integrates a client SDK and accesses our cloud service to build a secure and reliable user authentication system for your app.
Auth Service supports multiple authentication modes and is closely integrated with other serverless services, effectively protecting user data by defining simple rules.

## Environment Requirements
* A computer with Android Studio installed for app development
* A device or an emulator in Android Studio running Android 4.2 or a later version
	
## Getting Started
1. Check whether you have a HUAWEI ID. If not, [register one](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set **Package type** to **APK (Android app)**.
3. Enable authentication modes.
3.1 Sign in to AppGallery Connect, click **My projects**, and click a project that you want to enable Auth Service from the project list.
3.2 Go to **Build** > **Auth Service**. If it is the first time that you use Auth Service, click **Enable now** in the upper right corner.
3.3 Click **Enable** for each authentication mode you want to enable.
3.4 Configure information required by each authentication mode by referring to the development guide.
4. Download the **agconnect-services.json** file from AppGallery Connect and copy this file to the app's module directory (for example, **auth/app/**)
Before compiling the APK, make sure that the project includes the **agconnect-services.json** file; otherwise, a compilation error will occur.

## Sample Code
The quickstart app supports the following authentication modes:
1. Anonymous account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\AnonymousActivity.java

2. Email address
Sample code: src\main\java\com\huawei\agc\quickstart\auth\EmailActivity.java

3. Huawei Game account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\HWGameActivity.java

4. HUAWEI ID
Sample code: src\main\java\com\huawei\agc\quickstart\auth\HWIDActivity.java

5. QQ account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\QQActivity.java
If you need to integrate QQ account sign-in, integrate the QQ account access SDK first.

6. Account from your own system
Sample code: src\main\java\com\huawei\agc\quickstart\auth\SelfBuildActivity.java

7. Weibo account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\WeiboActivity.java

8. WeChat account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\WeixinActivity.java

9. Facebook account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\FacebookActivity.java

10. Google account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\GoogleActivity.java

11. Google Play Games account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\PlayGameActivity.java

12. Twitter account
Sample code: src\main\java\com\huawei\agc\quickstart\auth\TwitterActivity.java

13. Mobile number
Sample code: src\main\java\com\huawei\agc\quickstart\auth\PhoneActivity.java


## Result
**Sign in using an anonymous account**</br>
<img src="images/login_anonymous.gif" alt="login_anonymous" height="600"/>

**Update a user's profile picture**</br>
<img src="images/update_name.gif" alt="update_name" height="600"/>

## Technical Support
If you have any questions about the sample code, try the following:
- Visit [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect), submit your questions, and tag them with `AppGallery`. Huawei experts will answer your questions.
- Visit the AppGallery section in the [HUAWEI Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) and communicate with other developers.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/AppGalleryConnect/agc-demos/issues) or submit a [pull request](https://github.com/AppGalleryConnect/agc-demos/pulls).

## License
The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).