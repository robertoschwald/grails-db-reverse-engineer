dataSource {
	pooled = true
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = false
	cache.region.factory_class = 'org.hibernate.cache.SingletonEhCacheRegionFactory'
	singleSession = true
	flush.mode = 'manual'
	format_sql = true
	use_sql_comments = true
}

environments {
	development {
		dataSource {
			dbCreate = 'none'
			url = 'jdbc:mysql://localhost/reveng'
			driverClassName = 'com.mysql.jdbc.Driver'
			username = 'reveng'
			password = 'reveng'
			dialect = org.hibernate.dialect.MySQL5InnoDBDialect
		}
	}
}
