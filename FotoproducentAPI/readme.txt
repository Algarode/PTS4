DBM dbManager = new DBM();


//Iets opslaan

Account ac = new Account();
ac.setUsername("Team");
ac.setPassword("Eduardo");

dbManager.save(ac);



//Iets wijzigen

Account ac = dbManager.findById(Account.class, 1); 
/*Eerste parameter is in welke table die moet zoeken,
	Aangezien elke table een class is moet je dus class doorgeven
	Tweede param is de Id
	*/

dbManager.update(ac);


//Iets verwijderen waarvan je de entiteit al hebt

dbManager.delete(ac);


//Iets verwijderen 	 doormiddel van id mee te geven

dbManager.deleteById(Account.class, 1);

	
