package interfaces;

import dto.AuthorDto;

public interface AuthorService extends BaseService<AuthorDto> {
	AuthorDto getDetails(Integer authorId);
	int getCount();
}
