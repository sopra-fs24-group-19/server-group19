package ch.uzh.ifi.hase.soprafs24.rest.mapper;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.TaskService;
import ch.uzh.ifi.hase.soprafs24.service.TodoService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface DTOMapper {
  // TODO: add mappings for all DTOS needed
  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "radius", target = "radius")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "coinBalance", target = "coinBalance")
    UserGetDTO convertEntityToUserGetDTO(User userGetDto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "radius", target = "radius")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    User convertUserEditDTOToEntity(UserEditDTO userEditDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "radius", target = "radius")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "coinBalance", target = "coinBalance")
    @Mapping(source = "totalComments", target = "totalComments")
    @Mapping(source = "averageStars", target = "averageStars")
    UserGetFullDTO convertEntityToUserGetFullDTO(User userGetFull);

    @Mapping(source = "compensation", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")   
    @Mapping(source = "title", target = "title")
    @Mapping(source = "duration", target = "duration")
    Task convertTaskPostDTOToEntity(TaskPostDTO taskPostDTO);


    @Mapping(source = "description", target = "description")
    Todo convertTodoPostDTOToEntity(TodoPostDTO todoPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "done", target = "done")
    Todo convertTodoPutDTOToEntity(TodoPutDTO todoPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "done", target = "done")
    TodoGetDTO convertEntityToTodoGetDTO(Todo todo);

    @Mapping(source = "price", target = "compensation")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "helper.id", target = "helperId")
    //@Mapping(source = "creator", target = "creator")
    @Mapping(source = "id", target = "id")
    TaskGetDTO convertEntityToTaskGetDTO(Task task);

    @Mapping(source = "stars", target = "rating")
    @Mapping(source = "comment", target = "review")
    Rating convertRatingPostDTOToEntity(RatingPostDTO ratingPostDTO);

    @Mapping(source = "stars", target = "rating")
    @Mapping(source = "comment", target = "review")
    Rating convertRatingPutDTOToEntity(RatingPutDTO ratingPutDTO);
    
    @Mapping(source = "rating", target = "stars")
    @Mapping(source = "review", target = "comment")
    @Mapping(source = "reviewer", target = "reviewer")
    @Mapping(source = "reviewed", target = "reviewed")
    @Mapping(source = "creationDate", target = "creationDate")
    RatingGetDTO convertEntityToRatingGetDTO(Rating rating);

/*     @Mapping(source = "helper", target = "helper")
    @Mapping(source = "userId", target = "creator")
    @Mapping(source = "taskId", target = "id")
    Task convertTaskPutDTOToEntity(TaskPutDTO taskPutDTO); */
}
