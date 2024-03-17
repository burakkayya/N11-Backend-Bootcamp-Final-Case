package com.burakkaya.userservice.business.concretes;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.enums.Status;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import com.burakkaya.userservice.business.dto.requests.CreateUserRequest;
import com.burakkaya.userservice.business.dto.requests.UpdateUserRequest;
import com.burakkaya.userservice.business.dto.responses.CreateUserResponse;
import com.burakkaya.userservice.business.dto.responses.GetAllUsersResponse;
import com.burakkaya.userservice.business.dto.responses.GetUserResponse;
import com.burakkaya.userservice.business.dto.responses.UpdateUserResponse;
import com.burakkaya.userservice.business.rules.UserBusinessRules;
import com.burakkaya.userservice.entities.User;
import com.burakkaya.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ModelMapperService mockMapper;
    @Mock
    private UserBusinessRules mockRules;

    private UserManager userManagerUnderTest;

    @BeforeEach
    void setUp() {
        userManagerUnderTest = new UserManager(mockUserRepository, mockMapper, mockRules);
    }

    @Test
    void testGetAllUsers() {
        final User user = new User();
        user.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");
        user.setStatus(Status.ACTIVE);
        final List<User> users = List.of(user);
        when(mockUserRepository.findAll()).thenReturn(users);

        when(mockMapper.forResponse()).thenReturn(new ModelMapper());

        final List<GetAllUsersResponse> result = userManagerUnderTest.getAllUsers();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        GetAllUsersResponse actualUser = result.get(0);
        assertThat(actualUser.getId()).isEqualTo(user.getId());
        assertThat(actualUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(actualUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(actualUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(actualUser.getStatus()).isEqualTo(user.getStatus());
    }

    @Test
    void testGetAllUsers_UserRepositoryReturnsNoItems() {
        when(mockUserRepository.findAll()).thenReturn(Collections.emptyList());

        final List<GetAllUsersResponse> result = userManagerUnderTest.getAllUsers();

        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetUserById() {
        final User user1 = new User();
        user1.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setEmail("email");
        user1.setStatus(Status.ACTIVE);
        final Optional<User> user = Optional.of(user1);
        when(mockUserRepository.findById(UUID.fromString("5a68a23b-a210-4f3e-b86a-9ccfa6b3cb6e"))).thenReturn(user);

        when(mockMapper.forResponse()).thenReturn(new ModelMapper());

        final GetUserResponse result = userManagerUnderTest.getUserById(
                UUID.fromString("5a68a23b-a210-4f3e-b86a-9ccfa6b3cb6e"));

        verify(mockRules).checkIfUserExistsById(UUID.fromString("5a68a23b-a210-4f3e-b86a-9ccfa6b3cb6e"));
    }

    @Test
    void testGetUserById_UserRepositoryReturnsAbsent() {
        when(mockUserRepository.findById(UUID.fromString("5a68a23b-a210-4f3e-b86a-9ccfa6b3cb6e")))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userManagerUnderTest.getUserById(
                UUID.fromString("5a68a23b-a210-4f3e-b86a-9ccfa6b3cb6e"))).isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfUserExistsById(UUID.fromString("5a68a23b-a210-4f3e-b86a-9ccfa6b3cb6e"));
    }

    @Test
    void testCreateUser() {
        final CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstName("firstName");
        createUserRequest.setLastName("lastName");
        createUserRequest.setEmail("email");
        createUserRequest.setPassword("password");
        createUserRequest.setPasswordConfirm("password");

        when(mockMapper.forRequest()).thenReturn(new ModelMapper());

        // Configure UserRepository.save(...).
        final User user = new User();
        user.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");
        user.setStatus(Status.ACTIVE);
        when(mockUserRepository.save(any(User.class))).thenReturn(user);

        when(mockMapper.forResponse()).thenReturn(new ModelMapper());

        final CreateUserResponse result = userManagerUnderTest.createUser(createUserRequest);

        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getStatus()).isEqualTo(user.getStatus());
    }

    @Test
    void testCreateUser_UserRepositoryThrowsOptimisticLockingFailureException() {
        final CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstName("firstName");
        createUserRequest.setLastName("lastName");
        createUserRequest.setEmail("email");
        createUserRequest.setPassword("password");
        createUserRequest.setPasswordConfirm("password");

        when(mockMapper.forRequest()).thenReturn(new ModelMapper());
        when(mockUserRepository.save(any(User.class))).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(() -> userManagerUnderTest.createUser(createUserRequest))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testUpdateUser() {
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        updateUserRequest.setFirstName("firstName");
        updateUserRequest.setLastName("lastName");
        updateUserRequest.setEmail("email");
        updateUserRequest.setPhoneNumber("phoneNumber");

        when(mockMapper.forRequest()).thenReturn(new ModelMapper());

        final User user = new User();
        user.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");
        user.setStatus(Status.ACTIVE);
        final Optional<User> userOptional = Optional.of(user);
        when(mockUserRepository.findById(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"))).thenReturn(userOptional);
        when(mockUserRepository.save(any(User.class))).thenReturn(user);

        when(mockMapper.forResponse()).thenReturn(new ModelMapper());

        final UpdateUserResponse result = userManagerUnderTest.updateUser(
                UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"), updateUserRequest);

        verify(mockRules).checkIfUserExistsById(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
    }

    @Test
    void testUpdateUser_UserRepositoryThrowsOptimisticLockingFailureException() {
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(UUID.fromString("b6855f90-09ae-4526-bc98-6c095ff5685e"));
        updateUserRequest.setFirstName("firstName");
        updateUserRequest.setLastName("lastName");
        updateUserRequest.setEmail("email");
        updateUserRequest.setPhoneNumber("phoneNumber");

        final User oldUser = new User();
        oldUser.setId(updateUserRequest.getId());
        oldUser.setFirstName("oldFirstName");
        oldUser.setLastName("oldLastName");
        oldUser.setEmail("oldEmail");
        oldUser.setPassword("oldPassword");
        oldUser.setPhoneNumber("oldPhoneNumber");
        oldUser.setStatus(Status.ACTIVE);

        when(mockMapper.forRequest()).thenReturn(new ModelMapper());
        when(mockUserRepository.findById(any(UUID.class))).thenReturn(Optional.of(oldUser));
        when(mockUserRepository.save(any(User.class))).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(
                () -> userManagerUnderTest.updateUser(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"),
                        updateUserRequest)).isInstanceOf(OptimisticLockingFailureException.class);
        verify(mockRules).checkIfUserExistsById(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
    }

    @Test
    void testDeleteUser() {
        final User user1 = new User();
        user1.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setEmail("email");
        user1.setStatus(Status.ACTIVE);
        final Optional<User> user = Optional.of(user1);
        when(mockUserRepository.findById(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"))).thenReturn(user);

        userManagerUnderTest.deleteUser(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"));

        verify(mockRules).checkIfUserExistsById(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"));
        verify(mockUserRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser_UserRepositoryFindByIdReturnsAbsent() {
        when(mockUserRepository.findById(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2")))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userManagerUnderTest.deleteUser(
                UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"))).isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfUserExistsById(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"));
    }

    @Test
    void testDeleteUser_UserRepositorySaveThrowsOptimisticLockingFailureException() {
        final User user1 = new User();
        user1.setId(UUID.fromString("4ae259aa-3fb2-4aeb-a3e2-6f12d8382d91"));
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setEmail("email");
        user1.setStatus(Status.ACTIVE);
        final Optional<User> user = Optional.of(user1);
        when(mockUserRepository.findById(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"))).thenReturn(user);

        when(mockUserRepository.save(any(User.class))).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(() -> userManagerUnderTest.deleteUser(
                UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2")))
                .isInstanceOf(OptimisticLockingFailureException.class);
        verify(mockRules).checkIfUserExistsById(UUID.fromString("e29850ab-6fe5-4907-b5d4-b4d8be0548f2"));
    }

    @Test
    void testCheckIfUserExists() {
        final ClientResponse result = userManagerUnderTest.checkIfUserExists(
                UUID.fromString("cf7a5a33-88bd-4473-b4b3-a77325859704"));

        verify(mockRules).checkIfUserExistsById(UUID.fromString("cf7a5a33-88bd-4473-b4b3-a77325859704"));
    }
}
