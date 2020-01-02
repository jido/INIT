# INIT File Structure

An INIT file contains simple properties, complex properties, comments and loaded configurations.

---

## Simple properties

A simple property is written as the property name followed by the **=** sign and then the property value. 

Example:

~~~properties
Animal = tortoise
~~~

The property name can be pretty much anything as long as it doesn't contain **=**, **:**, **]** start with **[** or **;**.

The property value can be anything as long as it is on a single line. 

White space around the property name is trimmed.

White space around a property value is also trimmed for the default property type. 

### Reference

A simple property can _reference_ another property by placing the referenced property name inside **%[ ]** as the property value. 

Example:

~~~properties
Usual-carer = %[ppAmanda]
~~~

The property takes the value of the referenced property.

> **NOTE:** This is particularly useful for inserting a complex property in the middle of simple properties.

If **;** is added after **%[**, it is ignored and not counted as reference.

A property belonging to a property set can be referenced using **:** to separate the property name from the set name.

### Inline reference

If the referenced property is a simple property, its value can be combined with the property value.

The text of the reference is replaced with the text of the referenced property value.

Example:

~~~properties
Badge = RMD-%[Profile:Usual-carer:Employee-id]
~~~

If the referenced property is a property set, its set of properties can be merged with another set.

If the referenced property is an array it can be extended with another array.

Example:

~~~properties
FullAddress = %[Recipient] %[AddressLines] %[City]
~~~

---

## Complex properties 

If more than one line is required to define the property value, that property is written as a complex property. 

A complex property is written as the property name inside square brackets **[ ]** on a line of its own. 

Its value follows below. 

Example:
~~~properties
[ppAmanda]
~~~

### Property set

A _property set_ is an object containing other properties. 

Example:

~~~properties
[Profile]
Favourite-food = baby leaf
Usual-carer = %[ppAmanda]
~~~

### Array

An _array_ contains property values arranged as a sequence or as a multi-dimensional table. 

Property names are replaced with numbers that denote the index of the value. 

Example:

~~~properties
[Visits]
1 = 2019-09-13 07:34
2 = 2019-09-14 10:12
3 = 2019-09-17 08:02
~~~

> **NOTE:** If the array is multi dimensional, the indices are separated with **.**

To simplify the writing of index numbers, a number can be replaced with a dot (**.**) to indicate the next index in the sequence.

For a multi dimensional array, only the last index is incremented. 

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

### Multiline text

To write text on multiple lines, just replace it with an array.

Example:

~~~properties
[Address Lines]
1 = 1000 Long Drive
. = Little Mead Green
~~~

---

## Comments

Comments are on a line of their own and start with **;**.

Example:

~~~properties
; This is the vet file for Vel
~~~

---

## Loaded configurations

Document options, property types and predefined properties can be loaded from an external INIT file.

The configuration applies to the document that loads it.

To load a configuration file, put its name or URI inside **[[ ]]** at the beginning of the document.

Example:

~~~properties
[[vetfile.config]]
~~~

If no configuration file is loaded, the [default configuration](Configuration.md) is used.
