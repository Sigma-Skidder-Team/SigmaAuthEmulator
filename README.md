# SigmaAuthEmulator

## Usage
After launching for the first time, do this in a terminal (admin privilage):

> [!IMPORTANT]
> Make sure your JAVA_HOME is the same as what Sigma uses!
> You also need to run this tool as admin!

windows:
``keytool -import -file _wildcard.sigmaclient.info+2.pem -keystore "%JAVA_HOME%\jre\lib\security\cacerts" -alias "skidmaEmulatorCert" -storepass changeit``

linux:
``sudo keytool -import -file _wildcard.sigmaclient.info+2.pem -keystore $JAVA_HOME/jre/lib/security/cacerts -alias skidmaEmulatorCert -storepass changeit``