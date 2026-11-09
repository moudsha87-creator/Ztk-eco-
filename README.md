# ZKTeco MB2000 Controller v2
Android Studio Kotlin project.
Includes IP 10.5.1.240, Port 4370, CommKey 0; TCP reachability test; manual date picker; 12-hour time picker with AM/PM; Apply Manual Date & Time; Sync Time With Phone.

Important: The two clock buttons currently select/display the requested time but do not yet send a ZKTeco clock-setting command. TCP reachability is not proof of protocol communication. Check In/Out are intentionally disabled pending an authorized employee-authentication integration. Actual device clock changes and attendance writes require the supported ZKTeco SDK/API/protocol and administrator authorization for this terminal/firmware.

Build in Android Studio with JDK 17, Android Gradle Plugin 8.7.3, Android SDK 35.
