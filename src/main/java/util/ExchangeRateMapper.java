package util;

import dto.ExchangeRateDTO;
import entity.ExchangeRate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Objects;

import static util.NumberConverter.convertToDoublePrecision;

public class ExchangeRateMapper {
    private static final ExchangeRateMapper INSTANCE = new ExchangeRateMapper();
    private final ModelMapper modelMapper;

    public ExchangeRateMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.createTypeMap(ExchangeRate.class, ExchangeRateDTO.class).setPostConverter(toDtoConverter());
    }

    public Converter<ExchangeRate, ExchangeRateDTO> toDtoConverter() {
        return context -> {
            ExchangeRate source = context.getSource();
            ExchangeRateDTO destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFields(ExchangeRate source, ExchangeRateDTO destination) {
        destination.setRate(Objects.isNull(source) || Objects.isNull(source.getRate()) ? null : convertToDoublePrecision(source.getRate()));
    }

    public ExchangeRateDTO convertToDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
    }

    public ExchangeRate convertToEntity(ExchangeRateDTO exchangeRateDTO) {
        return modelMapper.map(exchangeRateDTO, ExchangeRate.class);
    }

    public static ExchangeRateMapper getInstance() {
        return INSTANCE;
    }
}
