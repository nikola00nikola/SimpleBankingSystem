# Simple Banking Software System

Jednostavan softverski sistem za simuliranje rada jedne banke.

## Opis
[Klijentska aplikacija](https://github.com/nikola00nikola/SimpleBankingSystem/blob/main/klijent/src/klijent/Klijent.java) interaguje sa korisnikom preko konzole, šalje odgovarajuće http zahteve (RETROFIT api) centralnom serveru, i dobija odgovor, koji zatim prikazuje korisniku.

[Centralni Server](https://github.com/nikola00nikola/SimpleBankingSystem/tree/main/centralniserver/src/main/java/endpoints) je veb aplikacija(JAX-RS api). Samu obradu zahteva prepušta odgovarajucem podsistemu.

Komunikacija izmedju centalnog servera i odgovarajućih podsistema implementirana je koriscenjem JMS api-ja, komunikacija se odvija preko Glassfish servera(Resource: Message Queue).

Baze podataka su implementirane korišćenjem mySql, data je [skripta](https://github.com/nikola00nikola/SimpleBankingSystem/blob/main/init.sql) za kreiranje sema i tabela. Baza(šema) podsistem3 predstavlja back-up bazu za baze podsistem1 i podsistem2. Podsistemi su odgovorni za rad sa bazama. Interakcija svih podsistema sa bazom je implementirana koriscenjem JPA.

Uloga podsistema1 je rad sa tabelama Mesto, Filijala i Komitent. Funkcije koje implementira ovaj podsistem date su u [Main.java](https://github.com/nikola00nikola/SimpleBankingSystem/blob/main/podsistem1/src/java/podsistem1/Main.java) fajlu.

Uloga podsistema2 je rad sa tabelama Racun, Transakcija i Ucestvuje. Funkcije koje implementira ovaj podsistem date su u [Main.java](https://github.com/nikola00nikola/SimpleBankingSystem/blob/main/podsistem2/src/java/podsistem2/Main.java) fajlu.

Uloga podsistema3 je automatski back-up svih podataka na zadati vremenski period. Pored toga, druga nit obradjuje jos 2 zahteva:
- dohvatanje svih podataka iz back-up baze podataka
- dohvatanje razlike u podacima izmedju regularne i back-up baze podataka
