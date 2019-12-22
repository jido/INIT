# Configuration

**The INIT Configuration File**

---

The INIT format is suitable to write configuration files.

Its own configuration is written with INIT. 

Example:

~~~properties
PrivatePropertyPrefix = pp

[Types]
Animal = required text
Age = integer
Name = required text
Profile = required ProfileProperties

[ProfileProperties]
Favourite-food = text
Mate = text
Usual-carer = CarerProperties

[CarerProperties]
Name = required text
Employee-id = required integer
~~~

---

## Options

The configuration options control the INIT file output.

| _Option_ | _Default value_ | _Possible values_ | _Description_ | _Notes_ |
|-|-|-|-|-|
| AllowRoundBrackets | false | true false | controls if references can be written **%( )** | implies property names can't contain **)** or start with **(** |
| CaseSensitiveNames | false | true false | controls if property names are case sensitive | if false the case is preserved but ignored |
| DefaultPresence | optional | required optional | controls if properties are required or optional by default | |
| DefaultTimeZone | +00:00 | a time zone offset | used for dates and times that don't specify a timezone | the date or time should be unambiguous |
| DefaultType | any(text, array(text), set) | a type | the type of a property when not specified in the schema | |
| UnknownOptionWarning | warn | none warn error | controls how unknown options and unused custom types are reported | if none, a misspelled option or a redundant custom type is silently ignored |
| PrivatePropertyPrefix | | any valid name prefix | a prefix added to properties so they don't appear in the output | |

---

## Type schema

The _Types_ property set in the configuration file controls the type of properties in the INIT document.

Type declaration regular expression:

~~~regex
(required|optional)? $type ( \($parameters...\) )? $modifiers
~~~

Example:

~~~properties
Name = required text
~~~

The modifiers are:

| _Modifier_ | _Use_ |
|-|-|
| `case (sensitive\|insensitive)` | controls if the property names of a set or the values in a choice are case sensitive, the _CaseSensitiveNames_ option is used if not specified |
| `host type $original_type` | informs about the name of the type in the host programming language for better type mapping |
| `default=$value` | provides a default value, must come last and implies **optional** |

Example:

~~~properties
ZooOwnership = choice(true, false) case sensitive host type boolean default=true
~~~

### Custom types

Custom types are written as the name of a top-level property. The property value should be a type or a property set containing type declarations.

Example:

~~~properties
Usual-carer = CarerProperties

[CarerProperties]
Name = required text
Employee-id = required integer
~~~

### Predefined types
The predefined types are:

| _Type_ | _Parameters_ | _Description_ | _Comments_ |
|-|-|-|-|
| any | a list of allowed types | can be any of the listed types (all types if no parameter) | |
| set | | a property set | it is generally preferable to use a custom type instead of **set** |
| array | a type | an array containing elements with the specified type | |
| choice | a list of values or the name of a property set | a property that can take specific values | if one parameter, it is a property set, and synonyms can be defined using **InputValue = OutputValue** |
| text | | some text | leading and trailing whitespace is trimmed, if the value is an array instead, it is interpreted as multiline text |
| raw | | some text | preserves all whitespace, multiline allowed using an array |
| urlformdata | ReplacePlusSign | some text with characters encoded in URL format (**%XX**) | trimmed, encoded characters are replaced with their equivalent, if the option is set **+** is replaced with a space, multiline allowed using an array |
| jsontext | | text inside quotes with characters encoded in JSON format (**\n...**) | the quotes are removed and encoded characters replaced with their equivalent, multiline allowed using an array with each line in quotes |
| xmltext | | some text with characters encoded as XML entities (**&XXX;**) | trimmed, encoded characters are replaced with their equivalent, multiline allowed using an array |
| base64 | | base-64 encoded data | can be split in multiple lines using an array, the data is decoded to binary format |
| hex | | data encoded in hexadecimal | can be split in multiple lines using an array, the data is decoded to binary format |
| number | Format="#.#E0" | a number with optional decimals, exponent and sign | |
| integer | | a signed integer number | |
| datetime | Format= "YYYY-MM-DD HH:mm:ss.SSSZ" TimeZone= _DefaultTimeZone_ | a date and time | the time components that are missing are set to 0, default timezone used if no timezone set |
| date | Format= "YYYY-MM-DD" | a date | |
| time | Format= "HH:mm:ss.SSSZ" TimeZone= _DefaultTimeZone_ | a time | missing components are set to 0, default timezone used if no timezone set (_not_ local timezone) |
| duration | Format= "H:mm:ss.SSS" | a duration | missing components are set to 0 |

---

## Hidden properties

Properties in the _Global_ property set can be referenced as hidden properties in the INIT file.

Example:

~~~properties
[Global]
ContactLink = http://www.example.com/funtimezoo/contact.html
~~~
