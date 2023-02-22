#include <stdio.h>
#include <ctype.h>

void valikko(void);
int kysytoiminto(void);
int rahasummat(int saldo, int pin, int tilinumero);
int asiakkaansaldo(int tilinumero);
void jaasetelit(int summa);
int kysytili(void);
int kysypin(int tilinumero);
void muokkaatiedostoa(int pin, int tilinumero, int saldo);
void onkonumero(int numero);
void puhdista(void);



    /* PÄÄOHJELMA */

int main(void){
    int summa = 0;
    int valinta = 0;
    int saldo;
    int tilinumero;
    int pin;

    printf("Tervetuloa pankkiautomaatille\n");

    tilinumero = kysytili();
    if (tilinumero != 0){
        pin = kysypin(tilinumero);
    }

    do {
        if (tilinumero == 0)
            break;
        if(pin == 1)
            break;

        valikko();
        valinta = kysytoiminto();
        summa = 0;
        switch(valinta){
            case 1:
                saldo = asiakkaansaldo(tilinumero);
                summa = rahasummat(saldo, pin, tilinumero);
                break;
            case 2:
                saldo = asiakkaansaldo(tilinumero);
                printf("Tilin saldo on %d euroa\n", saldo);
                printf("Paina enter jatkaaksesi");
                while (getchar() != '\n');
                getchar();
                break;

            case 3:
                break;
        }
    }while(valinta == 2 || saldo < 0 || summa == 10);

    printf("Tervetuloa uudelleen");
}

/*Funktio, joka tulostaa valikon */

    void valikko(void){
        printf("\nValitse toiminto\n");
        printf("1. Rahan nosto\n");
        printf("2. Saldo\n");
        printf("3. Lopeta\n");
}

/*Funktio, joka kysyy käyttäjän valinnan*/

        int kysytoiminto(void){
            int valinta;

            printf("Valintasi: ");
            scanf("%d", &valinta);

            while (valinta < 1 || valinta > 3){
                onkonumero(valinta);
                printf("\nValinta pitää olla kokonaisluku 1, 2 tai 3 \n");
                printf("Valintasi: ");
                scanf("%d", &valinta);
            }
        return valinta;
    }
/*Funktio, joka pyytää käyttäjää valitsemaan nostettavan summan */

        int rahasummat(int saldo, int pin, int tilinumero){

            int nosto = 0;
            int valinta;
            while (nosto == 0 || nosto > saldo){
                printf("1. 20e\n");
                printf("2. 40e\n");
                printf("3. 60e\n");
                printf("4. 90e\n");
                printf("5. 140e\n");
                printf("6. 240e\n");
                printf("7. Muu summa\n");
                printf("8. Alkuun\n");

                printf("Valitse summaa vastaava luku > ");
                scanf("%d", &valinta);

                switch(valinta){

                    case 1:
                        nosto = 20;
                        break;
                    case 2:
                        nosto = 40;
                        break;
                    case 3:
                        nosto = 60;
                        break;
                    case 4:
                        nosto = 90;
                        break;
                    case 5:
                        nosto = 140;
                        break;
                    case 6:
                        nosto = 240;
                        break;
                    case 7:
                        printf("Voit nostaa 20 - 1000 valilla\n");
                        printf("Nostettavat summat ovat 20e, 40e tai enemman kymmenen euron valein\n");
                        printf("Kirjoita summa > ");
                        scanf("%d", &nosto);
                        if (nosto < 20 || nosto > 1000){
                            printf("Viallinen summa\n");
                            onkonumero(nosto);
                            nosto = 0;
                            }
                        if (nosto % 10 != 0 || nosto == 30){
                            printf("Viallinen summa, automaatista saa vain 20e ja 50e seteleita\n");
                            printf("Summa voi olla vain tasakymmenen\n");
                            nosto = 0;
                            }

                        break;
                    case 8:
                        nosto = 10;
                        break;

                    default:
                        printf("Vaihtoehtoa ei ole\n");
                        onkonumero(valinta);
                        break;

                }
                if (nosto > saldo){
                    printf("Ei tarpeeksi saldoa\n");
                    nosto = 0;
                    }
                if (nosto >= 20){
                    printf("\nNostit %d euroa\n", nosto);
                    printf("Ota rahat\n");
                    jaasetelit(nosto);
                    saldo = saldo - nosto;
                    printf("Tilin saldo on %d euroa\n", saldo);
                    muokkaatiedostoa(pin, tilinumero, saldo);
                    break;
                }
            }
            return nosto;
        }


