package ch.uzh.ifi.hase.soprafs24.rest.mapper;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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
    @Mapping(source = "coinBalance", target = "coinBalance")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "compensation", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "address", target = "address")
    Task convertTaskPostDTOToEntity(TaskPostDTO taskPostDTO);

    @Mapping(source = "price", target = "compensation")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "status", target = "status")
    TaskGetDTO convertEntityToTaskGetDTO(Task task);

    @Mapping(source = "stars", target = "rating")
    @Mapping(source = "comment", target = "review")
    Rating convertRatingPostDTOToEntity(RatingPostDTO ratingPostDTO);


}
