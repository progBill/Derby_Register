package model;

public class Person {

	private int _id;
	private String _first_name, _last_name, _hourly_rate, _social;
	private String _leave_date, _leave_reason, _birth, _hire_date;
	private Address _addy;
	private boolean _hasChanged;

	public Person(int id, String first, String last) {
		_id = id;
		_first_name = first;
		_last_name = last;
		_hasChanged = false;
	}

	public String get_hourly_rate() {
		return _hourly_rate;
	}

	public String get_first_name() {
		return _first_name;
	}

	public String get_last_name() {
		return _last_name;
	}

	public String get_social() {
		return _social;
	}

	public int get_id() {
		return _id;
	}

	public Address get_addy() {
		return _addy;
	}

	public String get_leave_date() {
		return _leave_date;
	}

	public String get_leave_reason() {
		return _leave_reason;
	}

	public String get_birth() {
		return _birth;
	}

	public String get_hire_date() {
		return _hire_date;
	}

	// / GETTERS ABOVE SETTERS BELOW

	public void set_hourly_rate(String _hourly_rate) {
		this._hourly_rate = _hourly_rate;
	}

	public void set_social(String _social) {
		this._social = _social;
	}

	public void setAll(String incFName, String incLName, Address incAddy) {
		_first_name = incFName;
		_last_name = incLName;
		_addy = incAddy;
		_hasChanged = true;
	}

	public void set_addy(Address _addy) {
		this._addy = _addy;
		_hasChanged = true;
	}

	public void set_first_name(String _first_name) {
		this._first_name = _first_name;
		_hasChanged = true;
	}

	public void set_last_name(String _last_name) {
		this._last_name = _last_name;
		_hasChanged = true;
	}

	public void set_birth(String birth) {
		this._birth = birth;
	}

	public void set_hire_date(String hire_date) {
		this._hire_date = hire_date;
	}

	public void set_leave_date(String leave_date) {
		this._leave_date = leave_date;
	}

	public void set_leave_reason(String leave_reason) {
		this._leave_reason = leave_reason;
	}

	///////ACTUAL DO-STUFF BELOW
	
	public boolean changed() {
		return _hasChanged;
	}

	@Override
	public String toString() {
		return "Person [\nid=" + _id + "\n" + _first_name + " " + _last_name
				+ "\n" + _addy + "\n]";
	}

}
