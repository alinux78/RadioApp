#!/bin/bash


LD_PRELOAD='/usr/lib64/libstdc++.so.6' $ANDROID_SDK/tools/emulator -netdelay none -netspeed full -avd Nexus_5_API_19
