package services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entities.MenuAction;
import interfaces.BaseMenuAction;

public class MenuActionService implements BaseMenuAction<MenuAction> {
	private List<MenuAction> listMenu;
	
	public MenuActionService() {
		initialize();
	}

	@Override
	public List<MenuAction> getMenuActionByMenuName(String menuName) {
		List<MenuAction> menu = listMenu.stream().filter(m->m.menuName.equals(menuName)).collect(Collectors.toList());
		return menu;
	}
	
	private void initialize() {
		listMenu = new ArrayList<MenuAction>();
		listMenu.add(MenuAction.create(1, "Add Author", "Main"));
		listMenu.add(MenuAction.create(2, "Add Book", "Main"));
		listMenu.add(MenuAction.create(3, "Add Customer", "Main"));
		listMenu.add(MenuAction.create(4, "View Authors", "Main"));
		listMenu.add(MenuAction.create(5, "View Author", "Main"));
		listMenu.add(MenuAction.create(6, "View Books", "Main"));
		listMenu.add(MenuAction.create(7, "View Book", "Main"));
		listMenu.add(MenuAction.create(8, "View Customers", "Main"));
		listMenu.add(MenuAction.create(9, "View Customer", "Main"));
		listMenu.add(MenuAction.create(10, "Edit Author", "Main"));
		listMenu.add(MenuAction.create(11, "Edit Book", "Main"));
		listMenu.add(MenuAction.create(12, "Edit Customer", "Main"));
		listMenu.add(MenuAction.create(13, "Borrow Book", "Main"));
		listMenu.add(MenuAction.create(14, "Return Book", "Main"));
		listMenu.add(MenuAction.create(15, "Delete Author", "Main"));
		listMenu.add(MenuAction.create(16, "Delete Book", "Main"));
		listMenu.add(MenuAction.create(17, "Delete Customer", "Main"));
	}

}
