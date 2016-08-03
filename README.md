# Matter Overdrive
![alt tag](https://raw.githubusercontent.com/simeonradivoev/MatterOverdrive/master/MatterOverdriveLogo.png)

<h2 align="center">
<a href='http://simeonradivoev.com/Mods/MatterOverdrive/'>Website</a> | 
<a href='http://simeonradivoev.com/Mods/MatterOverdrive/category/gettingstarted/'>Getting Started</a> |
<a href='http://simeonradivoev.com/Mods/MatterOverdrive/category/faq/'>FAQ</a> | 
<a href='http://simeonradivoev.com/Mods/MatterOverdrive/download_category/downloads/'>Downloads</a> | 
<a href='http://simeonradivoev.com/Mods/MatterOverdrive/builds/'>Dev Builds</a>
</h2>

[![Build Status](https://travis-ci.org/simeonradivoev/MatterOverdrive.svg?branch=1.8.9)](https://travis-ci.org/simeonradivoev/MatterOverdrive)

## Table of Contents
* [About](#about)
* [Features](#features)
* [Contacts](#contacts)
* [Isues](#issues)
* [Building](#building)
* [IMC](#imc)
* [Donations](#donations)

## About
Matter Overdrive is a Minecraft mod inspired by the popular Sci-fi TV series Star Trek. It dwells in the concept of replicating and transforming one type matter into another.
Although it may seem overpowered, Matter Overdrive takes a more realistic approach and requires the player to build a complex system before even the simplest replication can be achieved.

## Contacts
* [Simeon Radivoev](simeonradivoev@gmail.com)
* IRC: **#matteroverdrive** at **irc.esper.net**

## Features
* [Matter Scanner](http://simeonradivoev.com/Mods/MatterOverdrive/items/matter_scanner/), for scanning matter patterns for replication.
* [Replicator](http://simeonradivoev.com/Mods/MatterOverdrive/items/replicator/), for transforming materials.
* [Decomposer](http://simeonradivoev.com/Mods/MatterOverdrive/items/decomposer/), for braking down materials to basic form.
* [Transporter](http://simeonradivoev.com/Mods/MatterOverdrive/items/transporter/), for beaming up.
* [Phaser](http://simeonradivoev.com/Mods/MatterOverdrive/items/phaser/), to set on stun.
* [Fusion Reactors](http://simeonradivoev.com/Mods/MatterOverdrive/fusion-reactor/) and [Gravitational Anomaly](http://simeonradivoev.com/Mods/MatterOverdrive/items/gravitational_anomaly/)
* Complex Networking for replication control.
* Star Maps, with Galaxies, Stars and Planets
* [Androids](http://simeonradivoev.com/Mods/MatterOverdrive/android-guide/), become an Android and learn powerful RPG like abilities, such as Teleportation and Forefield Shields.


![Matter Overdrive Blocks and Items](http://simeonradivoev.com/Mods/MatterOverdrive/wp-content/uploads/2015/05/main_screenshot.png)

## Issues
If you have any crashes, problems or suggestions just open a [new Issue](https://github.com/simeonradivoev/MatterOverdrive/issues/new).
If your crash or problem was fixed, but is not yet released as a public download you can always download the latest [Dev Build](http://simeonradivoev.com/Mods/MatterOverdrive/builds/).

## Building
1. Clone this repository via 
  - SSH `git clone git@github.com:simeonradivoev/MatterOverdrive.git` or 
  - HTTPS `git clone https://github.com/simeonradivoev/MatterOverdrive.git`
2. Setup workspace 
  - Decompiled source `gradlew setupDecompWorkspace`
  - Obfuscated source `gradlew setupDevWorkspace`
  - CI server `gradlew setupCIWorkspace`
3. Build `gradlew build`. Jar will be in `build/libs`
4. For core developer: Setup IDE
  - IntelliJ: Import into IDE and execute `gradlew genIntellijRuns` afterwards
  - Eclipse: execute `gradlew eclipse`
  
## IMC
See the example on [IMC](https://github.com/simeonradivoev/MatterOverdrive/blob/master/src/main/java/matteroverdrive/api/IMC.java) or you can see the [IMC handler](https://github.com/simeonradivoev/MatterOverdrive/blob/master/src/main/java/matteroverdrive/imc/MOIMCHandler.java).

## Donations
Donations go a long way to helping me continue working on Matter Overdrive, making exiting new features.

[![Donate](http://simeonradivoev.com/Mods/MatterOverdrive/wp-content/uploads/2015/06/paypal1.png)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=KGKJSVXZQXWXS)
