package org.grails.datastore.gorm.neo4j

import org.springframework.datastore.mapping.core.AbstractDatastore
import org.springframework.datastore.mapping.core.Session
import org.springframework.beans.factory.InitializingBean
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.datastore.mapping.model.MappingContext
import org.springframework.datastore.mapping.model.PersistentEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.Relationship
import org.neo4j.graphdb.Node

/**
 * TODO: refactor constructors to be groovier
 * Created by IntelliJ IDEA.
 * User: stefan
 * Date: 25.04.11
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
class Neo4jDatastore extends AbstractDatastore implements InitializingBean, MappingContext.Listener {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jDatastore.class);

    GraphDatabaseService graphDatabaseService
    def subReferenceNodes // maps entity class names to neo4j subreference node

    def storeDir
    //Transaction transaction

    /**
     * only to be called during testing
     * @return
     */
    public Neo4jDatastore() {
        this(new Neo4jMappingContext(), null, null)
    }

    public Neo4jDatastore(Neo4jMappingContext mappingContext, ConfigurableApplicationContext ctx, GraphDatabaseService graphDatabaseService) {
        super(mappingContext, Collections.<String,String>emptyMap(), ctx)
        this.graphDatabaseService = graphDatabaseService
        if (mappingContext != null) {
            mappingContext.addMappingContextListener(this);
        }

        initializeConverters(mappingContext);

/*        mappingContext.getConverterRegistry().addConverter(new Converter<String, ObjectId>() {
            public ObjectId convert(String source) {
                return new ObjectId(source);
            }
        });

        mappingContext.getConverterRegistry().addConverter(new Converter<ObjectId, String>() {
            public String convert(ObjectId source) {
                return source.toString();
            }
        });*/

    }

    public Neo4jDatastore(Neo4jMappingContext mappingContext, GraphDatabaseService graphDatabaseService) {
        this(mappingContext, null, graphDatabaseService);
    }

    @Override
    protected Session createSession(Map<String, String> connectionDetails) {
        new Neo4jSession(this, mappingContext, applicationEventPublisher)
    }

    void afterPropertiesSet() {
        if (!graphDatabaseService) {
            if (!storeDir) {
                storeDir = File.createTempFile("neo4j",null)
                assert storeDir.delete()
                assert storeDir.mkdir()
                // directory.deleteOnExit()
                storeDir = storeDir.path
            }
            graphDatabaseService = new EmbeddedGraphDatabase(storeDir)
        }
        subReferenceNodes = findSubReferenceNodes()

    }

    @Override
    void persistentEntityAdded(PersistentEntity entity) {
        LOG.warn("persistentEntityAdded $entity")
    }

    def findSubReferenceNodes() {
        def map = [:]
        Node referenceNode = graphDatabaseService.referenceNode
        for (Relationship rel in referenceNode.getRelationships(GrailsRelationshipTypes.SUBREFERENCE, Direction.OUTGOING)) {
            def endNode = rel.endNode
            def clazz = endNode.getProperty(Neo4jEntityPersister.SUBREFERENCE_PROPERTY_NAME)
            map[clazz] = endNode
        }
        map
    }

}

