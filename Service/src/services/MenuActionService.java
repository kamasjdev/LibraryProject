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
		listMenu.add(MenuAction.Create(1, "Add Author", "Main"));
		listMenu.add(MenuAction.Create(2, "Add Book", "Main"));
		listMenu.add(MenuAction.Create(3, "Add Customer", "Main"));
		listMenu.add(MenuAction.Create(4, "View Authors", "Main"));
		listMenu.add(MenuAction.Create(5, "View Author", "Main"));
		listMenu.add(MenuAction.Create(6, "View Books", "Main"));
		listMenu.add(MenuAction.Create(7, "View Book", "Main"));
		listMenu.add(MenuAction.Create(8, "View Customers", "Main"));
		listMenu.add(MenuAction.Create(9, "View Customer", "Main"));
		listMenu.add(MenuAction.Create(10, "Edit Author", "Main"));
		listMenu.add(MenuAction.Create(11, "Edit Book", "Main"));
		listMenu.add(MenuAction.Create(12, "Edit Customer", "Main"));
		listMenu.add(MenuAction.Create(13, "Borrow Book", "Main"));
		listMenu.add(MenuAction.Create(14, "Return Book", "Main"));
		listMenu.add(MenuAction.Create(15, "Delete Author", "Main"));
		listMenu.add(MenuAction.Create(16, "Delete Book", "Main"));
		listMenu.add(MenuAction.Create(17, "Delete Customer", "Main"));
	}

}
