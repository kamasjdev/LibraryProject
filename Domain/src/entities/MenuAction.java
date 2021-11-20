package entities;

import common.BaseEntity;

public class MenuAction extends BaseEntity {
	public String name;
    public String menuName;
    
    public static MenuAction Create(Integer id, String name, String menuName) {
    	MenuAction menuAction = new MenuAction();
    	menuAction.id = id;
    	menuAction.name = name;
    	menuAction.menuName = menuName;
    	
    	return menuAction;
    }
    
    @Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, name, menuName);
		return description;
	}
}
