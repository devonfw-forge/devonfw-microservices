# Spring native vs Quarkus

Nowadays it is very common to write and application and deploy it to cloud. Serverless and FaaS has become very popular. To deploy a Java application in latest cloud environment there are lot of challenges but major problem faced by developers is, memory footprint and startup time required for Java application. With introduction of framework like micronaut and microprofile java processes are getting faster and lightweight. In similar context Spring has introduced **Spring-native Beta version** and also Red Hat has introduced a new framework called **Quarkus**. This document aims at discussing pros and cons of this two frameworks and which is more suitable with devonfw and why?

## Quarkus

Quarkus is "_A Kubernetes Native Java stack tailored for OpenJDK HotSpot and **GraalVM**, crafted from the best of breed Java libraries and standards_". It is designed around a **container first philosophy**. i.e it is is optimized for low memory usage and fast startup times. 

Quarkus achieves it in following ways:

* First Class Support for GraalVM :

  Use of AOT compilation
* Build Time Metadata Processing : 

  As much processing as possible is done at build time, so your application will only contain the classes that are actually needed at runtime. This results in less memory usage, and also faster startup time as all metadata processing has already been done.
* Reduction in Reflection Usage:

  As much as possible Quarkus tries to avoid reflection, reducing startup time and memory usage.
* Native Image Pre Boot:

  When running in a native image Quarkus pre-boots as much of the framework as possible during the native image build process. This means that the resulting native image has already run most of the startup code and serialized the result into the executable, resulting in even faster startup.

## Spring- native:

Current version of Spring Native 0.9.1 is designed to be used with Spring Boot 2.4.4

Spring Native provides support for compiling Spring applications to native executables using the GraalVM native-image compiler. Using native image provides key advantages, such as instant startup, instant peak performance, and reduced memory consumption. But there are some disadvantages like it is a heavy process that is slower than a regular application. A native image has fewer runtime optimizations after warmup. Finally, it is less mature than the JVM with some different behaviors.

Difference in regular JVM vs GraalVM:

* A static analysis of your application from the main entry point is performed at build time.

* The unused parts are removed at build time.

* Configuration is required for reflection, resources, and dynamic proxies.

* Classpath is fixed at build time.

* No class lazy loading: everything shipped in the executables will be loaded in memory on startup.

* Some code will run at build time.

* There are some [limitations](https://github.com/oracle/graal/blob/master/substratevm/Limitations.md) around some aspects of Java applications that are not fully supported.

## Build time and start time for apps:

| component| Build time | Start time |
| ----------- | ----------- | ----------- |
| Spring native | 19.615s | 2.913s |
| Quarkus | 52.818s | 0.802s |

## Memory footprints:

TODO

## Considering devonfw best practices: 

TODO

## Conclusion:

To summarize , quarkus is a framework with a container first philosophy. It is tailored for OpenJDK HotSpot and GraalVM. Whereas spring started for java application and with spring-native it is adding support for compiling Spring applications to native images with GraalVM in order to provide a new way to deploy Spring applications. As philosophy both framework started are opposite, it seems quarkus is better in two points startup time and memory footprint. But let's not forget the cons, as we know some features of JVM mentioned [here](https://github.com/oracle/graal/blob/master/substratevm/Limitations.md) could not work (yet/easily) in native executable binaries. 
Projects starting as greenfield and mainly focusing on microservices or cloud can use quarkus as it is better suitable for their requirement. Projects which are brownfield using spring as their main technology, they have a chance to use spring-native to improve their performance.





