insert into artikels (naam, aankoopprijs, verkoopprijs, soort, houdbaarheid, garantie, artikelgroepid)
values ('test', 1, 1, 'F', 0, 0, (select id from artikelgroepen where naam ='test'));
insert into kortingen (artikelid, vanafAantal, percentage)
values ((select id from artikels where naam ='test'), 1, 1);
insert into artikels (naam, aankoopprijs, verkoopprijs, soort, houdbaarheid, artikelgroepid)
values ('testFoodArtikel', 1, 2, 'F', 69, (select id from artikelgroepen where naam ='test'));
insert into artikels (naam, aankoopprijs, verkoopprijs, soort, garantie, artikelgroepid)
values ('testNonFoodArtikel', 1, 2, 'NF', 420, (select id from artikelgroepen where naam='test'));