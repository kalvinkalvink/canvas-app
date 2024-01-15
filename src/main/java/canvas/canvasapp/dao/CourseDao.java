package canvas.canvasapp.dao;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CourseDao {

	public boolean saveOrUpdate(Course course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(course);
			transaction.commit();
			return transaction.getStatus() == TransactionStatus.COMMITTED;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			log.error("Failed to save or update course", e);
		}
		return false;
	}

	public List<Course> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Course", Course.class).list();
		} catch (Exception e) {
			log.error("Failed to get course list", e);
			return new ArrayList<>();
		}
	}
}
