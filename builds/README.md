mvn clean install -Dmaven.test.skip=true -o \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/idea/tc-util-project/target/tc-util-1.0.25.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.25 -Dpackaging=jar -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/idea/foss-qdp-project-v4/lib \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/idea/tc-util-project/target/tc-util-1.0.25-sources.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.25 -Dpackaging=jar -Dclassifier=sources -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/idea/foss-qdp-project-v4/lib \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/idea/tc-util-project/target/tc-util-1.0.25.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.25 -Dpackaging=jar -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/idea/tc-util-project/builds \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/idea/tc-util-project/target/tc-util-1.0.25-sources.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.25 -Dpackaging=jar -Dclassifier=sources -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/idea/tc-util-project/builds \
&& \
rm -rf /Users/zhaochen/dev/workspace/idea/tc-util-project/1.0.25


