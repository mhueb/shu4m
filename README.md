# Maven Plugins

## Generate a version class from maven pom
```xml
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
```

## Manipulate project version

### Set a new build number:
```
	mvn org.shu4m:shu4m-maven-plugin:set-buildnumber --non-recursive -Dbuildnumber=4711
```

### Increment build number by one:
```
	mvn org.shu4m:shu4m-maven-plugin:next-buildnumber --non-recursive
```

### Set a new incremental:
```
	mvn org.shu4m:shu4m-maven-plugin:set-incremental --non-recursive -Dincremental=4711
	mvn org.shu4m:shu4m-maven-plugin:set-incremental --non-recursive -Dincremental=4711 -Dqualifier="Final"
```

### Increment incremental:
```
	mvn org.shu4m:shu4m-maven-plugin:next-incremental
```

### Set a new incremental and buildnumber:
```
	mvn org.shu4m:shu4m-maven-plugin:set-incremental-and-buildnumber --non-recursive -Dincremental=42 -Dbuildnumber=4711
```

### Set a new Qualifier:
```
	mvn org.shu4m:shu4m-maven-plugin:set-qualifier --non-recursive -Dqualifier=BETA1
```

### Dump pom to property file
```
	mvn org.shu4m:shu4m-maven-plugin:dump-pom -Dprefix="pom_" -Dfilename="mine.properties"
```
