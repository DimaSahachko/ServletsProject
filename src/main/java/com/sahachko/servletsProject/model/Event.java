package com.sahachko.servletsProject.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="events")
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private Integer fileId;
	
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false)
	private Date eventTime;
	
	@Column(name = "action", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventAction eventAction;

	public Event() {
		
	}
	
	public Event(int fileId, String fileName, EventAction eventAction) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.eventAction = eventAction;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public EventAction getEventAction() {
		return eventAction;
	}

	public void setEventAction(EventAction eventAction) {
		this.eventAction = eventAction;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY 'at' HH:mm:ss");
		return "Event [id=" + id + ", fileId=" + fileId + ", fileName=" + fileName + ", eventTime=" + dateFormat.format(eventTime)
				+ ", eventAction=" + eventAction + "]";
	}

}
