# Depot Git
# OPENCLASSROOMS
### PARCOURS : Développeur d'application - Java
#### PROJET 7 : Développez le nouveau système d’information de la bibliothèque d’une grande ville
#### PROJET 10 : Améliorer le  système d’information de la bibliothèque
#### ETUDIANT : Frédéric Leroux

## Pre-requis
	java 1.8
	Apache tomcat 9.7
	PostgreSQL 11.3
	FakeSMTP 2.0 (fournis)
	EDI eclipse ou autre


### Contenu

- DOSSIER: P7_App :     ensemble du code source JEE.
- DOSSIER: P7_DB :      un dump de la DB en ".sql" & ".txt"  pour la création des roles et de la dite base 	                    modele physique de donné.
- DOSSIER P10_UpDateDB:  script sql de mise à jour
- DOSSIER P10_AppUpdate: update jar to 1.2
- DOSSIER: P7_DOC:       modele physique de donné.
- DOSSIER: P7_Fake_SMTP_Server: Fake-SMTP API
- DOSSIER: P7_Launch: fichiers .bat et .sh application launcher + jar
- DOSSIER: P10_Launch: fichiers .bat et .sh application launcher + jar
- DOOSIER p10_Postman_Tests: export des tests postman

# Installation

- Creer la base de donné via les script dans:
		/P7_DB
- Backup de la base via les script dans
		/P7_DB
- Lancer les jar
		Application: /P7_Launch/App
		Batch and Mailing: /P7_Launch\BatchAndMailing
- ou compiler via code source
		/P7_App

# Mise à jour
- stopper les service
- stopper la base de données
- mettre à jour la base de donné avec le script  /P10_UpdateDb/Ticket_1_Update_DB_v1.2.sql
- Lancer les jar
		Application: \P10_AppUpdate\P10_Launch.bat or .sh
- ou compiler via code source
		/P7_App

# Test Postman
- Utiliser le profil itService pour les test services
- Utiliser le profile it pour les test API
-
## Note
- pour les test mail and scheduling utilisé les services eureka et configserver en supplément
- pour les test API ainsi que mail fakeSMTP est nécésssaire(fournis)

URL acces:
	http://localhost:9005/

Note:

	Projet à vocation de présentation et de test, l'utilisation de fakeSMTP simule un serverSMTP utilisé dans l'application,
	un mail est envoyé toute les minutes concernant les retards.
