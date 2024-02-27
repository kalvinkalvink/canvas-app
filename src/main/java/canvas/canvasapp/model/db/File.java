package canvas.canvasapp.model.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Data
@Accessors(chain = true)
public class File {
	@Id
	@Column(nullable = false)
	private Long id;
	@Column(nullable = false)
	private String uuid;
	@Column(nullable = false)
	private String displayName;
	@Column(nullable = false)
	private String filename;
	@Column(nullable = false)
	private String url;
	private Long size;
	private Instant createdAt;
	private Instant updatedAt;
	private Instant modifiedAt;
	private Boolean hidden;
	private String thumbnailUrl;
	private String previewUrl;
	private String mimeClass;
	@ManyToOne
	private Course course;
	@ManyToOne(fetch = FetchType.EAGER)
	private Folder folder;
}

