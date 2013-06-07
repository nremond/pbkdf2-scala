A *PBKDF2* implementation in Scala
==================================

pbkdf2-scala is an implementation of pbkdf2 [1] in scala.

The current implementation is hardcoded with the Hash-based Message Authentication Code (HMAC) Secure Hash Algorithm (SHA-1) pseudorandom function. One can very easily change that. 

[1] http://en.wikipedia.org/wiki/PBKDF2

For using it, you can add the following dependency in SBT.

```Scala
resolvers += "Sonatype OSS" at
  "https://oss.sonatype.org/content/groups/public"

libraryDependencies += "io.github.nremond" %% "pbkdf2-scala" % "0.2"
```
