package run;

import java.util.ArrayList;
import java.util.List;

public class Result {
	public List<Integer> ids;
	public int currentPage;
	public int nextPage;
	public boolean stop;
	
	public static Result Create(List<Integer> ids, int currentPage, int nextPage, boolean stop) {
		Result result = new Result();
		result.ids = ids != null ? ids : new ArrayList<Integer>();
		result.currentPage = currentPage;
		result.nextPage = nextPage;
		result.stop = stop;
		return result;
	}
}
