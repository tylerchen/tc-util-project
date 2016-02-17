mvn clean install -Dmaven.test.skip=true \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/cocoa/tc-util-project/target/tc-util-1.0.5.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.5 -Dpackaging=jar -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/lib \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/cocoa/tc-util-project/target/tc-util-1.0.5-sources.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.5 -Dpackaging=jar -Dclassifier=sources -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/lib \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/cocoa/tc-util-project/target/tc-util-1.0.5.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.5 -Dpackaging=jar -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/cocoa/tc-util-project/builds \
&& \
mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=/Users/zhaochen/dev/workspace/cocoa/tc-util-project/target/tc-util-1.0.5-sources.jar -DgroupId=org.iff -DartifactId=tc-util-project -Dversion=1.0.5 -Dpackaging=jar -Dclassifier=sources -DlocalRepositoryPath=/Users/zhaochen/dev/workspace/cocoa/tc-util-project/builds \
&& \
rm -rf ~/dev/soft/maven/repo/org/iff/tc-util-project/1.0.5


