package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDTO;
import dto.ExchangeRateDTO;
import entity.ExchangeRate;
import exception.ApplicationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import service.ExchangeRateService;
import util.CurrencyMapper;
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
    private final ObjectMapper objectMapper;
    private final CurrencyMapper currencyMapper;

    public ExchangeRatesServlet() {
        this.currencyService = CurrencyService.getInstance();
        this.exchangeRateService = ExchangeRateService.getInstance();
        this.formFieldValidator = FormFieldValidator.getInstance();
        this.objectMapper = new ObjectMapper();
        this.currencyMapper = new CurrencyMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter writer = resp.getWriter()) {
            try {
                List<ExchangeRateDTO> exchangeRates = exchangeRateService.findAll();
                objectMapper.writeValue(writer, exchangeRates);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
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

                ExchangeRateDTO exchangeRateDTO = exchangeRateService.save(
                        new ExchangeRate(
                                currencyMapper.convertToEntity(currencyService.findByCode(baseCurrencyCode)),
                                currencyMapper.convertToEntity(currencyService.findByCode(targetCurrencyCode)),
                                new BigDecimal(rate)
                        )
                );
                objectMapper.writeValue(writer, exchangeRateDTO);
            } catch (ApplicationException e) {
                resp.setStatus(e.getStatus());
                objectMapper.writeValue(writer, new ErrorDTO(e.getMessage()));
            }
        }
    }
}
