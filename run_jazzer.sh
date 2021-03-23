cd src
$JAVA_HOME/bin/javac ch/randelshofer/math/FastDoubleParser.java
$JAVA_HOME/bin/javac -cp .:../jazzer_api_deploy.jar ch/randelshofer/Fuzzer.java

cd ..
./jazzer --cp=src --target_class=ch.randelshofer.Fuzzer
