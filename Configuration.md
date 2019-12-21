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
| CaseSensitiveNames | false | true false | controls if property names are case sensitive | if false the case is preserved but ignored |
| AllowRoundBrackets | false | true false | if true, references can be written **%( )** | this implies property names should not contain **)** or start with **(** |
| PrivatePropertyPrefix | | any valid name prefix | a prefix added to properties so they don't appear in the output | |
| HiddenPropertyWarning | warn | none warn error | controls how properties defined but not used are reported | if none a misspelt option is silently ignored |
| DefaultType | any(text, array(text), set) | a type | the type of a property when not specified in the schema | |
| DefaultPresence | optional | required optional | controls if properties are optional by default | |
| DefaultTimeZone | +00:00 | a time zone offset | used for dates and times that don't specify a timezone | the date or time should be unambiguous |

## Type schema

The _Types_ property set in the configuration file controls the type of properties in the INIT document.

Type declaration:

~~~regex
(required|optional)? $type (\($options...\))? (host type $original_type)? (default=$value...)?
~~~

## Hidden properties

Properties which are defined in the configuration file but not used can be referenced as hidden properties in the INIT file.
