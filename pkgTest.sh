
#!/bin/sh

version=0.1

rm -rf finder-*

mvn clean appassembler:assemble package -DskipTests


unzip finder-0.1.zip

search

