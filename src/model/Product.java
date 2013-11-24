package model;
import java.math.BigDecimal;

public class Product {
	private String _name;
	private int _id, _quantity, _reorderLevel;
	private BigDecimal _costPrice, _listPrice;
	private boolean _hasChanged;

	public Product(String name, int id, int qty, int reorder, BigDecimal cost, BigDecimal list){
		_name = name;
		_id = id;
		_quantity = qty;
		_reorderLevel = reorder;
		_costPrice = cost;
		_listPrice = list;
		_hasChanged = false;
	}

	public String getName(){return _name;}
	public BigDecimal getCost(){return _costPrice;}
	public BigDecimal getList(){return _listPrice;}
	public boolean hasChanged(){return _hasChanged;}
	public int getReorder(){ return _reorderLevel;}
	public int getQuantity() {return _quantity;}
	public int getID() {return _id;}
	
	public void setCost(BigDecimal newCost){
		_costPrice = newCost;
		_hasChanged = true;
	}
	
	public void setList(BigDecimal newList){
		_listPrice = newList;
		_hasChanged = true;
	}
	
}
