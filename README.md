# Webshop Test Automation

## Overview
Projektni zadatak je automatizacija testiranja web stranice (AutomationPractice). Automatizirano je end-to-end testiranje različitih funkcionalnosti kao što su registracija korisnika, pregled proizvoda, upravljanje košaricom, plaćanje i potvrda o narudžbi.

## Korištene tehnologije
- **Java**: Glavni programski jezik
- **JavaScript**: Korišteno za interakciju s dinamičkim web elementima
- **Selenium WebDriver**: Framework za web automatizaciju (version 4.26.0)
- **TestNG**: Testing framework za organizaciju i izvršavanje testova (version 7.10.2)

## Features
Scenarij za izradu automatiziranog testa:

1.	Otvori stranicu http://www.automationpractice.pl/
2.	Otvori Sign in
3.	Registriraj se
4.	Upiši podatke o osobi na način da svaki put kada se ponovno pusti ova skripta upisuje nove podatke (ime, prezime, email, lozinka, datum rođenja) koji se spremaju kako bi se mogli    ponovo upotrijebiti tijekom testa
5.	Otvori Women izbornik
6.	Sort by: Product name: A-Z
7.	Filtriraj po boji: Crna
8.	Namjesti Price range $20 - $30 
9.	Otvori proizvod Blouse
10.	Promjeni Size na L
11.	Add to cart
12.	Provjeri jel se pojavila poruka o uspješnom dodavanju proizvoda u košaricu 
13.	Continue shopping
14.	Otvori Contact us na vrhu stranice
15.	Ispuni podatke (dodaj file, u poruci ispiši podatke o osobi koja je registrirana - ime, prezime, email, datum rođenja) i pošalji te provjeri poruku o uspješnom slanju
16.	Otvori svoj račun
17.	Provjeri svoje podatke (ime, prezime, email, datum rođenja) 
18.	Otvori košaricu i nastavi do plaćanja
19.	Otvori Order history
20.	Preuzmi PDF dokument o narudžbi
21.	Ispiši Test report u aplikaciji sa podacima o osobi (ime, prezime, email, lozinka, datum rođenja) i podacima o narudžđbi (broj narudžbe, datum, cijena)

## Reporting
- HTML izvještaji se generiraju u direktoriju test-reports/
- PDF-ovi narudžbi se generiraju u direktoriju invoice/
- Konzolni logovi pružaju status izvođenja u stvarnom vremenu

## Demo
`demo` folder sadrži .mp4 datoteku koja prikazuje demo snimku prve verzije ovog projekta.
Link na video: https://github.com/CrnkovicIvona/automation-practice/tree/master/demo 

## Licenca
Ovaj projekt je licenciran pod MIT licencom - pogledajte datoteku LICENSE za detalje.

## Autor
Ivona Crnković
