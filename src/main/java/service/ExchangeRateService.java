package service;

import dao.ExchangeRateDao;
import dto.ExchangeRateDto;
import entity.ExchangeRate;
import exception.DatabaseException;
import exception.exchange.ExchangeRateAlreadyExistsException;
import exception.exchange.ExchangeRateNotFoundException;
import org.sqlite.SQLiteException;
import util.mapper.ExchangeRateModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao;

    private ExchangeRateService() {
        this.exchangeRateDao = ExchangeRateDao.getInstance();
    }

    public List<ExchangeRateDto> findAll() throws DatabaseException {
        try {
            return exchangeRateDao.findAll().stream()
                    .map(ExchangeRateModelMapper::convertToDTO)
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
            return ExchangeRateModelMapper.convertToDTO(exchangeRate.get());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDto save(ExchangeRate exchangeRate) throws ExchangeRateAlreadyExistsException, DatabaseException {
        try {
            return ExchangeRateModelMapper.convertToDTO(exchangeRateDao.save(exchangeRate));
        } catch (SQLiteException e) {
            throw new ExchangeRateAlreadyExistsException();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDto update(ExchangeRate exchangeRate) throws DatabaseException {
        try {
            return ExchangeRateModelMapper.convertToDTO(exchangeRateDao.update(exchangeRate));
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
