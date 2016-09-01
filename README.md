# EvolveEval

Level Up Quick. A Pokémon GO tool.

##App Summary

Evolving pokémon during a Lucky Egg is one of the best ways to gain experience in Pokemon GO! But determining how many pokémon can evolve and the amount of experience that will be gained, can be a bit tricky to figure out. This app seeks to solve that problem! 

Containing all of the pokémon in the Pokemon GO Pokédex, this calculator will display the total number of possible evolutions and experience. After the trainer enters 1) Any evolution line 2) The number of candies for that line 3) The number of each pokémon in that line and 4) Whether it is registered in the Pokédex, he/she will be able to determine if the Lucky Egg is ready to be used.

The app can be downloaded [here](http://austinheartscs.co.nf/apks/app-release.apk).

Disclaimer: this is not through the Play Store and you will need to allow unknown sources in your device security settings first.

Happy evolving!

## Code Summary

A simple app featuring:
* Python script that scraps Pokemon GO images of their website as well as candies to the local resources file.
* SQLite database to holding a table of the pokdex from which the content provider pulls.
* List adapters that create the modules for each evolution line. The modules are then mapped into varialbes of the Equation model.
* Some poorly design UI... :) Not great at that kind of thing