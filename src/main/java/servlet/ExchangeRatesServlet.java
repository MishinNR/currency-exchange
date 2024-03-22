package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDto;
import dto.ExchangeRateDto;
import entity.ExchangeRate;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import service.ExchangeRateService;
import util.CurrencyModelMapper;
import util.validation.FormFieldValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final FormFieldValidator formFieldValidator;
    private final CurrencyModelMapper currencyModelMapper;
    private final ObjectMapper objectMapper;

    public ExchangeRatesServlet() {
        this.currencyService = CurrencyService.getInstance();
        this.exchangeRateService = ExchangeRateService.getInstance();
        this.formFieldValidator = new FormFieldValidator();
        this.currencyModelMapper = new CurrencyModelMapper();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                List<ExchangeRateDto> exchangeRates = exchangeRateService.findAll();
                objectMapper.writeValue(writer, exchangeRates);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                String baseCurrencyCode = req.getParameter("baseCurrencyCode");
                String targetCurrencyCode = req.getParameter("targetCurrencyCode");
                String rate = req.getParameter("rate");

                formFieldValidator.validateCode(baseCurrencyCode);
                formFieldValidator.validateCode(targetCurrencyCode);
                formFieldValidator.validateRate(rate);

                ExchangeRateDto exchangeRateDto = exchangeRateService.save(
                        new ExchangeRate(
                                currencyModelMapper.convertToEntity(currencyService.findByCode(baseCurrencyCode)),
                                currencyModelMapper.convertToEntity(currencyService.findByCode(targetCurrencyCode)),
                                new BigDecimal(rate)
                        )
                );
                objectMapper.writeValue(writer, exchangeRateDto);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDto(e.getMessage()));
            }
        }
    }
}
