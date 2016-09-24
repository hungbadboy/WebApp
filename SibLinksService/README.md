# Install local maven
mvn install:install-file -Dfile=javathumbnailer-standalone-0.7-PREVIEW.jar -DgroupId=local.javathumbnailer -DartifactId=javathumbnailer -Dversion=0.7 -Dpackaging=jar

#How to clean data in server

ssh -i Siblinks_keypair_new.pem ec2-user@52.34.250.166

In home folder of ec2-user
./clean.sh

Input password
P@ssw0rd

#How to clean data in dev

ssh -i demo.pem ec2-user@54.200.200.106

In home folder of ec2-user
./clean.sh

Input password
P@ssw0rd