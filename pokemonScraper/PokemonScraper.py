from urllib2 import Request, urlopen, URLError, HTTPError
import os

def scrapImage(file_name, file_mode, url, local_path):
    req = Request(url, headers={'User-Agent' : "Magic Browser"})

    try:
        print "downloading " + url
        f = urlopen(req)

        if not os.path.exists(local_path):
            os.makedirs(local_path)
        local_file = open(local_path + file_name, "w" + file_mode)
        local_file.write(f.read())
        local_file.close()

    except HTTPError, e:
        print "HTTP Error:", e.code, url
    except URLError, e:
        print "URL Error:" ,e.reason, url

def scrapPokemonGen1(path):
    image_range = range(0,151)
    pokemonGen1= ["bulbasaur","ivysaur","venusaur","charmander","charmeleon","charizard","squirtle","wartortle","blastoise","caterpie","Metapod","Butterfree","Weedle","Kakuna",
    "Beedrill","Pidgey","Pidgeotto","Pidgeot","Rattata","Raticate","Spearow","Fearow","Ekans","Arbok","Pikachu","Raichu","Sandshrew","Sandslash","Nidoran-female","Nidorina",
    "Nidoqueen","Nidoran-male","Nidorino","Nidoking","Clefairy","Clefable","Vulpix","Ninetales","Jigglypuff","Wigglytuff","Zubat","Golbat","Oddish","Gloom","Vileplume","Paras",
    "Parasect","Venonat","Venomoth","Diglett","Dugtrio","Meowth","Persian","Psyduck","Golduck","Mankey","Primeape","Growlithe","Arcanine","Poliwag","Poliwhirl","Poliwrath",
    "Abra","Kadabra","Alakazam","Machop","Machoke","Machamp","Bellsprout","Weepinbell","Victreebel","Tentacool","Tentacruel","Geodude","Graveler","Golem","Ponyta","Rapidash",
    "Slowpoke","Slowbro","Magnemite","Magneton","Farfetchd","Doduo","Dodrio","Seel","Dewgong","Grimer","Muk","Shellder","Cloyster","Gastly","Haunter","Gengar","Onix","Drowzee",
    "Hypno","Krabby","Kingler","Voltorb","Electrode","Exeggcute","Exeggutor","Cubone","Marowak","Hitmonlee","Hitmonchan","Lickitung","Koffing","Weezing","Rhyhorn","Rhydon",
    "Chansey","Tangela","Kangaskhan","Horsea","Seadra","Goldeen","Seaking","Staryu","Starmie","mr-mime","Scyther","Jynx","Electabuzz","Magmar","Pinsir","Tauros","Magikarp",
    "Gyarados","Lapras","Ditto","Eevee","Vaporeon","Jolteon","Flareon","Porygon","Omanyte","Omastar","Kabuto","Kabutops","Aerodactyl","Snorlax","Articuno","Zapdos","Moltres",
    "Dratini","Dragonair","Dragonite","Mewtwo","Mew"]

    for index in image_range:
        pokemonName = pokemonGen1[index].lower()
        url = 'http://www.pokego.org/assets/img/pokemon/' + pokemonName + "-pokemon-go.png"
        line =  pokemonName + '.png'
        file_name = line.translate(None, '-')
        scrapImage(file_name,"b",url, path)


def scrapCandies(path):
    image_range = range(0,79)
    primaryColorsGen1 = ['36C8A4','F09230','85C4D6','A5CD87','E7BC83','E9E0B7','A989BA','EBB9A0','CBA8C9','F5D368','E0D2A4','C5D3E4','D59FC1','F1D3D1','F5865E','F1D2E1','478ABF',
    '7095BF','F1873D','998FD6','B08570','ECE0C4','F4C487','E5D6CB','F3A056','849FCA','E5CE5C','A1BBDE','EBE16E','71ACD8','ACA078','EDE7C7','DFA1B9','D0DAE0','AC9E95','C89462',
    'C7DFE8','BFA4C7','AB9CC5','242223','B5B6B8','F8CB58','EB9063','B64656','F4DDE7','D4D5D6','BD9F88','C8ABBB','E3AEB9','8B8FAE','BCBDBF','E0AEB2','666C9D','978781','9FCFE9',
    'E6E6E7','B49569','E56387','92C587','C44552','F5DB77','F5D477','BCB1AB','D8A058','E87839','6BA7D4','AD8DBE','CA9761','E7757C','DDDCCC','C18335','D4BAD3','326583','3484CD',
    'F5D368','FFDB70','90AED4','CDD2FA','F997A4']
    secondaryColorsGen1 = ['A3FB83','FFE699','F2E8BE','FAE3B1','DB76AD','D29E65','D9D7BE','FE5D6C','F1E090','E2A65D','C9B180','9697C5','C37096','F1BFC0','F6D29C','EAB9CE','DC8DD7',
    '75C06B','FFD159','E24379','EEC5DC','FFE28A','EEEED8','C3927F','3F3D2A','ECECF6','8E7994','DCCEB1','AFD57E','C24589','756108','F59062','EEE1C7','92B6C6','95FB97','AF755F',
    'B6CAED','5F5370','E0B5B3','9B7FB7','626264','AF7961','EDD9CE','F0E5EA','EFC3C1','CBB57A','EEE1C7','E4643B','F0E4CA','DEE0BF','959CA2','C68D87','E46E8C','E3DDB8','FCF7D7',
    'F38469','F5E688','FFCED5','F6F0CF','643187','14175E','F0664E','CFD4D8','887E6F','F6F0CF','FFF0DA','DBD8BE','7E5621','6BC7C5','73CEE2','4E4E48','B196C5','E3DACE','D3FCFE',
    '3F3D2A','FF6A15','EFEAE6','F997A4','FFE6E9']
    baseNamesGen1 = ['Bulbasaur','Charmander','Squirtle','Caterpie','Weedle','Pidgey','Rattata','Spearow','Ekans','Pikachu','Sandshrew','Nidoranfemale','Nidoranmale','Clefairy',
    'Vulpix','Jigglypuff','Zubat','Oddish','Paras','Venonat','Diglett','Meowth','Psyduck','Mankey','Growlithe','Poliwag','Abra','Machop','Bellsprout','Tentacool','Geodude',
    'Ponyta','Slowpoke','Magnemite','Farfetchd','Doduo','Seel','Grimer','Shellder','Gastly','Onix','Drowzee','Krabby','Voltorb','Exeggcute','Cubone','Hitmonlee','Hitmonchan',
    'Lickitung','Koffing','Rhyhorn','Chansey','Tangela','Kangaskhan','Horsea','Goldeen','Staryu','MrMime','Scyther','Jynx','Electabuzz','Magmar','Pinsir','Tauros','Magikarp',
    'Lapras','Ditto','Eevee','Porygon','Omanyte','Kabuto','Aerodactyl','Snorlax','Articuno','Zapdos','Moltres','Dratini','Mewtwo','Mew']

    for index in image_range:
        url = ('https://pokemon.agne.no/candyMaker.php?base=' 
            + primaryColorsGen1[index] + '&secondary=' 
            + secondaryColorsGen1[index] + '&zoom=.175')
        file_name = baseNamesGen1[index].lower() + '_candy.png'
        scrapImage(file_name,"b",url, path)

def scrapGen1Images(path):
    scrapPokemonGen1(path)
    scrapCandies(path)


if __name__=='__main__':
    scrapGen1Images('../app/src/main/res/drawable/')