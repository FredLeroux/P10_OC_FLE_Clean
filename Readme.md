# Depot Git
# OPENCLASSROOMS
### PARCOURS : D�veloppeur d'application - Java
#### PROJET 7 : D�veloppez le nouveau syst�me d�information de la biblioth�que d�une grande ville
#### PROJET 10 : Am�liorer le  syst�me d�information de la biblioth�que
#### ETUDIANT : Fr�d�ric Leroux

## Pre-requis
	java 1.8
	Apache tomcat 9.7
	PostgreSQL 11.3
	FakeSMTP 2.0 (fournis)
	EDI eclipse ou autre


### Contenu

- DOSSIER: P7_App :     ensemble du code source JEE.
- DOSSIER: P7_DB :      un dump de la DB en ".sql" & ".txt"  pour la cr�ation des roles et de la dite base 	                    modele physique de donn�.
- DOSSIER P10_UpDateDB:  script sql de mise � jour
- DOSSIER P10_AppUpdate: update jar to 1.2
- DOSSIER: P7_DOC:       modele physique de donn�.
- DOSSIER: P7_Fake_SMTP_Server: Fake-SMTP API
- DOSSIER: P7_Launch: fichiers .bat et .sh application launcher + jar
- DOSSIER: P10_Launch: fichiers .bat et .sh application launcher + jar
- DOOSIER p10_Postman_Tests: export des tests postman

# Installation

- Creer la base de donn� via les script dans:
		/P7_DB
- Backup de la base via les script dans
		/P7_DB
- Lancer les jar
		Application: /P7_Launch/App
		Batch and Mailing: /P7_Launch\BatchAndMailing
- ou compiler via code source
		/P7_App

# Mise � jour
- stopper les service
- stopper la base de donn�es
- mettre � jour la base de donn� avec le script  /P10_UpdateDb/Ticket_1_Update_DB_v1.2.sql
- Lancer les jar
		Application: /P10_Launch
- ou compiler via code source
		/P7_App

# Test Postman
- Utiliser le profil itService pour les test services
- Utiliser le profile it pour les test API
-
## Note
- pour les test mail and scheduling utilis� les services eureka et configserver en suppl�ment
- pour les test API ainsi que mail fakeSMTP est n�c�sssaire(fournis)

URL acces:
	http://localhost:9005/

Note:

	Projet � vocation de pr�sentation et de test, l'utilisation de fakeSMTP simule un serverSMTP utilis� dans l'application,
	un mail est envoy� toute les minutes concernant les retards.