A *PBKDF2* implementation in Scala
==================================

pbkdf2-scala is an implementation of PBKDF2 [PBKDF2] in scala.

By default, the current implementation uses the Hash-based Message Authentication 
Code (HMAC) with the Secure Hash Algorithm 256 (SHA-256) pseudorandom function. One can very easily change that. 

For using it, you can add the following dependency in SBT.

```Scala
resolvers += "Sonatype OSS" at
  "https://oss.sonatype.org/content/groups/public"

libraryDependencies += "io.github.nremond" %% "pbkdf2-scala" % "0.2"
```


[PBKDF2] http://en.wikipedia.org/wiki/PBKDF2
