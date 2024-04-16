package util.mapper;

import dto.CurrencyDto;
import entity.Currency;
import org.modelmapper.ModelMapper;


public class CurrencyModelMapper {
    private final ModelMapper modelMapper;

    public CurrencyModelMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CurrencyDto convertToDto(Currency currency) {
        return modelMapper.map(currency, CurrencyDto.class);
    }

    public Currency convertToEntity(CurrencyDto currencyDto) {
        return modelMapper.map(currencyDto, Currency.class);
    }
}
