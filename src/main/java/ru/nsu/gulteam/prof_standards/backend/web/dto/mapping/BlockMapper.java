package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Block;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBlockInfo;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BlockDto;

@Mapper(componentModel = "spring",
        uses = {RoleMapper.class,
                FacultyMapper.class,
                DepartmentMapper.class})
public interface BlockMapper {
    @Mappings({
            @Mapping(source = "course.id", target = "id"),
            @Mapping(source = "course.amount", target = "amount"),
            @Mapping(source = "course.semester", target = "semester"),
            @Mapping(source = "course.attestationForm", target = "attestationForm"),
            @Mapping(source = "canEdit", target = "canEdit"),
    })
    BlockDto toDto(FullBlockInfo course);

    Block fromDto(BlockDto courseDto);
}
