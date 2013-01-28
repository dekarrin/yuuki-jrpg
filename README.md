yuuki-jrpg
===========

An NDSU project to create a JRPG from scratch.

Yuuki is an attempt to fully create every aspect of a Japanese-style RPG from
the ground up. It was started as a course project for an introductory computer
science class at North Dakota State University in late September of 2012. Once
the assignment was finished, the developers decided to continue working on the
project.


Testing
-------
The fastest way to test Yuuki is to go the project website at
http://dekarrin.github.com/yuuki-jrpg and download one of the binaries.

To build a stable commit manually, you can execute `ant dist-jar` at the
project root. All tags in this project should be suitable for testing in this
manner. If, for some reason, a tag is not testable in this manner, you may have
to attempt to manually build the code and debug any problems you find.

To build the project code into a directory rather than a JAR file, execute `ant
all` at the project's root. The code will be compiled to dist/bin. Execute the
main class yuuki.Engine to run the program. You must include dist/lib/ and
dist/bin/ in your classpath when executing the main class manually.

Attribution
-----------
**Yuuki Asset Credits:**
* bgm/insidia.mp3: Aaron Krogh - CC-BY-3.0 Unported
* bgm/worldmap.mp3: Aaron Krogh - CC-BY-3.0 Unported
* sfx/strike.mp3: thecluegeek - CC-BY-3.0 Unported
* sfx/strike.mp3: Ekokubza123 - CC-0-1.0 Universal
* sfx/strike.mp3: zimbot - CC-BY-3.0 Unported
* sfx/strike.mp3: hazure - CC-BY-3.0 Unported
* images/joshua_tree.jpg: steveberardi - CC-BY-SA-2.0 Generic
* Other sounds used from http://www.freesfx.co.uk

**Artist home pages:**
* Aaron Krogh - http://soundcloud.com/aaron-anderson-11
* Ekokubza123 - http://www.freesound.org/people/Ekokubza123/
* thecluegeek - http://www.freesound.org/people/thecluegeek/
* hazure - http://www.freesound.org/people/hazure/
* zimbot - http://www.freesound.org/people/zimbot/
* steveberardi - http://www.flickr.com/photos/steveberardi/

**Abbreviations:**
* CC - Creative Commons
* BY - Attribution
* SA - ShareAlike
* 0 - CC0; Public Domain Dedication