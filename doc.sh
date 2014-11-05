rm -rf docs
grails doc --pdf
grails add-tracking
rm -rf docs/manual/api/
rm -rf docs/manual/gapi/
