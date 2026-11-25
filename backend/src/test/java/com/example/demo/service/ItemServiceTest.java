package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item sample;

    @BeforeEach
    void setUp() {
        sample = new Item("Notebook", "Ruled pages");
        sample.setId(1L);
    }

    @Test
    void findAllReturnsItems() {
        when(itemRepository.findAll()).thenReturn(List.of(sample));

        List<Item> result = itemService.findAll();

        assertEquals(1, result.size());
        assertEquals("Notebook", result.getFirst().getName());
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> itemService.findById(99L));
    }

    @Test
    void createPersistsItem() {
        Item incoming = new Item("Pen", "Blue ink");
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        Item created = itemService.create(incoming);

        assertEquals(2L, created.getId());
        assertEquals("Pen", created.getName());
        verify(itemRepository).save(any(Item.class));
    }
}
