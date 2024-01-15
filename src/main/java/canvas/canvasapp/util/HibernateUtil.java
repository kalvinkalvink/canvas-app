package canvas.canvasapp.util;

import canvas.canvasapp.model.Course;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.util.Properties;
@Slf4j
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {

		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();

				Properties settings = new Properties();
				settings.put(Environment.DRIVER, "org.h2.Driver");
				settings.put(Environment.URL, "jdbc:h2:./src/main/resources/db/canvasapp");
				settings.put(Environment.USER, "canvas");
				settings.put(Environment.PASS, "canvasapp");
				settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
				settings.put(Environment.SHOW_SQL, "true");
				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				settings.put(Environment.HBM2DDL_AUTO, "update");

				configuration.setProperties(settings);
				configuration.addAnnotatedClass(Course.class);
//				configuration.addDirectory(new File("canvas/canvasapp/model"));

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				log.error("Failed to get sessionfactory", e);

			}
		}
		return sessionFactory;
	}
	public static void shutdown(){
		getSessionFactory().close();
	}
}
