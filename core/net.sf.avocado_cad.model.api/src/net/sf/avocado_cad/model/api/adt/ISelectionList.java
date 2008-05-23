package net.sf.avocado_cad.model.api.adt;

public interface ISelectionList {

	public int getSelectionSize();
	
	public boolean contains(String s);
	
	public boolean isSatisfied();
	
	public boolean hasFocus();
	
	public String getStringAtIndex(int i);

	// The following methods are deprecated because ISelectionList should be a read-only interface.
	@Deprecated
	public void add(String string);

	
	@Deprecated
	public void clearList();

	@Deprecated
	public void setSatisfied(boolean isSatisfied);


	@Deprecated
	public void setHasFocus(boolean hasFocus);

}
