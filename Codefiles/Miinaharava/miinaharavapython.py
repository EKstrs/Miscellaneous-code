import random 
import time
import haravasto
import sys
import datetime

tila = {
    "kentta": []
}

tyhjio = {
    "tyhja": []
}

ruudut = {
    "jaljella": []
}

def kysy_kentta():
    """
    Kysyy käyttäjältä kentän koon ja miinojen lukumäärän
    """
    global leveys
    global korkeus
    global miina_maara
    while True:
        try:
            leveys = int(input("Anna kentän leveys: "))
            korkeus = int(input("Anna kentän korkeus: "))
            miina_maara = int(input("Anna miinojen lukumäärä: "))
            break
        except ValueError:
            print("Anna arvot kokonaislukuna")
    

def kentta_koko():
    """
    Valmistaa kaksi kenttää käyttäjän syötteiden mukaisesti. Toinen on tyhjä ja toisessa on kaikki miinat ja numerot paikallaan.
    Valmistaa myös listan kaikista ruuduista kentällä.
    """
    kentta = []
    for rivi in range(korkeus):
        kentta.append([])
        for sarake in range(leveys):
            kentta[-1].append("0")

    tila["kentta"] = kentta
    
    tyhja = []
    for rivi in range(korkeus):
        tyhja.append([])
        for sarake in range(leveys):
            tyhja[-1].append(" ")
    tyhjio["tyhja"] = tyhja        
    
    jaljella = []
    for x in range(leveys):
        for y in range(korkeus):
            jaljella.append((x, y))
    ruudut["jaljella"] = jaljella            
    
def miinoita(kentta, vapaat, lkm):
    """
    Asettaa kentälle miinoja käyttäjän valinnan mukaisen määrän satunnaisiin paikkoihin 
    ja numeroi miinojen ympärillä olevat ruudut.
    """
    miinat = random.sample(vapaat, lkm)
    for i in miinat:
        x, y = i
        kentta[y][x] = "x" 

def laske(kentta):
    """
    Jos ruudun ympärillä on miina tämä funktio asettaa numeron ruudulle, joka vastaa ympärillä olevien miinojen lukumäärää.
    """
    for i in range(len(kentta)):
        for j in range(len(kentta[0])):
            if kentta[i][j] != "x":
                luku = 0
                for a in (-1, 0, 1):
                    for b in (-1, 0 ,1):
                        if 0 <= i + a < len(kentta) and 0 <= j + b < len(kentta[0]) and kentta[i+a][j+b] == "x":
                            luku += 1
                kentta[i][j] = str(luku)
                
def avaa_turvalliset(tyhja, kentta, x, y):
    """
    Jos pelaaja painaa turvallista ruutua tämä funktio aukaisee kaikki ympäröivät turvalliset ruudut kunnes tullaan numeroituihin tai miinallisiin ruutuihin.
    """
    turva = [(x, y)]
    if kentta[y][x] == "0":
        while turva != []:        
            k, c = turva.pop(-1)
            tyhja[c][k] = "0"
            for i in range(k - 1, k + 2):
                for j in range(c - 1, c + 2):
                    if 0 <= i < len(kentta[0]) and 0 <= j < len(kentta):
                        if kentta[j][i] == "0" and tyhja[j][i] != "0":
                            tyhja[j][i] = "0"
                            turva.append((i, j))
                        elif kentta[j][i] != "0" and kentta[j][i] != "x":
                            tyhja[j][i] = kentta[j][i]
    else:
        pass


def tarkista_voitto_havio(tyhja, x, y):
    """
    Tarkistaa onko pelaaja voittanut tai hävinnyt pelin.
    """
    if tyhja[y][x] == "x":
        print("Häviö")
        havio = "Häviö"
        haravasto.aseta_hiiri_kasittelija(sulje)
        tallenna_tilastot(havio)
    else:
        pass
       
    luku = 0
    for i in range(len(tyhja)):
        for j in range(len(tyhja[0])):
            if tyhja[i][j] == " " or tyhja[i][j] == "f":
                luku += 1
            else:
                pass                
    if luku == miina_maara:
        print("Voitto")
        voitto = "Voitto"
        haravasto.aseta_hiiri_kasittelija(sulje)
        tallenna_tilastot(voitto)
    else: 
        pass
        
def aika_muunnin(loppu):
    """
    Muuntaa peliin kuluneen ajan minuuteiksi ja sekunneiksi
    """
    sekunnit = loppu - aloitus_aika
    minuutit = sekunnit // 60
    sekunnit = sekunnit % 60
    minuutit = minuutit % 60
    return minuutit, sekunnit
    
