@echo off
echo Starting Library App P7 Fle will take about 2 minute 30 secondes
start "P10_libraryEureka-0.0.1-SNAPSHOT" libraryEureka-0.0.1-SNAPSHOT.jar
echo  	Starting libraryEureka
start "P10_libraryAPIGatewayZuul-0.0.1-SNAPSHOT" libraryAPIGatewayZuul-0.0.1-SNAPSHOT.jar
echo  	Starting libraryAPIGatewayZuul
start "P10_libraryUi-0.0.1-SNAPSHOT" libraryUi-0.0.1-SNAPSHOT.jar
echo  	Starting libraryUi
start "P10_libraryCustomers-0.0.1-SNAPSHOT" libraryCustomers-0.0.1-SNAPSHOT.jar
echo  	Starting libraryCustomers
start "P10_libraryBookCase-0.0.1-SNAPSHOT" libraryBookCase-0.0.1-SNAPSHOT.jar
echo  	Starting libraryBookCase
start "P10_libraryBookLoans-0.0.1-SNAPSHOT" libraryBookLoans-0.0.1-SNAPSHOT.jar
echo  	Starting libraryBookLoans
start "P10libraryBuildings-0.0.1-SNAPSHOT" libraryBuildings-0.0.1-SNAPSHOT.jar
echo  	Starting libraryBuildings
echo Starting Library Batch and mailling P10 Fle ensure FakeSmtp is started and libraryEureka-0.0.1-SNAPSHOT is launched
echo  	Starting LibraryConfigServer
start "P10_LibraryConfigServer-0.0.1-SNAPSHOT" LibraryConfigServer-0.0.1-SNAPSHOT.jar
echo  	Starting LibraryScheduledBatchAndMailing
start "P10_LibraryScheduledBatchAndMailing-0.0.1-SNAPSHOT" LibraryScheduledBatchAndMailing-0.0.1-SNAPSHOT.jar
echo 		9/9 service stating wait the end of countdown before launch app
echo 			force quit will not garantee that app works immediatly"
timeout /t 150 /nobreak 




echo "if you see that an issue occure during start"

exit /b





