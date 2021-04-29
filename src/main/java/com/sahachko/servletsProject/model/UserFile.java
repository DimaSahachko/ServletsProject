package com.sahachko.servletsProject.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Files")
public class UserFile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable=false, updatable=false)
	private String name;
	
	@Column(nullable = false, updatable = false)
	private Integer userId;

	@Column(nullable=false, updatable=false)
	private Date created;
	
	private Date updated;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FileStatus status;

	public UserFile() {
		super();
	}

	public UserFile(String name, int userId, FileStatus status) {
		super();
		this.name = name;
		this.userId = userId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public FileStatus getStatus() {
		return status;
	}

	public void setStatus(FileStatus status) {
		this.status = status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserFile other = (UserFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY 'at' HH:mm:ss");
		if (updated == null) {
			return "UserFile [id=" + id + ", name=" + name + ", userId=" + userId + ", created=" + dateFormat.format(created) + ", status=" + status + "]";
		} else {
			return "UserFile [id=" + id + ", name=" + name + ", userId=" + userId + ", created=" + dateFormat.format(created) + ", updated=" + dateFormat.format(updated)
					+ ", status=" + status + "]";
		}
	}

}