/* Funktio, joka käy asiakkaan tiedoista hakemassa tilin saldon */

        int asiakkaansaldo(int tilinumero){
            FILE *fp;
            char tiedosto[10];
            int saldo;


            sprintf(tiedosto, "%d.tili.txt", tilinumero);

            fp = fopen(tiedosto, "r+");

            fscanf(fp, "%d", &saldo);
            fscanf(fp, "%d", &saldo);
            fclose(fp);

            return(saldo);

            }

/* Funktio, joka jakaa halutun noston seteleiksi */

        void jaasetelit(int summa){
            int viisikymppiset;
            int jaannos;
            int kaksikymppiset;

            jaannos = summa % 50;

            switch(jaannos){

                case 0:
                    kaksikymppiset = 0;
                    viisikymppiset = summa / 50;
                    break;

                case 10:
                    kaksikymppiset = 3;
                    viisikymppiset = (summa - 60) / 50;
                    break;
                case 20:
                    kaksikymppiset = 1;
                    viisikymppiset = (summa - 20) / 50;
                    break;
                case 30:
                    kaksikymppiset = 4;
                    viisikymppiset = (summa - 80) / 50;
                    break;
                case 40:
                    kaksikymppiset = 2;
                    viisikymppiset = (summa - 40) / 50;
                    break;
            }
            if (kaksikymppiset == 0)
                printf("%d x 50e\n", viisikymppiset);
            if (viisikymppiset > 0 && kaksikymppiset > 0)
                printf("%d x 50e ja %d x 20e\n", viisikymppiset, kaksikymppiset);
            if (viisikymppiset <= 0)
                printf("%d x 20e\n", kaksikymppiset);

        }

        int kysytili(void){

            FILE *fp;
            int numero;
            char tiedosto[10];
            int tilipalautus;

            printf("Anna tilinumero > ");
            scanf("%d", &numero);
            tilipalautus = numero;

            sprintf(tiedosto, "%d.tili.txt", numero);

            fp = fopen(tiedosto, "r");

            if (fp == NULL){
                printf("Tilia ei loydy\n");
                return(0);
                }
            else{
                return(tilipalautus);

                }

            fclose(fp);
        }

    int kysypin(int tilinumero){

        FILE *fp;
        char tiedosto[10];
        int pin;
        int tunnusluku;
        int luku = 3;

        sprintf(tiedosto, "%d.tili.txt", tilinumero);
        fp = fopen(tiedosto, "r");
        fscanf(fp, "%d", &pin);


        printf("Anna tunnusluku > ");
        scanf("%d", &tunnusluku);
        while(luku != 1 && pin != tunnusluku){
                onkonumero(tunnusluku);
                printf("Vaara tunnusluku\n");
                luku--;
                printf("Yrita uudelleen (%d yritysta jaljella) > ", luku);
                scanf("%d", &tunnusluku);
        }
        if (luku == 1 && pin != tunnusluku){
            printf("Vaarin kolmesti lopetetaan ohjelman kaytto\n");
            return (luku);
            }
         return(pin);
         fclose(fp);

    }

    void muokkaatiedostoa(int pin, int tilinumero, int saldo){

        FILE *fp;
        char tiedosto[10];

        sprintf(tiedosto, "%d.tili.txt", tilinumero);
        fp = fopen(tiedosto, "w");

        fprintf(fp, "%d\n", pin);
        fprintf(fp, "%d\n", saldo);


    }

    void onkonumero(int numero){

        int palautus;


       palautus = isdigit(numero);

        if(palautus == 0){
            puhdista();

            }
    }

    void puhdista(void){

        while (fgetc(stdin) != '\n');

    }



