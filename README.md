Resume Generator
================

Transforms an XML resume document into a professional formatted PDF.
The transformation is driven via an [XSL-FO stylesheet][1]

Runs as both a command line tool and an HTTP server.
When running as an HTTP server, it's capable of transforming the XML resume document into either HTML or PDF.
The XML transformation (as well as the generated PDF) is cached after the first invocation.

[1]: http://xmlgraphics.apache.org/fop/fo.html
