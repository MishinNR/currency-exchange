package dao;

import entity.Currency;
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
public class CurrencyDAO implements CRUD<Currency> {
    private static final CurrencyDAO INSTANCE;

    static {
        INSTANCE = new CurrencyDAO();
    }

    private static final String FIND_ALL = """
            SELECT
                c.id,
                c.code,
                c.full_name,
                c.sign
            FROM currencies AS c
            """;

    private static final String FIND_BY_CODE = """
            SELECT
                c.id,
                c.code,
                c.full_name,
                c.sign
            FROM currencies AS c
            WHERE c.code = ?
            """;

    private static final String SAVE = """
            INSERT INTO currencies (code, full_name, sign)
            VALUES (?, ?, ?)
            """;

    @Override
    public Currency save(Currency currency) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                currency.setId(generatedKeys.getLong("id"));
            }
            return currency;
        }
    }

    @Override
    public List<Currency> findAll() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        }
    }

    @Override
    public Optional<Currency> findById(Long id) throws SQLException {
        throw new SQLException("Method not implemented!");
    }

    @Override
    public Currency update(Currency currency) throws SQLException {
        throw new SQLException("Method not implemented!");
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        throw new SQLException("Method not implemented!");
    }

    public Optional<Currency> findByCode(String code) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);
        }
    }


    public static CurrencyDAO getInstance() {
        return INSTANCE;
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
