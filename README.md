YOAuth
======

The best and simplest OAuth2.0 client library

This particular implementation is targetted for Java/JSP based projects. Token fetching has been abstracted to be totally transparent for developers. All grant types are supported. 

How to use this?
* The entire implementation is available under YOAuth folder under GIT home; so copy and use that folder as a starter JSP based project and deploy it on your Application Container, and then access the index.html page. That would fetch the token using the particular grant type you have set in index.html.
* If however you want to use this in a Java Application, just take the client/Main.java and run it - modify it to use the grant type you want. 
* In both cases remember to configure the right URLs in yoauth.properties.

Try it out and let me know if you need any improvements. 
