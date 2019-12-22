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
| text | | some text with leading and trailing whitespace trimmed | if the value is an array instead, it is interpreted as multiline text |
| raw | | some text | preserves all whitespace |
| urlformdata | ReplacePlusSign | some trimmed text with characters encoded in URL format (**%XX**) | encoded characters are replaced with their equivalent, if the option is set **+** is replaced with a space |
| jsontext | | quoted text with characters encoded in JSON format (**\n...**) | the quotes are removed and encoded characters replaced with their equivalent |
| xmltext | | some trimmed text with characters encoded as XML entities (**&XXX;**) | encoded characters are replaced with their equivalent |
| base64 | | base-64 encoded data | the data is decoded to binary format |
| number | Format="#.#E0" | a number with optional decimals, exponent and sign | |
| integer | | a signed integer number | |
| datetime | Format="YYYY-MM-DD hh:mm:ss.SSSZ" | a date and time | the time components that are missing are set to 0, default timezone used if no timezone set |
| date | Format="YYYY-MM-DD" | a date | |
| time | Format="hh:mm:ss.SSSZ" | a time | missing components are set to 0, default timezone used if no timezone set (_not_ local timezone) |
| duration | Format="hh:mm:ss.SSS" | a duration | |

## Hidden properties

Properties which are defined in the configuration file but not used can be referenced as hidden properties in the INIT file.
