

Build with:

  mvn clean install

To access the ONP database you need to run this in order to open a tunnel connection to database server (for this of course you need to get a user name):

  ssh -L5432:localhost:5432 <user>@onp.java.no

To run Cibus; go to cibus-web:

  mvn jetty:run

Visit http://localhost:8080/cibus-web

More information about Cibus is available/should be added at http://wiki.java.no/display/smia/Cibus

Happy Hacking

