# devon4j vs Quarkus REST client

This guide mainly aims to compare features available in quarkus REST client and devon4j REST client.And also which REST client we should consider for a particular scenario in Quarkus.
Documentation for devon4j REST client are available [here](https://github.com/devonfw/devon4j/blob/master/documentation/guide-service-client.asciidoc).
Quarkus support multiple REST client extension such as Microprofile REST client, RESTEasy, Vertex HTTP client , reactive JAX-RS REST client.

Details for above extension can be found at 
* RESTEasy:
  RESTEasy is a JBoss / Red Hat project that provides various frameworks to help you build RESTful Web Services and RESTful Java applications. It is an implementation of the Jakarta RESTful Web Services, an Eclipse Foundation specification that provides a Java API for RESTful Web Services over the HTTP protocol.
  Moreover, RESTEasy also implements the MicroProfile REST Client specification API. More details can be found [here](https://resteasy.github.io/)
* Microprofile: 
  The MicroProfile Rest Client provides a type-safe approach to invoke RESTful services over HTTP. As much as possible the MP Rest Client attempts to use Jakarta RESTful Web Services 2.1 APIs for consistency and easier re-use.
  More details can be found [here](https://github.com/eclipse/microprofile-rest-client)
* Vertex: 
  Vert.x Web Client is an asynchronous HTTP and HTTP/2 client.The Web Client makes easy to do HTTP request/response interactions with a web server, and provides advanced features like Json body encoding / decoding,
  request/response pumping,request parameters,unified error handling,form submissions. More details can be found [here](https://vertx.io/docs/vertx-web-client/java/)

Below is table containing overview of different features:


|Feature|	Quarkus REST Client|	devon4j REST Client|
| -----------|----------- |----------- |
|Sync and Async|	Yes|	Yes|
|Simple usage|	Yes|	Yes|
|Flexible Configuration|	Yes|	Yes|
|Other Extensions|	Supports Microprofile REST Client,<br> RESTEasy, VertX HTTP Client,<br> Reactive JAX-RS rest client|	No as of now,<br> yet would like to confirm|
|Headers|	Yes|	Yes|
|Multipart|	Yes|  |	
|Service Discovery|    |		Yes|
|Timeouts|Yes|	Yes|
|Error Handling|	Yes|	Yes|
|Logging	|  |  |	
|Resilience	|   |    |	

Vertex REST client VS Micro-profile REST client:

Vertex REST client follow API driven approach. It has asynchornous support, multipart support and reactive support. <br>
Microprofile REST client follows annotation driven approach. It also have asynchronous support. <br><br>
If you want to use asynchronous support without reactive support you can use MP client.

Reference document : [here](https://dzone.com/articles/invoking-rest-apis-asynchronously-with-quarkus)

Reactive JAX-RS client(using Jersy API) vs Eclipse Vertx:
Both REST client have reactive support. With Reactive JAS-RS client we can write cleaner, readable code. <br>
Eclipse Vertex can achieve highest throughput, and lowest latency.<br>

Reference document links:
https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/rx-client.html
https://medium.com/@tongqingqiu/build-high-performance-restful-api-5fdbbd859b58
https://github.com/vert-x3/vertx-awesome/blob/master/README.md


