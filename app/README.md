
//setup
// Made on Android Studio Electric Eel
// Compile SDK 33

---------------------------------------------------------------------------------------
// Dependencies Need to be Synced and installed properly
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.github.bumptech.glide:glide:4.12.0'
implementation 'com.google.firebase:firebase-storage:20.0.0'
implementation 'com.google.firebase:firebase-database:20.0.4'
implementation 'com.yuyakaido.android:card-stack-view:2.3.3'
implementation platform('com.google.firebase:firebase-bom:32.0.0')
implementation 'com.google.firebase:firebase-analytics'
implementation 'com.firebaseui:firebase-ui-database:7.2.0'
implementation 'com.firebaseui:firebase-ui-auth:7.2.0'
implementation 'com.facebook.android:facebook-android-sdk:16.0.0'
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-firestore'
implementation 'com.google.android.gms:play-services-auth:20.5.0'
implementation 'com.google.android.material:material:1.9.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'com.facebook.android:facebook-android-sdk:16.0.1'
implementation 'org.osmdroid:osmdroid-android:6.0.0'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.viewpager2:viewpager2:1.0.0'
implementation 'com.squareup.retrofit2:retrofit:2.6.1'
implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
apply plugin: 'com.google.gms.google-services'
---------------------------------------------------------------------------
Firebase Used for Database 
Realtime Database
Firebase Storage
Firebase Authentication
Glide for images

---------------------------------------------------------------------------
DATABASE CONFIGURATIONS:

Project name: wegatyou
Project ID: wegatyou-386819
Project number: 624703053541
Default GCP resource location: eur3 (europe-west)
Web API key: AIzaSyCoddlYFG8NpD6CUEQXfk6ijY1TCRt1QWk

App ID: 1:624703053541:android:7bed9377d20aa28095fe0c
App nickname: wegatyou
Package name: com.app.wegatyou
SHA certificate fingerprints:
60:b5:01:c6:3a:1f:7a:7e:10:91:e2:b0:02:d0:48:a0:f5:99:89:42

https://console.firebase.google.com/project/wegatyou-386819/overview

leedsoffice@boysenbsoftware.uk ADDED AS OWNER

---------------------------------------------------------------------------

Project needs some serious work...

Known Problems:

activity_dish - the options have been changed from up to 3 to 1 this needs
changing in xml.

swipeable card - the user using the app can see their own profile in the card
stack.

swipeable card - uses likeUserId in the realtime database to collect the userIds
of people who are liked. This solution has flaws and needs revising.

Matching - matching system should use the likeUserId Field once its been fixed.

Location - location tracking and google maps was unable to be implemented due 
to unknown errors.

Settings - the settings page needs to be completed with all the functions
currently the user can only log out. 

----------------------------------------------------------------------------

Dev Notes:

This project does not use the best options for handling data or storing data.

To improve this app a proper card implementation, location tracking and
messaging service needs to be introduced.

Most basic functions have been added however and app is linked and ready for 
regular updates.

----------------------------------------------------------------------------