def paiva_muunnin(aika):
    """
    muuntaa päivämäärään ja kellonajan muotoa.
    """
    ajoitus = aika.strftime("%d/%m/%Y %H:%M:%S")
    return ajoitus

def sulje(x, y, nappi, modit):
    haravasto.lopeta()

                        
def kasittele_hiiri(x, y, nappi, muokkausnappit):
    """
    Hiiren käsittelijä funktio
    """
    nappaimet = {
        "vasen": haravasto.HIIRI_VASEN,
        "oikea": haravasto.HIIRI_OIKEA,
    }
    i = int(x / 40)
    j = int(y / 40)
    if nappi == nappaimet["vasen"]:
        tyhjio["tyhja"][j][i] = tila["kentta"][j][i] 
        avaa_turvalliset(tyhjio["tyhja"], tila["kentta"], i, j)
        tarkista_voitto_havio(tyhjio["tyhja"], i, j)
    elif nappi == nappaimet["oikea"]:       
        if tyhjio["tyhja"][j][i] == " ":
            tyhjio["tyhja"][j][i] = "f"
        elif tyhjio["tyhja"][j][i] ==  "f":
            tyhjio["tyhja"][j][i] = " "
        else: 
            pass
            
def paavalikko():
    """
    Pelin päävalikko, josta käyttäjä voi valita uuden pelin, tilastoiden katselun tai lopettaa pelin.
    """
    tee_tiedosto()
    print("Miinaharava")
    while True:
        print("1. Uusi peli")
        print("2. Tilastot")
        print("3. Lopeta")
        valinta = input("Valitse toiminto (1-3): ")
        if valinta == "1":
            global aloitus_aika
            kysy_kentta()
            kentta_koko()   
            miinoita(tila["kentta"], ruudut["jaljella"], miina_maara)
            laske(tila["kentta"])
            aloitus_aika = time.time()
            main(tila["kentta"])
            break
        elif valinta == "2":
            tallennus = open("Tulokset.txt", "r")
            tilastot = tallennus.read()
            print(tilastot)
            tallennus.close

        elif valinta == "3":
            sys.exit()
        else:
            print("Valintaa ei ole olemassa")
            
def tee_tiedosto():
    """
    Tekee tiedoston johon tulokset tallennetaan jos sitä ei vielä ole olemassa.
    """
    tiedosto = open("Tulokset.txt", "a")
    
    return tiedosto
 
def tallenna_tilastot(tulos):
    """
    Tallentaa tulokset tiedostoon.
    """
    lopetus_aika = time.time()
    m, s = aika_muunnin(lopetus_aika)
    nyt = datetime.datetime.now()
    paiva = paiva_muunnin(nyt)
    tallennus = tee_tiedosto()
    tilasto = tallennus.write("{lopputulos}, Kesto: {minu:02} min {sek:02} sek, Ajankohta: {ak}, Kentän koko: {lev} x {kor}, Miinojen määrä: {lkm}\n".format(lopputulos=tulos, minu=int(m), sek=int(s), ak=paiva, lev=leveys, kor=korkeus, lkm=miina_maara))
    tallennus.close
   
def piirra_kentta():
    """ 
    Käsittelijäfunktio, joka piirtää kaksiulotteisena listana kuvatun miinakentän
    ruudut näkyviin peli-ikkunaan. Funktiota kutsutaan aina kun pelimoottori pyytää
    ruudun näkymän päivitystä.
    """
    teksti = ""
    x = "b"
    y = "K"
    haravasto.tyhjaa_ikkuna()
    haravasto.piirra_tausta()
    haravasto.piirra_tekstia(teksti, x, y, vari=(0, 0, 0, 255), fontti="serif", koko=32)
    haravasto.aloita_ruutujen_piirto()
    for i, lista in enumerate(tyhjio["tyhja"]):
        for j, jtn in enumerate(lista):
            haravasto.lisaa_piirrettava_ruutu(jtn, j*40, i*40)
    haravasto.piirra_ruudut()
            
    
def main(kentta):
    """
    Lataa pelin grafiikat, luo peli-ikkunan ja asettaa siihen piirto- ja hiirikäsittelijän.
    """
    korkeus = len(kentta) * 40
    leveys = len(kentta[0]) * 40
    haravasto.lataa_kuvat("spritet")
    haravasto.luo_ikkuna(leveys, korkeus)
    haravasto.aseta_piirto_kasittelija(piirra_kentta)
    haravasto.aseta_hiiri_kasittelija(kasittele_hiiri)
    haravasto.aloita()


if __name__ == "__main__":
    paavalikko()