# Minnesota Off Road Cycles (MORC) Trail Conditions Android Application

This repository contains the code for both the Android App and backend cloud functions that power the app. These
are written using kotlin multiplatform in order to share code between the backend and frontend.

## The modules

### Trail-Conditions-App

This is the Android App. Not much else to say. All merges to master are pushed to the internal test portion of the 
play store. If you'd like access to these please email me, and I can get you added. To run the app simply press the 
run button in Android Studio / IntelliJ or run `./gradlew installDebug` 
to install the debug variant or `./gradlew installRelease` to install the release variant, you can have both variants
installed on the same device. You can also install both versions with `./gradlew installAll`. The 
release version will be exactly the same as the play store, just signed with a debug key.

The debug variant has an extra launcher activity that can be used to change some settings of the app. The most helpful 
being the ability to change what url is hit for the api endpoints.

### Trail-Conditions-GCP-Functions

These are cloud functions hosted on GCP.

#### trailAggregator: https://us-central1-mn-trail-functions.cloudfunctions.net/trail-aggregator

This endpoint is the latest in MN trail technology. It combines the [MORC trails](http://www.morcmtb.org/trail/),
[COGGS Trails](https://www.coggs.com/trail-feed-twitter), and the 
[Cuyuna Trails](https://www.cuyunalakesmtb.com/currentconditions) all into a single endpoint for getting 
trail status. If you know of any others you would like included, feel free to open a ticket!

`./gradlew runTrailAggregator` can be used to test this function locally. In order to get statuses from the twitter
api you will need a twitter api account. These values can be passed into the project using gradle properties or
system environment variable. See buildSrc/src/main/kotlin/twitter-api-keys.gradle.kts for the values to set.

### Trail-Conditions-Networking

This is the shared code that is used by both the Android app and the Cloud Functions. It uses Kotlin multiplatform
to target both js and the jvm. If it's going to be used in Trail-Conditions-GCP-Functions and 
Trail-Conditions-App it belongs in Trail-Conditions-Networking.

## Questions, Issues, Ideas?

Join the telegram room https://t.me/morcmtb

or submit a ticket https://gitlab.com/andrew.reitz/mn-trail-conditions-app/-/issues

## License

    Copyright 2020 Andrew Reitz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
