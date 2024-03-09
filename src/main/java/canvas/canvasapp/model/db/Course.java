package canvas.canvasapp.model.db;


import canvas.canvasapp.model.db.converter.ColorConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
public class Course {

	@Id
	@Column(nullable = false)
	private Long id;
	@Column(unique = true, nullable = false)
	private String name;
	@Column(unique = true, nullable = false)
	private String courseCode;
	@Column(nullable = false)
	private Boolean selected;
	@Column(nullable = false)
	private Boolean synced;
	private Date createdAt;

	@Convert(converter = ColorConverter.class)
	private Color color;

//	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//	@ToString.Exclude
//	private List<Assignment> assignments;
//	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//	@ToString.Exclude
//	private List<Announcement> announcements;
//	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//	private List<File> files;
//	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//	private List<Folder> folders;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Course course = (Course) o;
		return getId() != null && Objects.equals(getId(), course.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
