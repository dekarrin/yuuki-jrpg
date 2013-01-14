yuuki-right
===========

Yuuki done right. This is a re-working of the original yuuki-jrpg project. Some of the processes weren't going as
smoothly as they should have, and so, yuuki-right was born out of yuuki-jrpg.


Testing
-------
The fastest way to test Yuuki is to go the project website at http://dekarrin.github.com/yuuki-right and
download one of the binaries.

To build a stable commit manually, you can execute `ant dist-jar` at the project root. All tags in this
project should be suitable for testing in this manner. If, for some reason, a tag is not testable in this
manner, you may have to attempt to manually build the code and debug any problems you find.

To build the project code into a directory rather than a JAR file, execute `ant all` at the project's root.
The code will be compiled to dist/bin. Execute the main class yuuki.YuukiEngine to run the program. You must
include dist/lib/ and dist/bin/ in your classpath when executing the main class manually.
