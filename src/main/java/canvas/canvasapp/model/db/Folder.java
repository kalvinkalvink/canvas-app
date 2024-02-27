package canvas.canvasapp.model.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
public class Folder {
	@Id
	@Column(nullable = false)
	private Long id;
	@Column(nullable = false)
	private String name;
	private String fullName;
	private Long contextId;
	private String contextType;
	private Long parentFolderId;
	private Date createdAt;
	private Date updatedAt;
	private Date lockAt;
	private Date unlockAt;
	private Long position;
	private boolean locked;
	private String foldersUrl;
	private String filesUrl;
	private Long filesCount;
	private Long foldersCount;
	private boolean hidden;
	private boolean lockedForser;
	private boolean hiddenForUser;
	private boolean forSubmissions;
	private boolean canUpload;

	@ManyToOne(cascade = CascadeType.ALL)
	Course course;
}
