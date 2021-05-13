@echo off
echo Starting Library App P7 Fle will take about 2 minute 30 secondes
start "P10_libraryEureka-1.2" libraryEureka-1.2.jar
echo  	Starting libraryEureka
start "P10_libraryAPIGatewayZuul-1.2" libraryAPIGatewayZuul-1.2.jar
echo  	Starting libraryAPIGatewayZuul
start "P10_libraryUi-1.2" libraryUi-1.2.jar
echo  	Starting libraryUi
start "P10_libraryCustomers-1.2" libraryCustomers-1.2.jar
echo  	Starting libraryCustomers
start "P10_libraryBookCase-1.2" libraryBookCase-1.2.jar
echo  	Starting libraryBookCase
start "P10_libraryBookLoans-1.2" libraryBookLoans-1.2.jar
echo  	Starting libraryBookLoans
start "P10libraryBuildings-1.2" libraryBuildings-1.2.jar
echo  	Starting libraryBuildings
echo Starting Library Batch and mailling P10 Fle ensure FakeSmtp is started and libraryEureka-1.2 is launched
echo  	Starting LibraryConfigServer
start "P10_LibraryConfigServer-1.2" LibraryConfigServer-1.2.jar
echo  	Starting LibraryScheduledBatchAndMailing
start "P10_LibraryScheduledBatchAndMailing-1.2" LibraryScheduledBatchAndMailing-1.2.jar
echo 		9/9 service stating wait the end of countdown before launch app
echo 			force quit will not garantee that app works immediatly"
timeout /t 150 /nobreak




echo "if you see that an issue occure during start"

exit /b
