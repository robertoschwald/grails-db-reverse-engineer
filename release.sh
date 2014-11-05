rm -rf target/release
mkdir target/release
cd target/release
git clone git@github.com:grails-plugins/grails-db-reverse-engineer.git
cd grails-db-reverse-engineer
grails clean
grails compile
grails publish-plugin --stacktrace
