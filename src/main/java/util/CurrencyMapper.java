package util;

import dto.CurrencyDTO;
import entity.Currency;
import org.modelmapper.ModelMapper;

public class CurrencyMapper {
    private final ModelMapper modelMapper;

    public CurrencyMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CurrencyDTO convertToDTO(Currency currency) {
        return modelMapper.map(currency, CurrencyDTO.class);
    }

    public Currency convertToEntity(CurrencyDTO currencyDTO) {
        return modelMapper.map(currencyDTO, Currency.class);
    }
}
