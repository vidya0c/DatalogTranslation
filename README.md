# DatalogTranslation
This project is written to implement translation of RPQs to Datalog rules using VLog open source Datalog engine using Java language.

# Steps to run Vlog program:
1) Download the Vlog source from the github https://github.com/knowsys/rulewerk/tree/master/rulewerk-examples 
2) Download Eclipse from https://www.eclipse.org/downloads/
3) Download Java from https://java.com/en/download/
4) Import the Vlog project into Eclipse IDE and copy the files in Src folder.
5) Download the below files of YAGO dataset from https://www.mpi-inf.mpg.de/departments/databases-and-information-systems/research/yago-naga/yago/downloads (yagoLiteralFacts.tsv, yagoFacts.tsv, yagoDateFacts.tsv)
6) From the Src folder, run SampleFileReader.java to get csv files.
7) Then run the ParsingClass.java with the RPQ_script.txt to get the datalog translation results.

# Steps to evaluate on POSTgreSQL:
1) Download POSTgreSQL from https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
2) Set up through the installer and localhost browser http://127.0.0.1:60626/browser/ to see the database.
3) Create a new database and under Tools option, click query tool 
4) Run the POSTgreSQl queries from the folder "PostgreSQL statements"
5) Once database is ready, run the "PostgreSQL queries" one by one in the Query tool.

# Steps to evaluate on Neo4j:
1) Download Neo4j from https://neo4j.com/download/
2) Set up using the installer.
3) Create a local database using a password and Start the graph database connection.
4) Open Neo4j browser.
5) In the command line, run the query from "Neo4j queries" one by one.

