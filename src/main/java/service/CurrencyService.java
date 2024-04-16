package service;

import dao.CurrencyDao;
import dto.CurrencyDto;
import entity.Currency;
import exception.DatabaseException;
import exception.currency.CurrencyAlreadyExistsException;
import exception.currency.CurrencyNotFoundException;
import org.sqlite.SQLiteException;
import util.mapper.CurrencyModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();

    private final CurrencyDao currencyDao;
    private final CurrencyModelMapper currencyModelMapper;

    private CurrencyService() {
        this.currencyDao = CurrencyDao.getInstance();
        this.currencyModelMapper = new CurrencyModelMapper();
    }

    public List<CurrencyDto> findAll() throws DatabaseException {
        try {
            return currencyDao.findAll().stream()
                    .map(currencyModelMapper::convertToDto)
                    .collect(toList());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public CurrencyDto findByCode(String code) throws DatabaseException, CurrencyNotFoundException {
        try {
            Optional<Currency> currency = currencyDao.findByCode(code);
            if (currency.isEmpty()) {
                throw new CurrencyNotFoundException();
            }
            return currencyModelMapper.convertToDto(currency.get());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public CurrencyDto save(Currency currency) throws DatabaseException, CurrencyAlreadyExistsException {
        try {
            return currencyModelMapper.convertToDto(currencyDao.save(currency));
        } catch (SQLiteException e) {
            throw new CurrencyAlreadyExistsException();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
