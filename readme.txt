
Submitted Files:
readme.txt
Hw3.java
populate.java
createdb.sql
dropdb.sql
map.JPG
ojdbc6.jar

Please follow the following instruction to test my project, thanks


Please copy *.xy file and sdoapi.zip file to /Hw3
1. run createdb.sql on sqlplus to create table
2. populate data into DB
   2.1 cd to /Hw3
   2.2 javac populate.java
   2.3 java -cp ".:ojdbc6.jar" populate photo.xy building.xy photographer.xy
   2.4 wait till the data populating is done.
3. Open Hw3 swing GUI and test project
   3.1 under /Hw3
   3.2 javac -cp ".:ojdbc6.jar:sdoapi.jar" Hw3.java
   3.3 java -cp ".:ojdbc6.jar:sdoapi.jar" Hw3

Notes:
   1. when drawing Range, after first left click, please move cursor to another place and click the second time,
      you will see a red line was drew between the first click and second click location
   2. For Find photo and Find Photographers, after you click any of these radiobuton,
      Please wait till all the features are fully retrieved from DB and drawing are Done, then do further test.
      It could be slow due to the db connection or network, please be patient.


