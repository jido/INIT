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
| HiddenPropertyWarning | warn | none warn error | controls how properties defined but not used are reported | if none, a misspelled option is silently ignored |
| PrivatePropertyPrefix | | any valid name prefix | a prefix added to properties so they don't appear in the output | |

## Type schema

The _Types_ property set in the configuration file controls the type of properties in the INIT document.

Type declaration:

~~~regex
(required|optional)? $type (\($parameters...\))? (host type $original_type)? (default=$value...)?
~~~

Example:

~~~properties
Name = required text
~~~

Custom types are written as the name of a top-level property. The property value should be a type or a property set containing type declarations.

Example:

~~~properties
Usual-carer = CarerProperties

[CarerProperties]
Name = required text
Employee-id = required integer
~~~

The predefined types are:

| _Type_ | _Parameters_ | _Description_ | _Comments_ |
|-|-|-|-|
| any | a list of types | allows any of the listed types (all types if no parameter) | |
| set | | a property set | it is generally preferable to use a custom type instead of **set** |
| array | a type | an array containing elements with the specified type | |
| text | | some text | leading and trailing whitespace is trimmed, if the value is an array instead, it is interpreted as multiline text |
| raw | | some text | preserves all whitespace, multiline allowed using an array |
| urlformdata | ReplacePlusSign | some text with characters encoded in URL format (**%XX**) | trimmed, encoded characters are replaced with their equivalent, if the option is set **+** is replaced with a space, multiline allowed using an array |
| jsontext | | text inside quotes with characters encoded in JSON format (**\n...**) | the quotes are removed and encoded characters replaced with their equivalent, multiline allowed using an array with each line in quotes |
| xmltext | | some text with characters encoded as XML entities (**&XXX;**) | encoded characters are replaced with their equivalent, multiline allowed using an array |
| base64 | | base-64 encoded data | can be split in multiple lines using an array, the data is decoded to binary format |
| hex | | data encoded in hexadecimal | can be split in multiple lines using an array, the data is decoded to binary format |
| number | Format="#.#E0" | a number with optional decimals, exponent and sign | |
| integer | | a signed integer number | |
| datetime | Format= "YYYY-MM-DD hh:mm:ss.SSSZ" | a date and time | the time components that are missing are set to 0, default timezone used if no timezone set |
| date | Format= "YYYY-MM-DD" | a date | |
| time | Format= "hh:mm:ss.SSSZ" | a time | missing components are set to 0, default timezone used if no timezone set (_not_ local timezone) |
| duration | Format= "hh:mm:ss.SSS" | a duration | missing components are set to 0 |

## Hidden properties

Properties which are defined in the configuration file but not used can be referenced as hidden properties in the INIT file.
