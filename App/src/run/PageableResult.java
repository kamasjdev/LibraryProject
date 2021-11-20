package run;

import java.util.List;

public class PageableResult<T> {
	public List<T> list;
	public int currentPage;
	public int pages;
	
	public static<T> PageableResult<T> Create(List<T> list, int currentPage, int pages) {
		PageableResult<T> pageableResult = new PageableResult<T>();
		pageableResult.list = list;
		pageableResult.currentPage = currentPage;
		pageableResult.pages = pages;
		return pageableResult;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		StringBuilder listString = new StringBuilder();
		list.forEach(l -> listString.append(l).append("\n"));
		sb.append(String.format("List: \n%1$s", listString));
		sb.append(String.format("Current Page: %1$s \n", currentPage));
		sb.append(String.format("Pages: %1$S", pages));
		return sb.toString();
	}
}
