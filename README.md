yuuki-right
===========

Yuuki done right. This is a re-working of the origina yuuki-jrpg project. Some of the processes weren't going as
smoothly as they should have, and so, yuuki-right was born out of yuuki-jrpg.


Testing
-------
The fastest way to test Yuuki is to run the distributable JAR file. To generate this jar file, execute `ant dist-jar` at the project root. All tags in this project should be suitable for testing in this manner. If, for some reason, a tag is not testable in this manner, you may have to attempt to manually test the code and debug any problems you find.

To test the project code manually, execute `ant all` at the project's root. The code will be compiled to ../dist/bin. Execute the main class yuuki.YuukiEngine to run the program.
