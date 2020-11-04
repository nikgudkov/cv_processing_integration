
Installation:

mvn clean package

java -jar target/ExtractionService-1.0.0.jar


Example curls:

curl http://localhost:8080/submit -u user:pass -F file=@petra.doc  

curl http://localhost:8080/retrieve/KSyN2SspQ1 -u user:pass

curl http://localhost:8080/accesstoken -u user:pass

curl http://localhost:8080/submit?access_token={accesstoken} -F file=@petra.doc

curl http://localhost:8080/retrieve/KSyN2SspQ1?access_token={accesstoken}

Postman sample request can be found in file postman_collection.json