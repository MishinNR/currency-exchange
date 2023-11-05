package util;

import dto.ExchangeRateDTO;
import entity.ExchangeRate;
import org.modelmapper.ModelMapper;

public class ExchangeRateConverter {
    private final ModelMapper modelMapper;

    public ExchangeRateConverter() {
        this.modelMapper = new ModelMapper();
    }

    public ExchangeRateDTO convertToDTO(ExchangeRate exchangeRate) {
        return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
    }

    public ExchangeRate convertToEntity(ExchangeRateDTO exchangeRateDTO) {
        return modelMapper.map(exchangeRateDTO, ExchangeRate.class);
    }
}
