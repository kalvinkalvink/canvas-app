package canvas.canvasapp.controller;

import canvas.canvasapp.task.StartupFetchDataTask;
import canvas.canvasapp.util.HibernateUtil;
import edu.ksu.canvas.model.Course;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class StartupController {
	public static void initApplication(){
		try{
			initDb();
			fetchDataFromCanvas();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	private static void initDb() {
		log.info("Setting up database");
		// read startup script
		String startupSqlPath = "sql/startup.sql";
		StringBuilder scriptContent = new StringBuilder();
		try (InputStream inputStream = StartupController.class.getClassLoader().getResourceAsStream(startupSqlPath)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				scriptContent.append(line);
			}
		} catch (IOException e) {
			log.error("Failed to init db",e);
		}
		// run startup script
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.createSQLQuery(scriptContent.toString()).executeUpdate();
			transaction.commit();
		}
	}
	private static void fetchDataFromCanvas() throws ExecutionException, InterruptedException {
		log.info("Fetching data from canvas");
		StartupFetchDataTask fetchDataTask = new StartupFetchDataTask();
		fetchDataTask.run();


	}
}
