package service;

import dao.CurrencyDAO;
import dto.CurrencyDTO;
import entity.Currency;
import exception.DatabaseException;
import exception.currency.CurrencyAlreadyExistsException;
import exception.currency.CurrencyNotFoundException;
import org.postgresql.util.PSQLException;
import util.CurrencyConverter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CurrencyService {
    private static final CurrencyService INSTANCE;
    private final CurrencyDAO currencyDAO;
    private final CurrencyConverter currencyConverter;

    static {
        INSTANCE = new CurrencyService();
    }

    private CurrencyService() {
        this.currencyDAO = CurrencyDAO.getInstance();
        this.currencyConverter = new CurrencyConverter();
    }

    public List<CurrencyDTO> findAll() throws DatabaseException {
        try {
            return currencyDAO.findAll().stream()
                    .map(currencyConverter::convertToDTO)
                    .collect(toList());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public CurrencyDTO findByCode(String code) throws DatabaseException, CurrencyNotFoundException {
        try {
            Optional<Currency> currency = currencyDAO.findByCode(code);
            if (currency.isEmpty()) {
                throw new CurrencyNotFoundException();
            }
            return currencyConverter.convertToDTO(currency.get());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public CurrencyDTO save(Currency currency) throws DatabaseException, CurrencyAlreadyExistsException {
        try {
            return currencyConverter.convertToDTO(currencyDAO.save(currency));
        } catch (PSQLException e) {
            throw new CurrencyAlreadyExistsException();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
