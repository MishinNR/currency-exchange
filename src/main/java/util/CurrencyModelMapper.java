package util;

import dto.CurrencyDto;
import entity.Currency;
import org.modelmapper.ModelMapper;


public class CurrencyModelMapper {
    private static final CurrencyModelMapper INSTANCE = new CurrencyModelMapper();

    private final ModelMapper modelMapper;

    private CurrencyModelMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CurrencyDto convertToDto(Currency currency) {
        return modelMapper.map(currency, CurrencyDto.class);
    }

    public Currency convertToEntity(CurrencyDto currencyDto) {
        return modelMapper.map(currencyDto, Currency.class);
    }

    public static CurrencyModelMapper getInstance() {
        return INSTANCE;
    }
}
