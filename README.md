# DatalogTranslation
This project is written to implement translation of RPQs to Datalog rules using VLog open source Datalog engine using Java language.

# Steps to run Vlog program:
1) Download the Vlog source from the github https://github.com/knowsys/rulewerk/tree/master/rulewerk-examples 
2) Download Eclipse from https://www.eclipse.org/downloads/
3) Download Java from https://java.com/en/download/
4) Import the Vlog project into Eclipse IDE and copy the files "ParsingClass.java" & "SampleFileReader.java" from Src folder to /vlog4j-examples/src/main/java/org/semanticweb/vlog4j/examples/core/
5) Download the below files of YAGO dataset from https://www.mpi-inf.mpg.de/departments/databases-and-information-systems/research/yago-naga/yago/downloads (yagoLiteralFacts.tsv, yagoFacts.tsv, yagoDateFacts.tsv)
6) Run SampleFileReader.java to get csv files.
7) Then run the ParsingClass.java with the VLog RPQs to get the datalog translation results.
8) Repeat the same for VLog CRPQs.

# Steps to evaluate on POSTgreSQL:
1) Download POSTgreSQL from https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
2) Set up through the installer and localhost browser http://127.0.0.1:60626/browser/ to see the database.
3) Create a new database and under Tools option, click query tool 
4) Run the POSTgreSQl queries from the folder "PostgreSQL statements"
5) Once database is ready, run the "PostgreSQL RPQs" one by one in the Query tool.
6) Repeat the same for "PostgreSQL CRPQs".

# Steps to evaluate on Neo4j:
1) Download Neo4j from https://neo4j.com/download/
2) Set up using the installer.
3) Create a local database using a password and Start the graph database connection.
4) Copy the fact csv files which was generated from SampleFileReader.java to the "import" folder of graph database.
5) Open Neo4j browser.
6) In the browser setting, set connection timeout to 30 minutes.
7) Edit the neo4j.config file and set the dbms.memory.heap.max_size = 5GB and dbms.memory.heap.initial_size=4GB and dbms.memory.pagecache.size=2GB.
8) In the command line, run the query from "Neo4j RPQs" one by one.
9) Repeat the same for "Neo4j CRPQs".
