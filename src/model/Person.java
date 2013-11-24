package model;

public class Person {

	private int _id;
	private String _name;
	private Address _addy;
	
	public Person(int id, String name, Address addy){
		_id = id;
		_name = name;
		_addy = addy;
		
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public Address get_addy() {
		return _addy;
	}

	public void set_addy(Address _addy) {
		this._addy = _addy;
	}

	@Override
	public String toString() {
		return "Person [\nid=" + _id + "\n" + _name + "\n" + _addy
				+ "\n]";
	}
	
}
