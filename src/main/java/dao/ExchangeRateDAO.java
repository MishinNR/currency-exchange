package dao;

import entity.Currency;
import entity.ExchangeRate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateDAO implements CRUD<ExchangeRate> {
    private static final ExchangeRateDAO INSTANCE;

    static {
        INSTANCE = new ExchangeRateDAO();
    }

    private static final String FIND_ALL = """
            SELECT
                er.id,
                bc.id AS base_id,
                bc.code AS base_code,
                bc.full_name AS base_full_name,
                bc.sign AS base_sign,
                tc.id AS target_id,
                tc.code AS target_code,
                tc.full_name AS target_full_name,
                tc.sign AS target_sign,
                er.rate
            FROM exchange_rates AS er
            LEFT JOIN currencies AS bc ON er.base_currency_id = bc.id
            LEFT JOIN currencies AS tc ON er.target_currency_id = tc.id
            """;

    private static final String FIND_BY_PAIR_CODE = """
            SELECT
                er.id,
                bc.id AS base_id,
                bc.code AS base_code,
                bc.full_name AS base_full_name,
                bc.sign AS base_sign,
                tc.id AS target_id,
                tc.code AS target_code,
                tc.full_name AS target_full_name,
                tc.sign AS target_sign,
                er.rate
            FROM exchange_rates AS er
            LEFT JOIN currencies AS bc ON er.base_currency_id = bc.id
            LEFT JOIN currencies AS tc ON er.target_currency_id = tc.id
            WHERE bc.code = ? AND tc.code = ?
            """;

    private static final String SAVE = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE exchange_rates
            SET rate = ?
            WHERE id = ?
            """;

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                exchangeRate.setId(generatedKeys.getLong(1));
            }

            return exchangeRate;
        }
    }

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        }
    }

    public Optional<ExchangeRate> findByBaseAndTargetCodes(String baseCode, String targetCode) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PAIR_CODE)) {
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }
            return Optional.ofNullable(exchangeRate);
        }
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) throws SQLException {
        throw new SQLException("Method not implemented!");
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setLong(2, exchangeRate.getId());
            preparedStatement.executeUpdate();
            return exchangeRate;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        throw new SQLException("Method not implemented!");
    }

    public static ExchangeRateDAO getInstance() {
        return INSTANCE;
    }

    public ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),
                new Currency(
                        resultSet.getLong("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_full_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getLong("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_full_name"),
                        resultSet.getString("target_sign")
                ),
                resultSet.getBigDecimal("rate")
        );
    }
}
