package controllers.mapper;

import controllers.pojos.author.AddAuthor;
import controllers.pojos.author.UpdateAuthor;
import dto.AuthorDto;

public class MapToDto {
	public static AuthorDto mapAddAuthorToAuthorDto(AddAuthor addAuthor) {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = addAuthor.firstName;
		authorDto.lastName = addAuthor.lastName;
		authorDto.id = 0;
		return authorDto;
	}
	
	public static AuthorDto mapUpdateAuthorToAuthorDto(UpdateAuthor updateAuthor) {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = updateAuthor.firstName;
		authorDto.lastName = updateAuthor.lastName;
		authorDto.id = updateAuthor.id;
		return authorDto;
	}
}
