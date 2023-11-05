package service;

import dao.ExchangeRateDAO;
import dto.CurrencyDTO;
import dto.ExchangeDTO;
import dto.ExchangeRateDTO;
import exception.DatabaseException;
import exception.currency.CurrencyNotFoundException;
import exception.exchange.ExchangeRateNotFoundException;

import java.math.BigDecimal;
import java.sql.SQLException;

import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL64;
import static util.NumberConverter.convertToDoublePrecision;

public class ExchangeService {
    private static final ExchangeService INSTANCE;
    private final CurrencyService currencyService;
    private final ExchangeRateDAO exchangeRateDAO;
    private final ExchangeRateService exchangeRateService;

    static {
        INSTANCE = new ExchangeService();
    }

    private ExchangeService() {
        this.currencyService = CurrencyService.getInstance();
        this.exchangeRateDAO = ExchangeRateDAO.getInstance();
        this.exchangeRateService = ExchangeRateService.getInstance();
    }

    public ExchangeDTO exchange(String baseCurrency, String targetCurrency, BigDecimal amount) throws DatabaseException, CurrencyNotFoundException, ExchangeRateNotFoundException {
        try {
            CurrencyDTO baseCurrencyDTO = currencyService.findByCode(baseCurrency);
            CurrencyDTO targetCurrencyDTO = currencyService.findByCode(targetCurrency);

            final String crossCurrency = "USD";
            if (exchangeRateDAO.findByBaseAndTargetCodes(baseCurrency, targetCurrency).isPresent()) {
                ExchangeRateDTO exchangeRateDTO = exchangeRateService.findByBaseAndTargetCodes(baseCurrency, targetCurrency);
                BigDecimal rate = exchangeRateDTO.getRate();
                return buildExchangeDTO(baseCurrencyDTO, targetCurrencyDTO, rate, amount);
            } else if (exchangeRateDAO.findByBaseAndTargetCodes(targetCurrency, baseCurrency).isPresent()) {
                ExchangeRateDTO exchangeRateDTO = exchangeRateService.findByBaseAndTargetCodes(targetCurrency, baseCurrency);
                BigDecimal targetToBaseRate = exchangeRateDTO.getRate();
                BigDecimal rate = ONE.divide(targetToBaseRate, DECIMAL64);
                return buildExchangeDTO(baseCurrencyDTO, targetCurrencyDTO, rate, amount);
            } else if (exchangeRateDAO.findByBaseAndTargetCodes(crossCurrency, baseCurrency).isPresent() && exchangeRateDAO.findByBaseAndTargetCodes(crossCurrency, targetCurrency).isPresent()) {
                ExchangeRateDTO crossToBaseDTO = exchangeRateService.findByBaseAndTargetCodes(crossCurrency, baseCurrency);
                ExchangeRateDTO crossToTargetDTO = exchangeRateService.findByBaseAndTargetCodes(crossCurrency, targetCurrency);

                BigDecimal crossToBaseRate = crossToBaseDTO.getRate();
                BigDecimal crossToTargetRate = crossToTargetDTO.getRate();
                BigDecimal rate = crossToTargetRate.divide(crossToBaseRate, DECIMAL64);
                return buildExchangeDTO(baseCurrencyDTO, targetCurrencyDTO, rate, amount);
            } else {
                throw new ExchangeRateNotFoundException();
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    private ExchangeDTO buildExchangeDTO(CurrencyDTO baseCurrencyDTO, CurrencyDTO targetCurrencyDTO, BigDecimal rate, BigDecimal amount) {
        return new ExchangeDTO(
                baseCurrencyDTO,
                targetCurrencyDTO,
                convertToDoublePrecision(rate),
                amount,
                convertToDoublePrecision(amount.multiply(rate))
        );
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }
}
