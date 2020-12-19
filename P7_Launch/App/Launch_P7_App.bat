@echo off
echo Starting Library App P7 Fle will take about 2 minute 30 secondes
start "P7_libraryEureka-0.0.1-SNAPSHOT" libraryEureka-0.0.1-SNAPSHOT.jar
echo  	Starting libraryEureka
start "P7_libraryAPIGatewayZuul-0.0.1-SNAPSHOT" libraryAPIGatewayZuul-0.0.1-SNAPSHOT.jar
echo  	Starting libraryAPIGatewayZuul
start "P7_libraryUi-0.0.1-SNAPSHOT" libraryUi-0.0.1-SNAPSHOT.jar
echo  	Starting libraryUi
start "P7_libraryCustomers-0.0.1-SNAPSHOT" libraryCustomers-0.0.1-SNAPSHOT.jar
echo  	Starting libraryCustomers
start "P7_libraryBookCase-0.0.1-SNAPSHOT" libraryBookCase-0.0.1-SNAPSHOT.jar
echo  	Starting libraryBookCase
start "P7_libraryBookLoans-0.0.1-SNAPSHOT" libraryBookLoans-0.0.1-SNAPSHOT.jar
echo  	Starting libraryBookLoans
start "P7libraryBuildings-0.0.1-SNAPSHOT" libraryBuildings-0.0.1-SNAPSHOT.jar
echo  	Starting libraryBuildings
echo 		7/7 service stating wait the end of countdown before launch app
echo 			force quit will not garantee that app works immediatly"
timeout /t 150 /nobreak 




echo "if you see that an issue occure during start"

exit /b





