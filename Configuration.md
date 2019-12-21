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
