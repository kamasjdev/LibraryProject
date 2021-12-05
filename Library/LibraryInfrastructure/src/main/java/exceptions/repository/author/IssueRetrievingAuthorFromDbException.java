package exceptions.repository.author;

import exceptions.InfrastructureException;

public class IssueRetrievingAuthorFromDbException extends InfrastructureException  {
	private static final long serialVersionUID = 2493201185316335398L;
	
	public IssueRetrievingAuthorFromDbException(Throwable throwable) {
		super("There was an issue while retrieving author from database", throwable);
	}

	@Override
	public String getCode() {
		String code = "issue_retrieving_author_from_db";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "AuthorRepository";
		return className;
	}
	
	
}
