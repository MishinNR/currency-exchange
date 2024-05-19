package util.mapper;

import dto.ExchangeRateDto;
import entity.ExchangeRate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import util.NumberConverter;

import java.util.Objects;

public class ExchangeRateModelMapper {
    private static final ModelMapper MODEL_MAPPER;

    static  {
        MODEL_MAPPER = new ModelMapper();
        MODEL_MAPPER.createTypeMap(ExchangeRate.class, ExchangeRateDto.class).setPostConverter(toDtoConverter());
    }

    private static Converter<ExchangeRate, ExchangeRateDto> toDtoConverter() {
        return context -> {
            ExchangeRate source = context.getSource();
            ExchangeRateDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private static void mapSpecificFields(ExchangeRate source, ExchangeRateDto destination) {
        destination.setRate(
                Objects.isNull(source) || Objects.isNull(source.getRate())
                        ? null
                        : NumberConverter.convertToDoublePrecision(source.getRate())
        );
    }

    public static ExchangeRateDto convertToDTO(ExchangeRate exchangeRate) {
        return MODEL_MAPPER.map(exchangeRate, ExchangeRateDto.class);
    }

    public static ExchangeRate convertToEntity(ExchangeRateDto exchangeRateDto) {
        return MODEL_MAPPER.map(exchangeRateDto, ExchangeRate.class);
    }
}
