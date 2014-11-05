package com.pdm.thoth;

public class WorkItem {

	private int _id;
	private String _acronym;
	private String _title;
	private String _submit;
	private String _startdate;
	private String _duedate;

	public WorkItem() {

	}

	public int getId() {
		return _id;
	}

	public String getAcronym() {
		return _acronym;
	}

	public String getTitle() {
		return _title;
	}

	public String getSubmit() {
		return _submit;
	}

	public String getStartdate() {
		return _startdate;
	}

	public String getDuedate() {
		return _duedate;
	}

	public void setId(int id) {
		_id = id;
	}

	public void setAcronym(String acronym) {
		_acronym = acronym;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setSubmit(String submit) {
		_submit = submit;
	}

	public void setStartdate(String startdate) {
		_startdate = startdate;
	}

	public void setDuedate(String duedate) {
		_duedate = duedate;
	}

	@Override
	public String toString() {
		return _title;
	}

}
