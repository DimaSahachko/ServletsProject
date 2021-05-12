package com.sahachko.servletsProject.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true)
	private String login;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(nullable = false, unique = true)
	private Account account;

	@OneToMany
	@JoinColumn(name = "user_id")
	private List<UserFile> files;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id")
	private List<Event> events;

	public User() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<UserFile> getFiles() {
		return files;
	}

	public void setFiles(List<UserFile> files) {
		this.files = files;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public void addFile(UserFile file) {
		if (files == null) {
			files = new ArrayList<UserFile>();
		}
		files.add(file);
	}

	public void removeFile(UserFile file) {
		if (files != null) {
			files.remove(file);
		}
	}

	public void addEvent(Event event) {
		if (events == null) {
			events = new ArrayList<Event>();
		}
		events.add(event);
	}

	public void removeEvent(Event event) {
		if (events != null) {
			events.remove(event);
		}
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", account=" + account + ", \nfiles=" + files + ", \nevents="
				+ events + "]";
	}

}
