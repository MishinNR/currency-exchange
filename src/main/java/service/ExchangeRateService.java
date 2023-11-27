package service;

import dao.ExchangeRateDAO;
import dto.ExchangeRateDTO;
import entity.ExchangeRate;
import exception.DatabaseException;
import exception.exchange.ExchangeRateAlreadyExistsException;
import exception.exchange.ExchangeRateNotFoundException;
import org.postgresql.util.PSQLException;
import util.ExchangeRateMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE;
    private final ExchangeRateDAO exchangeRateDAO;
    private final ExchangeRateMapper exchangeRateMapper;

    static {
        INSTANCE = new ExchangeRateService();
    }

    private ExchangeRateService() {
        this.exchangeRateDAO = ExchangeRateDAO.getInstance();
        this.exchangeRateMapper = new ExchangeRateMapper();
    }

    public List<ExchangeRateDTO> findAll() throws DatabaseException {
        try {
            return exchangeRateDAO.findAll().stream()
                    .map(exchangeRateMapper::convertToDTO)
                    .collect(toList());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDTO findByBaseAndTargetCodes(String baseCode, String targetCode) throws ExchangeRateNotFoundException, DatabaseException {
        try {
            Optional<ExchangeRate> exchangeRate = exchangeRateDAO.findByBaseAndTargetCodes(baseCode, targetCode);
            if (exchangeRate.isEmpty()) {
                throw new ExchangeRateNotFoundException();
            }
            return exchangeRateMapper.convertToDTO(exchangeRate.get());
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDTO save(ExchangeRate exchangeRate) throws ExchangeRateAlreadyExistsException, DatabaseException {
        try {
            return exchangeRateMapper.convertToDTO(exchangeRateDAO.save(exchangeRate));
        } catch (PSQLException e) {
            throw new ExchangeRateAlreadyExistsException();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public ExchangeRateDTO update(ExchangeRate exchangeRate) throws DatabaseException {
        try {
            return exchangeRateMapper.convertToDTO(exchangeRateDAO.update(exchangeRate));
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
