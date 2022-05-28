# Cloud DB Quick Start

English | [中文](https://gitee.com/appgallery_connect/agc-android-demos/blob/master/clouddb-kotlin/README_zh.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Getting Started](#getting-started)
 * [Result](#result)
 * [License](#license)
 
## Introduction

This project is a quick start sample developed using Cloud DB APIs.

## Getting Started

1. On the [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/myApp) page, create a project, and add an app with a package named **com.huawei.agc.clouddb.xxxx**.

2. Go to **Auth Service** and enable Auth Service and the anonymous account authentication mode.

3. Go to **Cloud DB** and enable Cloud DB. Then, perform the following operations:

    3.1 Create an object type by importing the template file **CloudDBQuickStart_1.json** in the root directory of the project. Alternatively, create an object type named **BookInfo** and ensure that all fields must be the same as those in **BookInfo.java** in the project.

    3.2 Click the **Cloud DB Zones** tab, and click **Add** to create a Cloud DB zone named **QuickStartDemo**.

4. Go to **Project settings** > **General information**, download the **agconnect-services.json** file, and copy this file to the **app** directory.

5. Use Android Studio to open the project.

6. Run the sample on your Android device.

## Result

<img src="./screenshot_en.jpg" height="550" width="320" />

## License

The sample code is licensed under the [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
