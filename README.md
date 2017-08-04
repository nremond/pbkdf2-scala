# PBKDF2 [![Build Status](https://travis-ci.org/nremond/pbkdf2-scala.svg?branch=master)](https://travis-ci.org/nremond/pbkdf2-scala)

*pbkdf2-scala* is an implementation of [PBKDF2] in Scala. It is cross-compiled for Scala 2.10, 2.11, and 2.12.


For using it, you can add the following dependency in SBT.

```Scala
libraryDependencies += "io.github.nremond" %% "pbkdf2-scala" % "0.6.2"
```

## Standards Conformance

This implementation conforms to [RFC 2898][RFC-2898], and has been tested using the
test vectors in Appendix B of [RFC 3962][RFC-3962]. Note, however, that while
those specifications use [HMAC][HMAC]-[SHA-1][SHA1], this implementation
defaults to [HMAC][HMAC]-[SHA-256][SHA1]. As a matter of fact, SHA-256 provides 
a longer bit length and in addition, NIST has [stated][NIST] that SHA-1 should be phased out
due to concerns over recent cryptanalytic attacks.


## Setting the number of iterations

Choosing the correct value for this parameter is thus a trade-off: it
should be set as high as possible, to make attacks as difficult as possible,
without making legitimate applications unusably slow.  
[Security Considerations section of RFC 3962][ITERS] provides a useful example 
on how to consider that choice.

The current default value is set to 20k. 

## Using the library

You can use the raw PBKDF2 function which as the following signature:

```scala
object PBKDF2 {
  def apply(password: Array[Byte], 
            salt: Array[Byte], 
            iterations: Int = 20000, 
            dkLength: Int = 32, 
            cryptoAlgo: String = "HmacSHA512"): Array[Byte]
}
```

Alternatively, you can use the following functions that will handle the salting for you:

```scala
object SecureHash {
  def createHash(password: String,
                 iterations: Int = 20000,
                 dkLength: Int = 32,
                 cryptoAlgo: String = "HmacSHA512"): String

  def validatePassword(password: String, hashedPassword: String): Boolean
}
```

validatePassword and createHash output are compatible with [PassLib][PASS_LIB] for the supported pseudo-random-functions (`HmacSHA1`, `HmacSHA256`, `HmacSHA512`).


## Release Notes

* 0.6:
Scala 2.12 support
* 0.5:
Breaking changes in `SecureHash` to implement a version of Modular Crypt Format (MCF) compatible with [PassLib][PASS_LIB].
User of older version of this library can find the previous API [here](src/main/scala/io/github/nremond/legacy/SecureHash.scala).
Update the default security settings to `HmacSHA512`.
* 0.4:
Introduce the `SecureHash` class to handle the salting.
* 0.3:
Update the default security settings.

## License

See the `license.txt` file for the terms under which it may be used and distributed.





[PBKDF2]: http://en.wikipedia.org/wiki/PBKDF2 "Wikipedia: PBKDF2"
[RFC-2898]: http://tools.ietf.org/html/rfc2898 "RFC 2898"
[RFC-3962]: http://tools.ietf.org/html/rfc3962 "RFC 3962"
[SHA1]: http://en.wikipedia.org/wiki/SHA-1 "Wikipedia: SHA-1"
[HMAC]: http://tools.ietf.org/html/rfc2104 "RFC 2104"
[ITERS]: http://tools.ietf.org/html/rfc3962#page-6 "RFC 3962: Section 8"
[NIST]: http://csrc.nist.gov/groups/ST/hash/statement.html "NIST Comments on Cryptanalytic Attacks on SHA-1"
[PASS_LIB]: https://pythonhosted.org/passlib/lib/passlib.hash.pbkdf2_digest.html "PassLib"
