package canvas.canvasapp.model;

import jakarta.persistence.*;
import lombok.*;
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
public class Announcement {
	@Id
	@Column(nullable = false)
	private Long id;
	@Column(nullable = false)
	private String title;
	private Date createdAt;
	private Date lockAt;
	private Date postedAt;
	@Column(length = 1024, nullable = false)
	private String htmlUrl;    // url to the announcement
	@Column(length = 1024, nullable = false)
	private String url;
	private Long position;
	private String contextCode;

	@ManyToOne(cascade = CascadeType.ALL)
	private Course course;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Announcement that = (Announcement) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
