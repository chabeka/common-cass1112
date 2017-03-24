// --------------------------------
// Utilisation de ce projet exemple
// --------------------------------

- installation de ActiveMQ (http://activemq.apache.org/)
	- download du binaire ActiveMQ (http://activemq.apache.org/download.html)
	  NB: ce projet a été réalisé avec la version 5.4.2 
	- lancer le serveur \bin\activemq.bat
	- afficher la console d'admin : http://localhost:8161/admin/

- installation de ant (http://ant.apache.org)
    - téléchargement du binaire http://ant.apache.org/bindownload.cgi
    - ajouter le path du répertoire bin dans la variable d'environnement système "PATH"
    - tester que la commande "ant" soit bien comprise telle quelle dans un invite de commandes

- ouvrir un invite de commande à la racine du projet

- Actions possibles
	
	
	- Production
	
	     - Envoi d'un message sur la queue "account-test"
	       Commande : ant send -Dargs="<prénom> <nom>"
	
	     - Publication d'un message sur le topic "account-test"
	       Commande : ant publish -Dargs="<prénom> <nom>"
	
	
	- Consommation 
		  
		  - Consommation d'un message en synchrone sur la queue "account-test"
		    Le programme s'arrête quand le message est consommé.
		    Commande : ant receiveS

        - Consommation d'un message en asynchrone sur la queue "account-test"
          Le programme s'arrête au bout du temps d'attente en secondes indiqué dans la commande
          Commande : ant receive -Dargs="<temps d'attente en secondes>"
        
        - Abonnement au topic "account-test"
          Le programme s'arrête au bout du temps d'attente en secondes indiqué dans la commande
          Commande : ant subscribe -Dargs="<temps d'attente en secondes>"
          
