package com.CarPick1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

public class ToPartsServiceTest {

    @InjectMocks
    private ToPartsService toPartsService;

    @Mock
    private ClassPathResource classPathResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetToParts() throws IOException {
        String modelSeries = "testHyundai";
        String engineType = " Getzбензиновый двигатель объемом 1,4 л";
        String fileName = String.format("parts_database/%s%s.txt", modelSeries, engineType);


        when(classPathResource.getInputStream()).thenReturn(new ByteArrayInputStream("Part1\nPart2\nPart3".getBytes()));

        List<String> parts = toPartsService.getToParts(modelSeries, engineType);

        assertEquals(3, parts.size());
        assertEquals("Part1", parts.get(0));
        assertEquals("Part2", parts.get(1));
        assertEquals("Part3", parts.get(2));
    }
}