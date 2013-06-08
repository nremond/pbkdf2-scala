# PBKDF2 #

*pbkdf2-scala* is an implementation of [PBKDF2] in scala.


For using it, you can add the following dependency in SBT.

```Scala
libraryDependencies += "io.github.nremond" %% "pbkdf2-scala" % "0.2"
```



## Standards Conformance ##

This implementation conforms to [RFC 2898][RFC-2898], and has been tested using the
test vectors in Appendix B of [RFC 3962][RFC-3962]. Note, however, that while
those specifications use [HMAC][HMAC]-[SHA-1][SHA1], this implementation
defaults to [HMAC][HMAC]-[SHA-256][SHA1]. As a matter of fact, SHA-256 provides 
a longer bit length and in addition, NIST has [stated][NIST] that SHA-1 should be phased out
due to concerns over recent cryptanalytic attacks.


## Setting the number of *iterations* ##

Choosing the correct value for this parameter is thus a trade-off: it
should be set as high as possible, to make attacks as difficult as possible,
without making legitimate applications unusably slow.  
[Security Considerations section of RFC 3962][ITERS] provides a useful example 
on how to consider that choice. 

In the test source folder, the `PBKDF2Benchmark.scala` file is a useful benchmarking
resource to help you tuning this paramater. 

The current default value is set to 20k. 

## License ##

See the `license.txt` file for the terms under which it may be used and distributed.





[PBKDF2]: http://en.wikipedia.org/wiki/PBKDF2 "Wikipedia: PBKDF2"
[RFC-2898]: http://tools.ietf.org/html/rfc2898 "RFC 2898"
[RFC-3962]: http://tools.ietf.org/html/rfc3962 "RFC 3962"
[SHA1]: http://en.wikipedia.org/wiki/SHA-1 "Wikipedia: SHA-1"
[HMAC]: http://tools.ietf.org/html/rfc2104 "RFC 2104"
[ITERS]: http://tools.ietf.org/html/rfc3962#page-6 "RFC 3962: Section 8"
[NIST]: http://csrc.nist.gov/groups/ST/hash/statement.html "NIST Comments on Cryptanalytic Attacks on SHA-1"
