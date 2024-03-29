package com.SEP490_G9.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonIgnoreProperties(value = { "reports" })
@Entity
@Table(name = "violation_types")
public class ViolationType implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;
	@Column(name = "name", unique = true, nullable = false)

	private String name;

	@OneToMany(mappedBy = "violation_types")
	private List<Report> reports;

	public ViolationType() {

	}

	public ViolationType(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public ViolationType(String name, List<Report> report) {
		this.name = name;
		this.reports = report;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

}
