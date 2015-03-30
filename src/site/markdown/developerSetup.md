# Developer setup guide

These instructions are intended to assist the would-be developer with setting up an environment in which to successfully build the source code.

## Required tools

* Maven
* GPG

## Details
    
### Installation

1. [Download and Install Maven](http://maven.apache.org/download.cgi) - Download the latest version of Maven and install it

2. Add username/password github account to `settings.xml` maven.
   ```
   <servers>
      <server>
        <id>github</id>
        <username>GitHubLogin</username>
        <password>GitHubPassw0rd</password>
      </server>
    </servers>
   ```
3. Add username/password `ossrh` account to `settings.xml` maven.
   ```
    <settings>
      <servers>
        <server>
          <id>ossrh</id>
          <username>your-jira-id</username>
          <password>your-jira-pwd</password>
        </server>
      </servers>
    </settings>
    ```
4. [Download GPG](http://www.gnupg.org/download/) - Download GPG and follow the instructions and install it to your system. Verify your gpg installation by running gpg with the version flag `gpg --version`
5. Generate key pair - Before you do anything with GPG, you will need to generate a key pair for yourself.   Once you have you own key pair, you can use your private key to sign artifacts, and distribute your public key to public key servers and end-users so that they can validate artifacts signed with your private key. Generate a key pair like this `gpg --gen-key`
You’ll be asked for the type, the size, and the time of validity for the key, just use the default value if you don’t have any special requirements.    You will be asked to input your name, email, and comment for the key.   These identifiers are essential as they will be seen by anyone downloading a software artifact and validating a signatute.  Finally, you can provide a passphase to protect your secret key, this is not mandatory, but I highly recommend you to do this.   It is essential that you choose a secure passphrase and that you do not divulge it to any one.   This passphrase and your private key are all that is needed to sign artifacts with your signature.
6. Add GPG Passphrase to `settings.xml`:
    ```
    <settings>
      <profiles>
        <profile>
          <id>release</id>
          <activation>
            <property>
              <name>performRelease</name>
              <value>true</value>
            </property>
          </activation>
          <properties>
            <gpg.passphrase>the_pass_phrase</gpg.passphrase>
          </properties>
        </profile>
      </profiles>
    </settings>
    ```
7. Distribute Your Public Key - Since other people need your public key to verify your files, you have to distribute your public key to a key server: `gpg --keyserver <key server url> --send-keys <key id>`
You can get your `keyid` by listing the public keys (`gpg --list-keys`). The line starting with pub shows the length, the keyid, and the creation date of the public key.

### Build

Once the above setup is complete:

1. Open a fresh terminal
2. Navigate to the `${project.name} directory, or whatever you called it.
3. Type the following on the command line: `mvn clean install`
    
### Snapshot deployment

Snapshot deployment are performed when your version ends in -SNAPSHOT . When performing snapshot deployments simply run `mvn clean deploy` on your project.

SNAPSHOT versions are not synchronized to the Central Repository. If you wish your users to consume your SNAPSHOT versions, they would need to add the snapshot repository to their Nexus, `settings.xml`, or `pom.xml`. Successfully deployed SNAPSHOT versions will be found in `https://oss.sonatype.org/content/repositories/snapshots/`.

### Release deployment

You can perform a release deployment to OSSRH with `mvn release:clean release:prepare` by answering the prompts for versions and tags, followed by `mvn release:perform`.

This execution will deploy to OSSRH and release to the Central Repository in one go, thanks to the usage of the Nexus Staging Maven Plugin with `autoReleaseAfterClose` set to `true`.