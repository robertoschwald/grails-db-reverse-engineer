/* Copyright 2010-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.reveng

import groovy.transform.CompileStatic
import org.hibernate.MappingException
import org.hibernate.boot.Metadata
import org.hibernate.boot.internal.BootstrapContextImpl
import org.hibernate.boot.internal.InFlightMetadataCollectorImpl
import org.hibernate.boot.internal.MetadataBuilderImpl
import org.hibernate.boot.internal.MetadataBuildingContextRootImpl
import org.hibernate.boot.internal.MetadataImpl
import org.hibernate.boot.registry.StandardServiceRegistry
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.boot.spi.BootstrapContext
import org.hibernate.boot.spi.MetadataBuildingContext
import org.hibernate.boot.spi.MetadataBuildingOptions
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy
import org.hibernate.engine.spi.Mapping
import org.hibernate.id.factory.IdentifierGeneratorFactory
import org.hibernate.mapping.PersistentClass
import org.hibernate.mapping.Property
import org.hibernate.tool.internal.metadata.JdbcMetadataDescriptor
import org.hibernate.type.Type

/**
 * Creates a GrailsJdbcBinder to register a logging ProgressListener.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
@CompileStatic
class GrailsJdbcMetaDataConfiguration extends JdbcMetadataDescriptor {

	private boolean preferBasicCompositeIds = true
	private Properties properties = new Properties()
	private ReverseEngineeringStrategy reverseEngineeringStrategy = new DefaultReverseEngineeringStrategy()

	GrailsJdbcMetaDataConfiguration (
		ReverseEngineeringStrategy reverseEngineeringStrategy,
		Properties properties,
		boolean preferBasicCompositeIds){
		super(reverseEngineeringStrategy, properties, preferBasicCompositeIds)
	}

	Metadata createMetadata() {
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
			.applySettings(getProperties())
			.build()
		MetadataBuilderImpl.MetadataBuildingOptionsImpl metadataBuildingOptions =
			new MetadataBuilderImpl.MetadataBuildingOptionsImpl( serviceRegistry )

		BootstrapContextImpl bootstrapContext = new BootstrapContextImpl(
			serviceRegistry,
			metadataBuildingOptions)

		metadataBuildingOptions.setBootstrapContext(bootstrapContext)

		InFlightMetadataCollectorImpl metadataCollector =
			getMetadataCollector(bootstrapContext, metadataBuildingOptions)

		MetadataBuildingContext metadataBuildingContext =
			new MetadataBuildingContextRootImpl(
				bootstrapContext,
				metadataBuildingOptions,
				metadataCollector)

		MetadataImpl metadata = metadataCollector
			.buildMetadataInstance(metadataBuildingContext)

		metadata.getTypeConfiguration().scope(metadataBuildingContext)

		GrailsJdbcBinder binder = new GrailsJdbcBinder(
			serviceRegistry, getProperties(), metadataBuildingContext, reverseEngineeringStrategy, preferBasicCompositeIds )

		binder.readFromDatabase(null, null, buildMapping(metadata))
		return metadata
	}

	private InFlightMetadataCollectorImpl getMetadataCollector(
		BootstrapContext bootstrapContext,
		MetadataBuildingOptions metadataBuildingOptions) {
		return new InFlightMetadataCollectorImpl(
			bootstrapContext,
			metadataBuildingOptions)
	}

	private Mapping buildMapping(final Metadata metadata) {
		return new Mapping() {
			/**
			 * Returns the identifier type of a mapped class
			 */
			public Type getIdentifierType(String persistentClass) throws MappingException {
				final PersistentClass pc = metadata.getEntityBinding(persistentClass);
				if (pc==null) throw new MappingException("persistent class not known: " + persistentClass);
				return pc.getIdentifier().getType();
			}

			public String getIdentifierPropertyName(String persistentClass) throws MappingException {
				final PersistentClass pc = metadata.getEntityBinding(persistentClass);
				if (pc==null) throw new MappingException("persistent class not known: " + persistentClass);
				if ( !pc.hasIdentifierProperty() ) return null;
				return pc.getIdentifierProperty().getName();
			}

			public Type getReferencedPropertyType(String persistentClass, String propertyName) throws MappingException
			{
				final PersistentClass pc = metadata.getEntityBinding(persistentClass);
				if (pc==null) throw new MappingException("persistent class not known: " + persistentClass);
				Property prop = pc.getProperty(propertyName);
				if (prop==null)  throw new MappingException("property not known: " + persistentClass + '.' + propertyName);
				return prop.getType();
			}

			public IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
				return null;
			}
		};
	}
}
