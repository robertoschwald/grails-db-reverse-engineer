grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'target.docs/manual'

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
		compile 'org.hibernate:hibernate-tools:3.6.0.Final', {
			excludes 'ant', 'cglib', 'common', 'org.eclipse.jdt.core', 'runtime', 'text'
		}

		compile 'org.hibernate:hibernate-core:3.6.10.Final'
	}

	plugins {
		build ':release:3.1.2', ':rest-client-builder:2.1.1', {
			export = false
		}
	}
}
