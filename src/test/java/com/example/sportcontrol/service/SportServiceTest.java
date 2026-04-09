package com.example.sportcontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.sportcontrol.dto.SportDto;
import com.example.sportcontrol.entity.Sport;
import com.example.sportcontrol.mapper.SportMapper;
import com.example.sportcontrol.repository.SportRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SportServiceTest {

    @Mock
    private SportRepository sportRepository;

    @Mock
    private SportMapper sportMapper;

    @InjectMocks
    private SportService service;

    @Test
    void getAllSportsReturnsMappedList() {
        Sport first = new Sport();
        Sport second = new Sport();
        SportDto firstDto = buildSportDto(1L, "Football");
        SportDto secondDto = buildSportDto(2L, "Basketball");

        when(sportRepository.findAll()).thenReturn(List.of(first, second));
        when(sportMapper.toDto(any(Sport.class))).thenReturn(firstDto, secondDto);

        List<SportDto> result = service.getAllSports();

        assertEquals(2, result.size());
        assertEquals(firstDto, result.get(0));
        assertEquals(secondDto, result.get(1));
    }

    @Test
    void getByIdReturnsDtoWhenFound() {
        Sport entity = new Sport();
        SportDto dto = buildSportDto(10L, "Tennis");

        when(sportRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(sportMapper.toDto(entity)).thenReturn(dto);

        SportDto result = service.getById(10L);

        assertEquals(dto, result);
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(sportRepository.findById(5L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.getById(5L));

        assertEquals("Sport 5 not found", exception.getMessage());
    }

    @Test
    void createSavesAndReturnsMappedDto() {
        SportDto input = buildSportDto(null, "Volleyball");
        Sport mapped = new Sport();
        Sport saved = new Sport();
        saved.setId(4L);
        SportDto savedDto = buildSportDto(4L, "Volleyball");

        when(sportMapper.toEntity(input)).thenReturn(mapped);
        when(sportRepository.save(mapped)).thenReturn(saved);
        when(sportMapper.toDto(saved)).thenReturn(savedDto);

        SportDto result = service.create(input);

        assertEquals(savedDto, result);
    }

    @Test
    void createThrowsWhenPayloadIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(null));

        assertEquals("Sport payload cannot be null", exception.getMessage());
    }

    @Test
    void updateUpdatesNameAndReturnsDto() {
        SportDto input = buildSportDto(null, "Updated Sport");
        Sport existing = new Sport();
        Sport saved = new Sport();
        saved.setId(6L);
        SportDto savedDto = buildSportDto(6L, "Updated Sport");

        when(sportRepository.findById(6L)).thenReturn(Optional.of(existing));
        when(sportRepository.save(existing)).thenReturn(saved);
        when(sportMapper.toDto(saved)).thenReturn(savedDto);

        SportDto result = service.update(6L, input);

        assertEquals(savedDto, result);
        assertEquals("Updated Sport", existing.getName());
    }

    @Test
    void updateThrowsWhenPayloadIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(1L, null));

        assertEquals("Sport payload cannot be null", exception.getMessage());
    }

    @Test
    void deleteCallsRepositoryDeleteById() {
        service.delete(9L);

        verify(sportRepository).deleteById(9L);
    }

    private SportDto buildSportDto(Long id, String name) {
        SportDto dto = new SportDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

}
