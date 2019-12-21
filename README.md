# INIT
**INformation Interchange using Text**

---

INIT is a human-readable data serialisation and exchange format. 

Sample file:

~~~properties
[[vetfile.config]]

; This is the vet file for Vel
Animal = tortoise
Age = 34
Name = Vel

[Profile]
Favourite-food = baby leaf
Usual-carer = %[Amanda]

[Amanda]
Name = Amanda Wall
Employee-id = 351
~~~

## Motivation 

Does the world need one more textual data interchange format?

No it doesn't.

There are already good choices; in many places XML and JSON are prevalent. For documents with a simple structure, even the relatively terse JSON syntax feels almost plump.  

The syntax of INIT is different. If you like it, use it – or else don't. 

---

## Structure

* _An INIT document contains_ properties.
* _They are organised as a list of_ simple _properties followed by_ complex _properties._ 
* _A property can_ reference _another property._
* _Whole-line_ comments _are allowed._
* _The_ type _of the properties is defined externally._

[More information about the structure](Structure.md)

---

## Configuration

* _An INIT document can_ load _its configuration from an external file._
* _Any number of_ configuration files _can be loaded for the document._
* _The configuration defines_ document options.
* _The configuration describes the_ type schema.
* _The configuration_ pre-defines _hidden properties._

[More information about configuration files](Configuration.md)
