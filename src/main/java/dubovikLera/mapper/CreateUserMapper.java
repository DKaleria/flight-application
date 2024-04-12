package dubovikLera.mapper;

import dubovikLera.dto.CreateUserDto;
import dubovikLera.entity.Gender;
import dubovikLera.entity.Role;
import dubovikLera.entity.User;
import dubovikLera.utils.LocalDateFormatter;


public class CreateUserMapper implements Mapper<User, CreateUserDto>{
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();
    @Override
    public User mapFrom(CreateUserDto object) {
        return User.builder()
                .name(object.getName())
                .birthday(LocalDateFormatter.format(object.getBirthday()))
                .email(object.getEmail())
                .password(object.getPassword())
                .role(Role.valueOf(object.getRole()))
                .gender(Gender.valueOf(object.getGender()))
                .build();
    }
    private CreateUserMapper(){

    }
    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }
}
