grails.servlet.version = '3.0'
grails.project.work.dir = 'target'
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.plugin.location.'db-reverse-engineer' = '..'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
	inherits 'global'
	log 'warn'
	checksums true
	legacyResolve false

	repositories {
		inherits true

		mavenLocal()
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		compile 'mysql:mysql-connector-java:5.1.38'
	}

	plugins {
		runtime ':hibernate:3.6.10.18'
	}
}
