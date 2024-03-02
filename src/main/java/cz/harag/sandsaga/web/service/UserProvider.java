package cz.harag.sandsaga.web.service;

import java.util.List;
import java.util.stream.Collectors;

import cz.harag.sandsaga.web.dto.UserDto;
import cz.harag.sandsaga.web.model.UserEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

/**
 * @author Patrik Harag
 * @version 2024-03-02
 */
@ApplicationScoped
public class UserProvider {

    private static final Logger LOGGER = Logger.getLogger(UserProvider.class);

    @Transactional
    public List<UserDto> list(int pageIndex, int pageSize) {
        return UserEntity.<UserEntity>findAll(Sort.by("timeRegistered").descending())
                .page(pageIndex, pageSize)
                .stream().map(this::asDto).collect(Collectors.toList());
    }

    private UserDto asDto(UserEntity e) {
        UserDto dto = new UserDto();
        dto.setId(e.id);
        dto.setTimeRegistered(e.timeRegistered);
        dto.setRole(e.role);
        dto.setEmail(e.email);
        dto.setDisplayName(e.displayName);
        return dto;
    }

    @Transactional
    public UserDto get(Long id) {
        UserEntity entity = UserEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return asDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        boolean b = UserEntity.deleteById(id);
        if (!b) {
            throw new NotFoundException();
        }
    }
}