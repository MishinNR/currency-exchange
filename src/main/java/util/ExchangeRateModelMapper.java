package util;

import dto.ExchangeRateDto;
import entity.ExchangeRate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Objects;

public class ExchangeRateModelMapper {
    private final ModelMapper modelMapper;
    private final NumberConverter numberConverter;

    public ExchangeRateModelMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.createTypeMap(ExchangeRate.class, ExchangeRateDto.class).setPostConverter(toDtoConverter());
        this.numberConverter = new NumberConverter();
    }

    public Converter<ExchangeRate, ExchangeRateDto> toDtoConverter() {
        return context -> {
            ExchangeRate source = context.getSource();
            ExchangeRateDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFields(ExchangeRate source, ExchangeRateDto destination) {
        destination.setRate(
                Objects.isNull(source) || Objects.isNull(source.getRate())
                        ? null
                        : numberConverter.convertToDoublePrecision(source.getRate())
        );
    }

    public ExchangeRateDto convertToDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDto.class);
    }

    public ExchangeRate convertToEntity(ExchangeRateDto exchangeRateDto) {
        return modelMapper.map(exchangeRateDto, ExchangeRate.class);
    }
}
