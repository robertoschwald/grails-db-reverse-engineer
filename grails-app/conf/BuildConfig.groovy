grails.project.work.dir = 'target'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		mavenLocal()
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		compile 'org.hibernate:hibernate-tools:4.3.1.Final', {
			excludes 'ant', 'common', 'org.eclipse.jdt.core', 'runtime', 'text'
		}

		compile 'org.hibernate:hibernate-core:4.3.10.Final'
	}

	plugins {
		build ':release:3.1.2', ':rest-client-builder:2.1.1', {
			export = false
		}
	}
}
