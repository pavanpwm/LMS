package application.dbutil;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;





/**

 * @author pavan BTD
 * 

The configuration file contains information about the database and mapping file. Conventionally, its name should be hibernate.cfg.xml.
Let's create an XML file named as hibernate.cfg.xml directly under src/ folder and write configurations it.





Create a helper class to bootstrap hibernate SessionFactory. In most Hibernate applications, the SessionFactory should be instantiated once during application initialization. The single instance should then be used by all code in a particular process, and any Session should be created using this single SessionFactory.

The SessionFactory is thread-safe and can be shared; a Session is a single-threaded object. Let's create HibernateUtil class to configure singleton SessionFactory and use throughout the application.
The bootstrapping API is quite flexible, but in most cases, it makes the most sense to think of it as a 3 step process:

    Build the StandardServiceRegistry
    Build the Metadata
    Use those 2 to build the SessionFactory



 */









public class HibernateUtil {
	
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    
    
    
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create registry
                registry = new StandardServiceRegistryBuilder().configure().build();		// note: hibernate.cfg.xml file should be directly under src/ folder

                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Couldnt connect to database");
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }
    
    

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}