# INIT
INformation Interchange using Text

INIT is a human-readable data serialisation and exchange format. 

Sample file:

~~~properties
; This is the vet file for Vel
Animal = tortoise
Age = 34
Name = Vel

[Profile]
Favourite-food = baby leaf
Usual-carer = %(Amanda)

[Amanda]
Name = Amanda Wall
Employee-id = 351
~~~

## Motivation 

Does the world need one more textual data interchange format?

No it doesn't.

There are already good choices; in many places XML and JSON are prevalent. For documents with a simple structure, even the relatively terse JSON syntax feels almost plump.  

The syntax of INIT is different. If you like it, use it – or else don't. 

## Structure

An INIT document contains _properties_ organised as a list of _simple_ properties followed by _complex_ properties. 

A simple property can _reference_ a complex property or another simple property.

Whole-line _comments_ are allowed.

The _type_ of the properties is defined externally. 

### Simple properties

A simple property is written as the property name followed by the **=** sign and then the property value. 

Example:

~~~properties
Animal = tortoise
~~~

The property name can be pretty much anything as long as it doesn't contain **=**, **:**, start with **[** or **;**.

The property value can be anything as long as it is on a single line. 

White space around the property name is trimmed. White space around a property value is also trimmed for the default property type. 

#### Reference

A simple property can _reference_ another property by placing the referenced property name inside **%( )** as the property value. 

Example:

~~~properties
Usual-carer = %(Amanda)
~~~

The property takes the value of the referenced property. This is particularly useful for inserting a complex property in the middle of simple properties.

The character **%** in a property value can be written using **%%** to distinguish it from a reference.

A property belonging to a property set can be referenced using **:** to separate the property name from the set name.

If the referenced property is a simple property, its value can be combined with the property value. The text of the reference is replaced with the text of the referenced property value.

Example:

~~~properties
Badge = RMD-%(Amanda:Employee-id)
~~~

### Complex properties 

If more than one line is required to define the property value, that property is written as a complex property. 

A complex property is written as the property name inside square brackets **[ ]** on a line of its own. Its value follows below. 

Example:
~~~properties
[Amanda]
~~~

#### Property set

A _property set_ is an object containing other properties. 

Example:

~~~properties
[Profile]
Favourite-food = baby leaf
Usual-carer = %(Amanda)
~~~

#### Array

Property values arranged as a sequence or as a multi-dimensional table are written as an _array_. 

In an array, property names are replaced with numbers that denote the index of the property. 

Example:

~~~properties
[Visits]
1 = 2019-09-13 07:34
2 = 2019-09-14 10:12
3 = 2019-09-17 08:02
~~~

If the array is multi dimensional, the indices are separated with **.**

To simplify the writing of index numbers, a number can be replaced with a dot (**.**) to indicate the next index in the sequence. For a multi dimensional array, only the last index is incremented. 

Example:

~~~properties
[Results]
1.1 = blue
. = green
. = orange
2.1 = black
. = orange
. = yellow
~~~

#### Multiline text

To write text on multiple lines, just replace it with an array.

Example:

~~~properties
[Address Lines]
1 = 1000 Long Drive
. = Little Mead Green
~~~

### Comments

Comments are on a line of their own and start with **;**.

Example:

~~~properties
; This is the vet file for Vel
~~~
