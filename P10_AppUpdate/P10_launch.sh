java -jar libraryEureka-1.2.jar &
java -jar libraryAPIGatewayZuul-1.2.jar &
java -jar libraryUi-1.2.jar &
java -jar libraryCustomers-1.2.jar &
java -jar libraryBookCase-1.2.jar &
java -jar libraryBookLoans-1.2.jar &
java -jar libraryBuildings-1.2.jar  &
java -jar LibraryConfigServer-1.2.jar &
java -jar LibraryScheduledBatchAndMailing-1.2.jar &
echo 'Starting app will take about 2 minutes and 30 seconds please standby'
sleep 150
echo 'app is normally launched exit know'
exit