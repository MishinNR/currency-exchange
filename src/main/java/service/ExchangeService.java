package service;

import dao.ExchangeRateDao;
import dto.CurrencyDto;
import dto.ExchangeDto;
import dto.ExchangeRateDto;
import exception.DatabaseException;
import exception.currency.CurrencyNotFoundException;
import exception.exchange.ExchangeRateNotFoundException;
import util.NumberConverter;

import java.math.BigDecimal;
import java.sql.SQLException;

import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL64;

public class ExchangeService {
    private static final ExchangeService INSTANCE = new ExchangeService();

    private final CurrencyService currencyService;
    private final ExchangeRateDao exchangeRateDao;
    private final ExchangeRateService exchangeRateService;

    private ExchangeService() {
        this.currencyService = CurrencyService.getInstance();
        this.exchangeRateDao = ExchangeRateDao.getInstance();
        this.exchangeRateService = ExchangeRateService.getInstance();
    }

    public ExchangeDto exchange(String baseCurrency, String targetCurrency, BigDecimal amount) throws DatabaseException, CurrencyNotFoundException, ExchangeRateNotFoundException {
        try {
            CurrencyDto baseCurrencyDto = currencyService.findByCode(baseCurrency);
            CurrencyDto targetCurrencyDto = currencyService.findByCode(targetCurrency);

            final String crossCurrency = "USD";
            if (exchangeRateDao.findByBaseAndTargetCodes(baseCurrency, targetCurrency).isPresent()) {
                ExchangeRateDto exchangeRateDTO = exchangeRateService.findByBaseAndTargetCodes(baseCurrency, targetCurrency);
                BigDecimal rate = exchangeRateDTO.getRate();
                return buildExchangeDTO(baseCurrencyDto, targetCurrencyDto, rate, amount);
            } else if (exchangeRateDao.findByBaseAndTargetCodes(targetCurrency, baseCurrency).isPresent()) {
                ExchangeRateDto exchangeRateDTO = exchangeRateService.findByBaseAndTargetCodes(targetCurrency, baseCurrency);
                BigDecimal targetToBaseRate = exchangeRateDTO.getRate();
                BigDecimal rate = ONE.divide(targetToBaseRate, DECIMAL64);
                return buildExchangeDTO(baseCurrencyDto, targetCurrencyDto, rate, amount);
            } else if (exchangeRateDao.findByBaseAndTargetCodes(crossCurrency, baseCurrency).isPresent() && exchangeRateDao.findByBaseAndTargetCodes(crossCurrency, targetCurrency).isPresent()) {
                ExchangeRateDto crossToBaseDTO = exchangeRateService.findByBaseAndTargetCodes(crossCurrency, baseCurrency);
                ExchangeRateDto crossToTargetDTO = exchangeRateService.findByBaseAndTargetCodes(crossCurrency, targetCurrency);
                BigDecimal crossToBaseRate = crossToBaseDTO.getRate();
                BigDecimal crossToTargetRate = crossToTargetDTO.getRate();
                BigDecimal rate = crossToTargetRate.divide(crossToBaseRate, DECIMAL64);
                return buildExchangeDTO(baseCurrencyDto, targetCurrencyDto, rate, amount);
            } else {
                throw new ExchangeRateNotFoundException();
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    private ExchangeDto buildExchangeDTO(CurrencyDto baseCurrencyDto, CurrencyDto targetCurrencyDto, BigDecimal rate, BigDecimal amount) {
        BigDecimal convertedAmount = amount.multiply(rate);
        return new ExchangeDto(
                baseCurrencyDto,
                targetCurrencyDto,
                NumberConverter.convertToDoublePrecision(rate),
                amount,
                NumberConverter.convertToDoublePrecision(convertedAmount)
        );
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }
}
