@echo off
echo Starting Library Batch and mailling P7 Fle ensure FakeSmtp is started and libraryEureka-0.0.1-SNAPSHOT is launched
echo  	Starting LibraryConfigServer
start "P7_LibraryConfigServer-0.0.1-SNAPSHOT" LibraryConfigServer-0.0.1-SNAPSHOT.jar
echo  	Starting LibraryScheduledBatchAndMailing
start "P7_LibraryScheduledBatchAndMailing-0.0.1-SNAPSHOT" LibraryScheduledBatchAndMailing-0.0.1-SNAPSHOT.jar
echo 		2/2 service starting wait the end of countdown before launch app
echo 			force quit will not garantee that app works immediatly"
timeout /t 60 /nobreak 




echo "if you see that an issue occure during start"

exit /b





