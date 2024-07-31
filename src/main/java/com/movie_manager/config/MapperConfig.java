package com.movie_manager.config;

import com.movie_manager.dto.CountryDTO;
import com.movie_manager.dto.GenreDTO;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Genre;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private static final Converter<String, GenreDTO.NameEnum> genreEnumConverter =
        context -> context.getSource() == null ? null : GenreDTO.NameEnum.fromValue(context.getSource());

    private static final Converter<String, CountryDTO.NameEnum> countryEnumConverter =
        context -> context.getSource() == null ? null : CountryDTO.NameEnum.fromValue(context.getSource());

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping for Genre to GenreDTO
        modelMapper.addMappings(new PropertyMap<Genre, GenreDTO>() {
            @Override
            protected void configure() {
                using(genreEnumConverter).map(source.getName(), destination.getName());
            }
        });

        // Custom mapping for Country to CountryDTO
        modelMapper.addMappings(new PropertyMap<Country, CountryDTO>() {
            @Override
            protected void configure() {
                using(countryEnumConverter).map(source.getName(), destination.getName());
            }
        });

        return modelMapper;
    }

}
