package service;

import dao.ExchangeRateDao;
import dto.ExchangeRateDto;
import entity.ExchangeRate;
import exception.DatabaseException;
import exception.exchange.ExchangeRateAlreadyExistsException;
import exception.exchange.ExchangeRateNotFoundException;
import org.sqlite.SQLiteException;
import util.ExchangeRateModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao;
    private final ExchangeRateModelMapper exchangeRateModelMapper;

    private ExchangeRateService() {
        this.exchangeRateDao = ExchangeRateDao.getInstance();
        this.exchangeRateModelMapper = new ExchangeRateModelMapper();
    }

    public List<ExchangeRateDto> findAll() throws DatabaseException {
        try {
            return exchangeRateDao.findAll().stream()
                    .map(exchangeRateModelMapper::convertToDTO)
                    .collect(toList());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDto findByBaseAndTargetCodes(String baseCode, String targetCode) throws ExchangeRateNotFoundException, DatabaseException {
        try {
            Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByBaseAndTargetCodes(baseCode, targetCode);
            if (exchangeRate.isEmpty()) {
                throw new ExchangeRateNotFoundException();
            }
            return exchangeRateModelMapper.convertToDTO(exchangeRate.get());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDto save(ExchangeRate exchangeRate) throws ExchangeRateAlreadyExistsException, DatabaseException {
        try {
            return exchangeRateModelMapper.convertToDTO(exchangeRateDao.save(exchangeRate));
        } catch (SQLiteException e) {
            throw new ExchangeRateAlreadyExistsException();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDto update(ExchangeRate exchangeRate) throws DatabaseException {
        try {
            return exchangeRateModelMapper.convertToDTO(exchangeRateDao.update(exchangeRate));
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
