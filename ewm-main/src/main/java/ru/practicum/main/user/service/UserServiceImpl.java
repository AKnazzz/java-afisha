package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.error.exception.EntityNotFoundException;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(userMapper.toUser(userDto));
        log.info("Новый User c ID {} создан.", user.getId());
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userExists(userId);
        userRepository.deleteById(userId);
        log.info("User c ID {} удалён", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null || ids.isEmpty()) {
            log.info("Получен список всех User с параметрами ids={}, from={}, size={}", ids, from, size);
            return userMapper.toUserDto(userRepository.findAll(PageRequest.of(from / size, size)));
        } else {
            log.info("Получен список всех User с параметрами ids={}, from={}, size={}", ids, from, size);
            return userMapper.toUserDto(userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size)));
        }
    }

    private void userExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(User.class, userId);
        }
    }

}
