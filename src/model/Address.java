package model;

public class Address {
	
	private String _str, _str2, _town, _st;
	private int _zip;

	public Address(String str, String str2, String town, String st, int zip){
		_str = str;
		_str2 = str2;
		_town = town;
		_st = st;
		_zip = zip;
		
	}

	public String get_str() {
		return _str;
	}

	public void set_str(String _str) {
		this._str = _str;
	}

	public String get_str2() {
		return _str2;
	}

	public void set_str2(String _str2) {
		this._str2 = _str2;
	}

	public String get_town() {
		return _town;
	}

	public void set_town(String _town) {
		this._town = _town;
	}

	public String get_st() {
		return _st;
	}

	public void set_st(String _st) {
		this._st = _st;
	}

	public int get_zip() {
		return _zip;
	}

	public void set_zip(int _zip) {
		this._zip = _zip;
	}

	public String toString() {
		return "Address [\n" + _str + "\n" + _str2 + "\n"
				+ _town + "\n" + _st + "\n" + _zip + "\n]";
	}
	
}
