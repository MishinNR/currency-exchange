package util.mapper;

import dto.CurrencyDto;
import entity.Currency;
import org.modelmapper.ModelMapper;


public class CurrencyModelMapper {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();
    }

    public static CurrencyDto convertToDto(Currency currency) {
        return MODEL_MAPPER.map(currency, CurrencyDto.class);
    }

    public static Currency convertToEntity(CurrencyDto currencyDto) {
        return MODEL_MAPPER.map(currencyDto, Currency.class);
    }
}
