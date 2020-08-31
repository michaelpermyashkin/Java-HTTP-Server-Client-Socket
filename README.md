# Java-HTTP-Server-Client-Socket
A simple server-client program to demonstrate HTTP communication using socket programming in Java
-------------------------------------------------

### Instructions

1.) Clone Repository
```
git clone https://github.com/michaelpermyashkin/Java-HTTP-Server-Client-Socket.git
```

2.) Open 2 terminals at the location of both .java files

3.) Run Server.java and then Client.java
```
java Server.java <port #>
java Client.java 127.0.0.1 <port #>
```

4.) The client will be prompted with a list of directories relative to the clients location. The client will enter a GET request to access one of the files. The server will process the basic HTTP header and determine whether the client has access to the file requested.

    HTTP 200 OK --> Access permitted and sends back contents of file in plain text

    HTTP 404 FileNotFound --> File does not exist in program directory structure

    HTTP 403 Forbidden --> Client does not have permission to view the requested file
