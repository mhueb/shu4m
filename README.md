# Maven Plugins

## Generate a version class from maven pom

	<plugin>
		<groupId>org.shu4m</groupId>
		<artifactId>shu4m-maven-plugin</artifactId>
		<version>1.0-SNAPSHOT</version>
		<executions>
			<execution>
				<phase>generate-sources</phase>
				<goals>
					<goal>generate-versionclass</goal>
				</goals>
				<configuration>
					<!-- Optional Parameters -->
					<packageName>${project.GroupId}.${version.packagename}.misc</packageName>
				</configuration>
			</execution>
		</executions>
	</plugin>
	
## Manipulate project version

1. Set a new Build number:

	mvn org.shu4m:shu4m-maven-plugin:setbuildnumber -Dbuildnumber=4711

2. Increment build number by one:

	mvn org.shu4m:shu4m-maven-plugin:nextbuildnumber
	
3. Set a new Incremental:

	mvn org.shu4m:shu4m-maven-plugin:setincremental -Dincremental=4711

4. Increment incremental:

	mvn org.shu4m:shu4m-maven-plugin:nextincremental

5. Set a new Qualifier:

	mvn org.shu4m:shu4m-maven-plugin:setqualifier -Dqualifier=BETA1

6. Dump pom to property file

	mvn org.shu4m:shu4m-maven-plugin:dumppom -Dprefix="pom_" -Dfilename="mine.properties"

