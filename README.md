# INIT
INformation Interchange using Text

INIT is a human-readable data serialisation and exchange format. 

Sample file:

~~~
Animal = tortoise
Age = 34
Name = Vel

[Profile]
Favourite-food = baby leaf
Usual-carer := Amanda

[Amanda]
Name = Amanda Wall
Employee-id = 351
~~~

## Motivation 

Does the world need one more textual data interchange format?

No it doesn't. There are already good choices; in many places XML and JSON are prevalent. 

The syntax of INIT is different. If you like it, use it – or else don't. 

## Structure

An INIT document contains _properties_ organised as a list of simple properties followed by complex properties. 

A simple property can _reference_ a complex property or another simple property. 

The type of properties is defined externally. 

### Simple properties

A _simple_ property is written as the property name followed by the **=** sign and the property value. 

Example:

~~~
Animal = tortoise
~~~

The property name can be pretty much anything, as long as it doesn't contain **=**, start with **[** or end with **:**. 

The property value can also be anything as long as it is on a single line. 

White space around the property name is trimmed. White space around a property value is also trimmed for the default property type. 

A simple property can _reference_ another property by replacing **=** with **:=** and writing the name of the referenced property as the property value. 

Example:

~~~
Usual-carer := Amanda
~~~

### Complex properties 

If more than one line is required to define the property value, that property is written as a _complex_ property. 

A complex property is written as a property name inside square brackets **[ ]** on a line of its own. It is followed by its value on the following lines. 

Example:
~~~
[Amanda]
~~~

#### Property set

A _property set_ is an object containing other properties. 

Example:

~~~
[Profile]
Favourite-food = baby leaf
Usual-carer := Amanda
~~~

#### Array

Property values arranged as a sequence or as a multi-dimensional table are written as an _array_. 

In an array, property names are replaced with numbers that denote the index of the property. 

Example:

~~~
[Visits]
1 = 2019-09-13 07:34
2 = 2019-09-14 10:12
3 = 2019-09-17 08:02
~~~

If the array is multi dimensional, the indices are separated with **.**

To simplify writing the index numbers, the number can be replaced with a **.** to indicate the next index in the sequence. For a multi dimensional array, only the last index is incremented. 

Example:

~~~
[Results]
1.1 = blue
. = green
. = orange
2.1 = black
. = orange
. = yellow
~~~
