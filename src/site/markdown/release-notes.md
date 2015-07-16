# Release Notes

Software release notes convey two important things:

1. They explain what changes, deficiencies and defects were addressed in the release.
2. They inform the reader about installation and deployment.

The audience for release notes can be testers, support staff and management.

Public release notes should contain at least:

* Product Name
* Version Number (Be consistent with major.minor.revision numbering in your releases, see [semantic versioning](semantic-versioning) for more details).
* release, buildnumber
* all fixed public bugs
* all added public features
* Release Date (name, version, and release date often repeated in the document footer)
* Type of Release (e.g. "This is a developer release for internal evaluation only." or "This is a public production release.")
* Major announcement (Not often used, but important when you need to note "This release corrects a major flaw in XYZ from the previous release. Please upgrade as soon as possible.")
* What's New (A List of major enhancements visible to the user, in user-friendly terms, e.g. "increased response time" versus "manipulated line 9 of widget #745 to change algorithm")
* Installation Instructions (Often is a list of system requirements with a link to a full installation document, but if there are compatibility issues or deviations from previous version instructions, note them.)
* Headings for: Additions, Removals, Changes, Bugfixes (The first three mean additions, removals, changes to functionality; the latter means fixes of anything. The info under each heading can come right out of your bug-tracking system and should be simple bullet points like "FIX ### Title_of_Bug". This assumes tickets are given meaningful titles. Bonus if you can link users to the bug-tracking system so they can see the entire ticket and commentary.)
* Known Issues (List of open bugs slated for next release, or prose describing known issues; this depends on your audience and what the company wants to make transparent, and how.)
* Additional Documentation (links to user guide, administrator's guide, other relevant documents)

The release notes should be published in plain text or at the very least html.

## Generate release notes from JIRA

With the [maven changes plugin](http://maven.apache.org/plugins/maven-changes-plugin/) one can generate release notes from JIRA tasks and also mail it to a list of email addresses.

To generate the release notes with the `maven changes plugin` you should include the following snippet into your `pom.xml`

    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-changes-plugin</artifactId>
      <version>${maven-changes-plugin.version}</version>
      <executions>
        <execution>
          <id>generate-release-notes</id>
          <phase>generate-resources</phase>
          <goals>
            <goal>announcement-generate</goal>
          </goals>
          <configuration>
            <!-- This will generate release-notes.txt file under META-INF folder within the built artifact: -->
            <announcementFile>release-notes.txt</announcementFile>
            <announcementDirectory>${project.build.outputDirectory}/META-INF</announcementDirectory>
          </configuration>
        </execution>
      </executions>
      <configuration>
        <issueManagementSystems>
          <!-- generate announcement based on both `changes.xml` and JIRA -->
          <issueManagementSystem>changes.xml</issueManagementSystem>
          <issueManagementSystem>JIRA</issueManagementSystem>
        </issueManagementSystems>

        <smtpHost>mail.yourhost.com</smtpHost>
        <smtpPort implementation="java.lang.Integer">25</smtpPort>

        <!-- Specifies the sender of release notes -->
        <mailSender>
          <name>Release Notification</name>
          <email>build@example.com</email>
        </mailSender>

        <!-- Recipient email addresses -->
        <toAddresses>
          <toAddress implementation="java.lang.String">to@example.com</toAddress>
        </toAddresses>

        <!-- Use the JIRA query language -->
        <useJql>true</useJql>

        <!--
        Take the version from the project's POM and match it against the
        Fix-For version of the JIRA issues. The names of your versions in JIRA
        must match the ones you use in your POM. The -SNAPSHOT part of the
        version in your POM is handled automatically by the plugin, so you
        don't need to include -SNAPSHOT in the names of your versions in JIRA.
        <onlyCurrentVersion>true</onlyCurrentVersion>
        -->
        <!--
         IDs of version(s) you want to include in the announcements
         These are JIRA's internal version IDs, NOT the human readable display
         ones. Multiple version(s) can be seperated by commas. If this is set
         to empty, that means all fix versions will be included.
        -->
        <fixVersionIds>11412</fixVersionIds>

        <!-- generate announcements only at the top of module tree -->
        <runOnlyAtExecutionRoot>true</runOnlyAtExecutionRoot>

        <!-- only include JIRA issues with resolution IDs Fixed and Done -->
        <resolutionIds>Fixed,Done</resolutionIds>
        <!-- <statusIds>Closed,Resolved,QA</statusIds> -->

        <!-- Columns included within the report -->
        <columnNames>Type,Key,Summary,Priority,Status,Resolution,Fix</columnNames>
        <sortColumnNames>Key ASC</sortColumnNames>

        <!-- Defines credentials for JIRA webserver -->
        <webUser>myuser</webUser>
        <webPassword>mypassword</webPassword>

      </configuration>
    </plugin>


To include links to the issues in your issue management system you have to enter the type of issue management system (see [Usage maven changes plugin](http://maven.apache.org/plugins/maven-changes-plugin/usage.html) for list of pre-configured issue management systems) and the URL to it:

    <project>
      ...
      <issueManagement>
        <system>JIRA</system>
        <url>http://jira.company.com/</url>
      </issueManagement>
      ...
    </project>

**Note:** Make sure that your `<issueManagement>/<url>` is correct. In particular, make sure that it has a trailing slash if it needs one.

To generate the release notes run this command:

    mvn changes:jira-report

## Related patterns

* [Semantic versioning](semantic-versioning)

# See also:

* [Release notes from JIRA](http://kaviddiss.com/2014/10/28/release_notes_from_jira/)