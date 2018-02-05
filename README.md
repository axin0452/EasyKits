# EasyKits

#### Usage:
To create a kit, simply arrange your inventory and armor with the items you would like in the kit.
    /kit create <name>
To set a price for a kit run the following:
    /kit price <kit> <price>
To set a cooldown time for a kit run the following:
NOTE: Time format examples: 10s, 1m, 1h, 1d
    /kit cooldown <kit> <cooldown>
To set the max limit player can get kit with the following:
    /kit limit <kit> <limit>
To reset players cooldown or limit on a kit type the following:
    /kit reset cooldown <kit> <player>
    /kit reset limit <kit> <player>
To view all kits player has permission to obtain the kits book with the following:
    /kit book
This book will always contain an updated list of kits. A max a 54 kits can be displayed. Simply right click to open. The inventory will show have a book for each kit. click to view contents of kit. If you would like to obtain this click, click the nether star in the bottom right corner.

#### Notes:
If you you prefer commands you can list kits with the following:
    /kit list
To view contents of a kit:
    /kit view <kit>
To obtain a kit:
    /kit <kit>
	
#### Signs:
To create a sign type <kit> then the kit name on next line
	
![](https://i.imgur.com/ZdE93pP.png)

You'll know you did it right if the color format changes:

![](https://i.imgur.com/18UX4KU.png)

Right click to use sign.
You can set in the config whether using the sign shows the kit or gets the kit by setting 'sign-action' to either 'view -or- get'

#### Permissions:
* EasyKits.kits.<kit> - Access to the kit by name
* EasyKits.cmd.create - Access to /kit create
* EasyKits.cmd.delete - Access to /kit remove
* EasyKits.cmd.cooldown - Access to /kit cooldown
* EasyKits.cmd.limit - Access to /kit limit
* EasyKits.cmd.price - Access to /kit price
* EasyKits.cmd.give - Access to /kit give
* EasyKits.cmd.list - Access to /kit list - Access to /kit book
* EasyKits.cmd.view - Access to /kit view - Allow opening kit in book.
* EasyKits.cmd.reset - Access to /kit reset
* EasyKits.override.cooldown - Force player to use cooldown time.
* EasyKits.override.limit - Set Max use for each kit.
* EasyKits.override.price - Bypass price of kit if economy is enabled.
* EasyKits.sign - Allow creation of kit signs

#### Commands:
* /kit create <kit>
* /kit delete <kit>
* /kit cooldown <kit> <cooldown>
* /kit limit <kit> <limit>
* /kit price <kit> <price>
* /kit book
* /kit list
* /kit view <kit>
* /kit reset <cooldown/limit> <kit> <player>
* /kit <kit>
* /kit give <player> <kit>
