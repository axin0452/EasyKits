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

![](https://i.imgur.com/18UX4KU.png)	

You'll know you did it right if the color format changes:

![](https://i.imgur.com/ZdE93pP.png)

Right click to use sign.
You can set in the config whether using the sign shows the kit or gets the kit by setting 'sign-action' to either 'view -or- get'

#### Permissions:
* easykits.kit.<kit> - Access to the kit by name
* easykits.cmd.create - Access to /kit create
* easykits.cmd.delete - Access to /kit remove
* easykits.cmd.cooldown - Access to /kit cooldown
* easykits.cmd.limit - Access to /kit limit
* easykits.cmd.price - Access to /kit price
* easykits.cmd.give - Access to /kit give
* easykits.cmd.list - Access to /kit list - Access to /kit book
* easykits.cmd.view - Access to /kit view - Allow opening kit in book.
* easykits.cmd.book - Access to /kit book
* easykits.cmd.reset - Access to /kit reset
* easykits.override.cooldown - Force player to use cooldown time.
* easykits.override.limit - Set Max use for each kit.
* easykits.override.price - Bypass price of kit if economy is enabled.
* easykits.sign.create - Allow player to create kit signs
* easykits.sign.break - Allow player to break kit signs
* easykits.modify - Allows player to modify kit when viewing it

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
